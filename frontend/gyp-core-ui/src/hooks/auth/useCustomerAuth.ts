import { useState } from "react";
import {
	CustomerAuthResponseDto,
	CustomerLoginRequestDto,
	CustomerRefreshTokenRequestDto,
	CustomerRegisterRequestDto,
	CustomerRegisterResponseDto
} from "../../models/generated/auth-service-models";
import { CustomerAuthService } from "../../services/CustomerAuth/CustomerAuthService.ts";
import { CustomerAuthStorage, CustomerAuthSession } from "../../services/CustomerAuth/CustomerAuthStorage.ts";

export interface UseCustomerAuthResult extends CustomerAuthSession {
	isAuthenticated: boolean;
	setRedirectPath: (redirectPath: string) => void;
	consumeRedirectPath: () => string | null;
	register: (requestDto: CustomerRegisterRequestDto) => Promise<CustomerRegisterResponseDto>;
	login: (requestDto: CustomerLoginRequestDto) => Promise<CustomerAuthResponseDto>;
	refreshToken: (requestDto: CustomerRefreshTokenRequestDto) => Promise<CustomerAuthResponseDto>;
	saveSession: (response: CustomerAuthResponseDto) => void;
	logout: () => void;
}

export const useCustomerAuth = (): UseCustomerAuthResult => {
	const [session, setSession] = useState<CustomerAuthSession>(CustomerAuthStorage.getSession());

	const syncSession = (): void => {
		setSession(CustomerAuthStorage.getSession());
	};

	const saveSession = (response: CustomerAuthResponseDto): void => {
		CustomerAuthStorage.setTokens(response.accessToken, response.refreshToken, response.expiresIn);
		syncSession();
	};

	const logout = (): void => {
		CustomerAuthStorage.clearAll();
		syncSession();
	};

	return {
		...session,
		isAuthenticated: Boolean(session.accessToken),
		setRedirectPath: CustomerAuthStorage.setRedirectPath,
		consumeRedirectPath: CustomerAuthStorage.consumeRedirectPath,
		register: async (requestDto: CustomerRegisterRequestDto) => CustomerAuthService.register(requestDto),
		login: async (requestDto: CustomerLoginRequestDto) => CustomerAuthService.login(requestDto),
		refreshToken: async (requestDto: CustomerRefreshTokenRequestDto) => CustomerAuthService.refreshToken(requestDto),
		saveSession,
		logout,
	};
};