import axios from "axios";
import { LoginModel, RegisterModel, ResponseModel } from "../../models/AuthService/LoginModel.ts";
import { apiClient, AUTH_SERVICE_PATH } from "../ApiClient.ts";

const LOGIN_PATH = "login";
const REGISTER_PATH = "register";

export class AuthService {
    static async login(login: LoginModel): Promise<ResponseModel> {
        const response = await apiClient.post(`/${AUTH_SERVICE_PATH}/${LOGIN_PATH}`, login);
        return response.data;
    }

    static async register(register: RegisterModel): Promise<ResponseModel> {
        const response = await axios.post(`/${AUTH_SERVICE_PATH}/${REGISTER_PATH}`, register);
        return response.data;
    }
}