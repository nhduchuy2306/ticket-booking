import { apiClient, AUTH_SERVICE_PATH } from "../ApiClient.ts";

const STORAGE_KEYS = {
    TOKEN: "token",
    ORG_ID: "organizationId",
    USER_ID: "userId",
    NAME: "name",
    REDIRECT: "postLoginRedirect",
    BOX_OFFICE_MODE: "boxOfficeMode",
};

const CLIENT_ID = "gyp-core-ui";
const TOKEN_PATH = "token";
const REFRESH_TOKEN_PATH = "refresh-token";

const REDIRECT_URL = "http://localhost:3000/callback";
const DEFAULT_RESPONSE_URL = "http://localhost:3000/user-account";

export class IamService {
    private static getAuthServiceUrl(): string {
        const {VITE_REDIRECT_URL, VITE_CLIENT_ID, VITE_API_GATEWAY_URL} = import.meta.env;
        const redirectUri = encodeURIComponent(VITE_REDIRECT_URL || REDIRECT_URL);
        const clientId = VITE_CLIENT_ID || CLIENT_ID;
        const prefixUrl = VITE_API_GATEWAY_URL || "";
        return `${prefixUrl}/auths/iam/oauth/login?redirect_uri=${redirectUri}&client_id=${clientId}`;
    }

    // ---- Local Storage Helpers ----
    private static setItem(key: string, value: string): void {
        localStorage.setItem(key, value);
    }

    private static getItem(key: string): string | null {
        return localStorage.getItem(key);
    }

    private static removeItem(key: string): void {
        localStorage.removeItem(key);
    }

    private static clear(): void {
        localStorage.clear();
    }

    // ---- Token Management ----
    static getToken = (): string | null => this.getItem(STORAGE_KEYS.TOKEN);

    static setToken = (token: string): void =>
            this.setItem(STORAGE_KEYS.TOKEN, token);

    static removeToken = (): void => this.removeItem(STORAGE_KEYS.TOKEN);

    // ---- User Metadata ----
    static setOrganizationId = (id: string): void =>
            this.setItem(STORAGE_KEYS.ORG_ID, id);

    static getOrganizationId = (): string | null =>
            this.getItem(STORAGE_KEYS.ORG_ID);

    static setUserId = (id: string): void =>
            this.setItem(STORAGE_KEYS.USER_ID, id);

    static getUserId = (): string | null => this.getItem(STORAGE_KEYS.USER_ID);

    static setName = (name: string): void =>
            this.setItem(STORAGE_KEYS.NAME, name);

    static getName = (): string | null => this.getItem(STORAGE_KEYS.NAME);

    // ---- Redirect Management ----
    static setCurrentRedirect = (url: string): void =>
            this.setItem(STORAGE_KEYS.REDIRECT, url);

    static getCurrentRedirect = (): string | null =>
            this.getItem(STORAGE_KEYS.REDIRECT);

    static getBoxOfficeMode = (): string | null =>
            this.getItem(STORAGE_KEYS.BOX_OFFICE_MODE);

    // ---- Auth Flow ----
    static redirectToLogin = (): void => {
        this.clear();
        const currentUrl = window.location.pathname + window.location.search;
        this.setCurrentRedirect(currentUrl);
        window.location.href = this.getAuthServiceUrl();
    };

    static handleAuthCallback = async (authCode: string): Promise<void> => {
        try {
            const {data} = await apiClient.post(`${AUTH_SERVICE_PATH}/${TOKEN_PATH}`, {
                clientId: CLIENT_ID,
                code: authCode,
            });

            if (!data?.token) {
                console.error("No token received in response");
                return;
            }

            this.setToken(data.token);

            if (data.organizationId) this.setOrganizationId(data.organizationId);
            if (data.userId) this.setUserId(data.userId);
            if (data.name) this.setName(data.name);

            window.location.href = this.getCurrentRedirect() || DEFAULT_RESPONSE_URL;
        } catch (error) {
            console.error("Error handling auth callback:", error);
        }
    };

    static handleRefreshToken = async (): Promise<void> => {
        try {
            const {data} = await apiClient.get(
                    `${AUTH_SERVICE_PATH}/${REFRESH_TOKEN_PATH}`
            );
            if (!data?.token) {
                console.error("No token received in refresh response");
                return;
            }
            this.setToken(data.token);
        } catch (error) {
            console.error("Error refreshing token:", error);
        }
    };

    static checkExistingAuth = (): void => {
        if (!this.getToken()) {
            this.redirectToLogin();
        }
    };
}
