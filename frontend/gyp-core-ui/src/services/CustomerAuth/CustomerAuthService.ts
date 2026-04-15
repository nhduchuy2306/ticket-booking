import {
    CustomerAuthResponseDto,
    CustomerLoginRequestDto,
    CustomerRefreshTokenRequestDto,
    CustomerRegisterRequestDto,
    CustomerRegisterResponseDto, CustomerResponseDto
} from "../../models/generated/auth-service-models";
import { AUTH_SERVICE_PATH } from "../ApiClient.ts";
import { customerApiClient, customerApiWithoutAuth } from "./CustomerApiClient.ts";

export class CustomerAuthService {
    static register = async (requestDto: CustomerRegisterRequestDto): Promise<CustomerRegisterResponseDto> => {
        const response = await customerApiWithoutAuth.post(`/${AUTH_SERVICE_PATH}/customer/auth/register`, requestDto);
        return response.data;
    };

    static login = async (requestDto: CustomerLoginRequestDto): Promise<CustomerAuthResponseDto> => {
        const response = await customerApiWithoutAuth.post(`/${AUTH_SERVICE_PATH}/customer/auth/login`, requestDto);
        return response.data;
    };

    static refreshToken = async (requestDto: CustomerRefreshTokenRequestDto): Promise<CustomerAuthResponseDto> => {
        const response = await customerApiWithoutAuth.post(`/${AUTH_SERVICE_PATH}/customer/auth/refresh-token`, requestDto);
        return response.data;
    };

    static getMe = async (): Promise<CustomerResponseDto> => {
        const response = await customerApiClient.get(`/${AUTH_SERVICE_PATH}/customer/auth/me`);
        return response.data;
    }
}