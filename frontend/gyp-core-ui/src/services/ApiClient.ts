import axios, { AxiosInstance } from "axios";

const AUTH_SERVICE_PATH = "auths";
const EVENT_SERVICE_PATH = "events";

const apiClient: AxiosInstance = axios.create({
    baseURL: "http://localhost:9999",
});

apiClient.interceptors.request.use(
        (config) => {
            const token = localStorage.getItem("token");
            if (token) {
                config.headers.Authorization = `Bearer ${token}`;
            }
            return config;
        },
        (error) => {
            return Promise.reject(error);
        }
);

export { apiClient, AUTH_SERVICE_PATH, EVENT_SERVICE_PATH }