export interface LoginModel {
    username: string;
    password: string;
}

export interface RegisterModel {
    username: string;
    password: string;
    confirmPassword: string;
}

export interface ResponseModel {
    token: string;
    username: string;
    image: string;
}