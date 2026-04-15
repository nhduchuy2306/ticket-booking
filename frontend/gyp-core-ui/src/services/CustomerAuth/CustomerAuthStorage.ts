const STORAGE_KEYS = {
	ACCESS_TOKEN: "customerAccessToken",
	REFRESH_TOKEN: "customerRefreshToken",
	EXPIRES_IN: "customerTokenExpiresIn",
	REDIRECT: "customerPostLoginRedirect",
	CUSTOMER_NAME: "customerName",
};

export interface CustomerAuthSession {
	accessToken: string | null;
	refreshToken: string | null | unknown;
	expiresIn: number | null;
}

export class CustomerAuthStorage {
	static getAccessToken = (): string | null => localStorage.getItem(STORAGE_KEYS.ACCESS_TOKEN);

	static getRefreshToken = (): string | null => localStorage.getItem(STORAGE_KEYS.REFRESH_TOKEN);

	static getExpiresIn = (): number | null => {
		const rawValue = localStorage.getItem(STORAGE_KEYS.EXPIRES_IN);
		return rawValue ? Number(rawValue) : null;
	};

	static getSession = (): CustomerAuthSession => ({
		accessToken: this.getAccessToken(),
		refreshToken: this.getRefreshToken(),
		expiresIn: this.getExpiresIn(),
	});

	static getCustomerName = (): string | null => localStorage.getItem(STORAGE_KEYS.CUSTOMER_NAME);

	static setTokens = (accessToken: string, refreshToken: string, expiresIn: number): void => {
		localStorage.setItem(STORAGE_KEYS.ACCESS_TOKEN, accessToken);
		localStorage.setItem(STORAGE_KEYS.REFRESH_TOKEN, refreshToken);
		localStorage.setItem(STORAGE_KEYS.EXPIRES_IN, String(expiresIn));
	};

	static setCustomerName = (customerName: string): void => {
		localStorage.setItem(STORAGE_KEYS.CUSTOMER_NAME, customerName);
	}

	static clearTokens = (): void => {
		localStorage.removeItem(STORAGE_KEYS.ACCESS_TOKEN);
		localStorage.removeItem(STORAGE_KEYS.REFRESH_TOKEN);
		localStorage.removeItem(STORAGE_KEYS.EXPIRES_IN);
		localStorage.removeItem(STORAGE_KEYS.CUSTOMER_NAME);
	};

	static clearAll = (): void => {
		this.clearTokens();
		localStorage.removeItem(STORAGE_KEYS.REDIRECT);
	};

	static setRedirectPath = (redirectPath: string): void => {
		localStorage.setItem(STORAGE_KEYS.REDIRECT, redirectPath);
	};

	static getRedirectPath = (): string | null => localStorage.getItem(STORAGE_KEYS.REDIRECT);

	static consumeRedirectPath = (): string | null => {
		const redirectPath = this.getRedirectPath();
		if(redirectPath) {
			localStorage.removeItem(STORAGE_KEYS.REDIRECT);
		}
		return redirectPath;
	};
}