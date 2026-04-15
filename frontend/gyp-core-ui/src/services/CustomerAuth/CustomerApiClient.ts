import axios, { AxiosError, AxiosInstance, InternalAxiosRequestConfig } from "axios";
import { createErrorNotification } from "../../components/notification/Notification.ts";
import { BASE_URL } from "../ApiClient.ts";
import { CustomerAuthStorage } from "./CustomerAuthStorage.ts";

interface RetriableRequestConfig extends InternalAxiosRequestConfig {
	_retry?: boolean;
}

const customerApiClient: AxiosInstance = axios.create({
	baseURL: BASE_URL,
});

const customerApiWithoutAuth: AxiosInstance = axios.create({
	baseURL: BASE_URL,
});

let refreshPromise: Promise<void> | null = null;

customerApiClient.interceptors.request.use(
	(config) => {
		const token = CustomerAuthStorage.getAccessToken();
		if(token) {
			config.headers.Authorization = `Bearer ${token}`;
		}
		return config;
	},
	(error) => Promise.reject(error)
);

customerApiClient.interceptors.response.use(
	(response) => response,
	async (error: AxiosError) => {
		const originalRequest = error.config as RetriableRequestConfig | undefined;
		if(error.response?.status === 401 && originalRequest && !originalRequest._retry && !originalRequest.url?.includes("/refresh-token")) {
			originalRequest._retry = true;
			try {
				await refreshCustomerSession();
				const refreshedToken = CustomerAuthStorage.getAccessToken();
				if(refreshedToken) {
					originalRequest.headers.Authorization = `Bearer ${refreshedToken}`;
				}
				return customerApiClient(originalRequest);
			} catch(refreshError) {
				CustomerAuthStorage.clearTokens();
				return Promise.reject(refreshError);
			}
		}

		if(error.response?.status === 404) {
			createErrorNotification("Not Found", "The requested resource could not be found.");
		} else if(error.response?.status === 403) {
			createErrorNotification("Access Denied", "You do not have permission to access this resource.");
		} else {
			createErrorNotification("Error", error.message || "An unexpected error occurred");
		}
		return Promise.reject(error);
	}
);

customerApiWithoutAuth.interceptors.response.use(
	(response) => response,
	(error: AxiosError) => {
		if(error.response?.status === 404) {
			createErrorNotification("Not Found", "The requested resource could not be found.");
		} else {
			createErrorNotification("Error", error.message || "An unexpected error occurred");
		}
		return Promise.reject(error);
	}
);

const refreshCustomerSession = async (): Promise<void> => {
	if(refreshPromise) {
		return refreshPromise;
	}

	refreshPromise = (async () => {
		const refreshToken = CustomerAuthStorage.getRefreshToken();
		if(!refreshToken) {
			throw new Error("Missing customer refresh token");
		}

		const response = await customerApiWithoutAuth.post("/api/auth/customer/refresh-token", {
			refreshToken,
		});

		if(!response.data?.accessToken || !response.data?.refreshToken) {
			throw new Error("Invalid refresh token response");
		}

		CustomerAuthStorage.setTokens(
			response.data.accessToken,
			response.data.refreshToken,
			response.data.expiresIn || 900
		);
	})().finally(() => {
		refreshPromise = null;
	});

	return refreshPromise;
};

export {
	customerApiClient,
	customerApiWithoutAuth,
};