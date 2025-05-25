import axios from "axios";
import { LoginModel, RegisterModel, ResponseModel } from "../../models/AuthService/LoginModel.ts";

export class AuthService {
    static AUTH_SERVICE_PATH = "auths";

    static async login(login: LoginModel): Promise<ResponseModel> {
        const response = await axios.post("http://localhost:9000/login", login);
        return response.data;
    }

    static async register(register: RegisterModel): Promise<ResponseModel> {
        const response = await axios.post("http://localhost:9000/register", register);
        return response.data;
    }
}