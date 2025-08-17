import { LoginRequestDto, LoginResponseDto, RegisterRequestDto } from "../../models/generated/auth-service-models";
import { apiClient, AUTH_SERVICE_PATH } from "../ApiClient.ts";

const LOGIN_PATH = "login";
const REGISTER_PATH = "register";

const login = async (login: LoginRequestDto) => {
    const response = await apiClient.post(`/${AUTH_SERVICE_PATH}/${LOGIN_PATH}`, login);
    return response.data;
}

const register = async (register: RegisterRequestDto): Promise<LoginResponseDto> => {
    const response = await apiClient.post(`/${AUTH_SERVICE_PATH}/${REGISTER_PATH}`, register);
    return response.data;
}

export const AuthService = {
    login,
    register
}