import { LoginModel, RegisterModel, ResponseModel } from "../../models/AuthService/LoginModel.ts";
import { apiClient, AUTH_SERVICE_PATH } from "../ApiClient.ts";

const LOGIN_PATH = "login";
const REGISTER_PATH = "register";

const login = async (login: LoginModel) => {
    const response = await apiClient.post(`/${AUTH_SERVICE_PATH}/${LOGIN_PATH}`, login);
    return response.data;
}

const register = async (register: RegisterModel): Promise<ResponseModel> => {
    const response = await apiClient.post(`/${AUTH_SERVICE_PATH}/${REGISTER_PATH}`, register);
    return response.data;
}

export const AuthService = {
    login,
    register
}