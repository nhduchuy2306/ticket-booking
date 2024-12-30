import { LoginRequestModel } from "../models/LoginRequestModel.ts";

export class AuthService {
    login(request: LoginRequestModel) {
        return "Login successfully with " + request.username;
    }
}