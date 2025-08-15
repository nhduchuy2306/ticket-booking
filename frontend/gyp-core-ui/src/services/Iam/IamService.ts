import { apiClient, AUTH_SERVICE_PATH } from "../ApiClient.ts";

const TOKEN_KEY = 'tọken';
const CLIENT_ID = 'gyp-core-ui';
const TOKEN_PATH = "token";
const REDIRECT_URL = 'http://localhost:3000/callback';
const RESPONSE_URL = 'http://localhost:3000/user-account';
const AUTH_SERVICE_URL = `http://localhost:9999/auths/iam/oauth/login?redirect_uri=${encodeURIComponent(REDIRECT_URL)}&client_id=${CLIENT_ID}`;

const getToken = (): string | null => {
    return localStorage.getItem(TOKEN_KEY);
}

const setToken = (token: string): void => {
    localStorage.setItem(TOKEN_KEY, token);
}

const setOrganizationId = (organizationId: string): void => {
    localStorage.setItem('organizationId', organizationId);
}

const getOrganizationId = (): string | null => {
    return localStorage.getItem('organizationId');
}

const setUserId = (userId: string): void => {
    localStorage.setItem('userId', userId);
}

const getUserId = (): string | null => {
    return localStorage.getItem('userId');
}

const setName = (username: string): void => {
    localStorage.setItem('name', username);
}

const getName = (): string | null => {
    return localStorage.getItem('name');
}

const removeToken = (): void => {
    localStorage.removeItem(TOKEN_KEY);
}

const redirectToLogin = (): void => {
    window.location.href = AUTH_SERVICE_URL;
}

const handleAuthCallback = async (authCode: string): Promise<void> => {
    try {
        const response = await apiClient.post(`${AUTH_SERVICE_PATH}/${TOKEN_PATH}`, {
            clientId: CLIENT_ID,
            code: authCode
        });
        if (response.data && response.data.token) {
            if (response.data.token) {
                setToken(response.data.token);
                window.location.href = RESPONSE_URL;
            }
            if (response.data.organizationId) {
                setOrganizationId(response.data.organizationId);
            }
            if (response.data.userId) {
                setUserId(response.data.userId);
            }
            if (response.data.name) {
                setName(response.data.name);
            }
        } else {
            console.error('No token received in response');
        }
    } catch (error) {
        console.error('Error handling auth callback:', error);
    }
}

const checkExistingAuth = async (): Promise<void> => {
    const token = getToken();
    if (!token) {
        redirectToLogin();
    }
}

export const IamService = {
    getToken,
    setToken,
    removeToken,
    setOrganizationId,
    getOrganizationId,
    setUserId,
    getUserId,
    setName,
    getName,
    handleAuthCallback,
    checkExistingAuth,
    redirectToLogin,
    CLIENT_ID,
    TOKEN_KEY,
}