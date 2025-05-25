import axios, { AxiosInstance } from "axios";
import { UserGroupPermissionModel } from "../../models/AuthService/UserGroupModel.ts";

const apiClient: AxiosInstance = axios.create({
    baseURL: "http://localhost:9000",
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

const USER_GROUP_PATH = "usergroups";
const USER_GROUP_ACTIONS_PATH = "usergroupactions";

export class UserGroupService {
    static async getApplicationPermissions(): Promise<UserGroupPermissionModel[]> {
        const res = await apiClient.get(`/${USER_GROUP_PATH}/${USER_GROUP_ACTIONS_PATH}`);
        return res.data;
    }
}