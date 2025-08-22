import { apiClient, AUTH_SERVICE_PATH } from "../ApiClient.ts";

const TOKEN_KEY = 'tọken';
const CLIENT_ID = 'gyp-core-ui';
const TOKEN_PATH = "token";
const REDIRECT_URL = 'http://localhost:3000/callback';
const RESPONSE_URL = 'http://localhost:3000/user-account';
const AUTH_SERVICE_URL = `http://localhost:9999/auths/iam/oauth/login?redirect_uri=${encodeURIComponent(REDIRECT_URL)}&client_id=${CLIENT_ID}`;
const REFRESH_TOKEN_PATH = "refresh-token";

export class IamService {
    static getToken = (): string | null => {
        return localStorage.getItem(TOKEN_KEY);
    }

    static setToken = (token: string): void => {
        localStorage.setItem(TOKEN_KEY, token);
    }

    static setOrganizationId = (organizationId: string): void => {
        localStorage.setItem('organizationId', organizationId);
    }

    static getOrganizationId = (): string | null => {
        return localStorage.getItem('organizationId');
    }

    static setUserId = (userId: string): void => {
        localStorage.setItem('userId', userId);
    }

    static getUserId = (): string | null => {
        return localStorage.getItem('userId');
    }

    static setName = (username: string): void => {
        localStorage.setItem('name', username);
    }

    static getName = (): string | null => {
        return localStorage.getItem('name');
    }

    static removeToken = (): void => {
        localStorage.removeItem(TOKEN_KEY);
    }

    static clearLocalStorage = (): void => {
        localStorage.clear();
    }

    static redirectToLogin = (): void => {
        this.clearLocalStorage();
        window.location.href = AUTH_SERVICE_URL;
    }

    static handleAuthCallback = async (authCode: string): Promise<void> => {
        try {
            const response = await apiClient.post(`${AUTH_SERVICE_PATH}/${TOKEN_PATH}`, {
                clientId: CLIENT_ID,
                code: authCode
            });
            if (response.data && response.data.token) {
                if (response.data.token) {
                    this.setToken(response.data.token);
                    window.location.href = RESPONSE_URL;
                }
                if (response.data.organizationId) {
                    this.setOrganizationId(response.data.organizationId);
                }
                if (response.data.userId) {
                    this.setUserId(response.data.userId);
                }
                if (response.data.name) {
                    this.setName(response.data.name);
                }
            } else {
                console.error('No token received in response');
            }
        } catch (error) {
            console.error('Error handling auth callback:', error);
        }
    }

    static handleRefreshToken = async (): Promise<void> => {
        const token = await apiClient.get(`${AUTH_SERVICE_PATH}/${REFRESH_TOKEN_PATH}`);
        if (token && token.data && token.data.token) {
            this.setToken(token.data.token);
        } else {
            console.error('No token received in refresh response');
        }
    }

    static checkExistingAuth = async (): Promise<void> => {
        const token = this.getToken();
        if (!token) {
            this.redirectToLogin();
        }
    }
}