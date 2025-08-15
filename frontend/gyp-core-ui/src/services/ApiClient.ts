import axios, { AxiosInstance } from "axios";
import { IamService } from "./Iam/IamService.ts";

const BASE_URL = "http://localhost:9999";
const AUTH_SERVICE_PATH = "auths";
const EVENT_SERVICE_PATH = "events";
const TICKET_SERVICE_PATH = "tickets";
const SALE_CHANNEL_SERVICE_PATH = "salechannels";

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
            }
            return Promise.reject(error);
        }
);

export { apiClient, AUTH_SERVICE_PATH, EVENT_SERVICE_PATH, TICKET_SERVICE_PATH, SALE_CHANNEL_SERVICE_PATH };