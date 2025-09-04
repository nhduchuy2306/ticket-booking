import axios, { AxiosInstance } from "axios";
import { createErrorNotification } from "../components/notification/Notification.ts";
import { IamService } from "./Iam/IamService.ts";

const BASE_URL = "http://localhost:9999";
const AUTH_SERVICE_PATH = "auths";
const EVENT_SERVICE_PATH = "events";
const TICKET_SERVICE_PATH = "tickets";
const SALE_CHANNEL_SERVICE_PATH = "salechannels";
const ORDER_SERVICE_PATH = "orders";

const apiClient: AxiosInstance = axios.create({
    baseURL: BASE_URL,
});

apiClient.interceptors.request.use(
        (config) => {
            const token = IamService.getToken();
            if (token) {
                config.headers.Authorization = `Bearer ${token}`;
            }
            return config;
        },
        (error) => {
            return Promise.reject(error);
        }
);

apiClient.interceptors.response.use(
        (response) => {
            return response;
        },
        (error) => {
            if (error.response?.status === 401) {
                IamService.removeToken();
                IamService.redirectToLogin();
            } else if (error.response?.status === 403) {
                createErrorNotification("Access Denied", "You do not have permission to access this resource.");
            } else if (error.response?.status === 404) {
                createErrorNotification("Not Found", "The requested resource could not be found.");
            } else {
                createErrorNotification("Error", error.message || "An unexpected error occurred");
            }
            return Promise.reject(error);
        }
);

const apiWithoutAuth: AxiosInstance = axios.create({
    baseURL: BASE_URL,
});

apiWithoutAuth.interceptors.response.use(
        (response) => {
            return response;
        },
        (error) => {
            if (error.response?.status === 404) {
                createErrorNotification("Not Found", "The requested resource could not be found.");
            } else {
                createErrorNotification("Error", error.message || "An unexpected error occurred");
            }
            return Promise.reject(error);
        }
);

export {
    apiClient,
    apiWithoutAuth,
    BASE_URL,
    AUTH_SERVICE_PATH,
    EVENT_SERVICE_PATH,
    TICKET_SERVICE_PATH,
    SALE_CHANNEL_SERVICE_PATH,
    ORDER_SERVICE_PATH
};