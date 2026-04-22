
import http from 'k6/http';
import { fail } from 'k6';

import { envBoolean, envNumber, envString, getAuthorizationHeader, getBaseUrl, getJsonHeaders } from '../config.js';

type CustomerAuthResponse = {
  accessToken: string;
  refreshToken: string;
  expiresIn: number;
};

type AuthCache = {
  accessToken: string;
  refreshToken: string;
  expiresAt: number;
};

let cachedAuth: AuthCache | null = null;

function buildUrl(baseUrl: string, path: string): string {
  return `${baseUrl}${path}`;
}

function loginPath(): string {
  return envString('AUTH_LOGIN_PATH', '/auths/customer/auth/login');
}

function refreshPath(): string {
  return envString('AUTH_REFRESH_PATH', '/auths/customer/auth/refresh-token');
}

function authEnabled(): boolean {
  return envBoolean('AUTH_ENABLED', true);
}

function loginEmail(): string {
  return envString('AUTH_LOGIN_EMAIL', '');
}

function loginPassword(): string {
  return envString('AUTH_LOGIN_PASSWORD', '');
}

function refreshTokenThresholdMs(): number {
  const seconds = envNumber('AUTH_REFRESH_THRESHOLD_SECONDS', 30);
  return Math.max(0, seconds) * 1000;
}

function parseAuthResponse(response: http.RefinedResponse<any>): CustomerAuthResponse {
  const json = response.json() as Partial<CustomerAuthResponse> | null;
  const accessToken = json?.accessToken;
  const refreshToken = json?.refreshToken;
  const expiresIn = json?.expiresIn;

  if (!accessToken || !refreshToken || typeof expiresIn !== 'number') {
    fail('Auth response from auth-service is missing accessToken, refreshToken, or expiresIn.');
  }

  return { accessToken, refreshToken, expiresIn };
}

function persistAuth(responsePayload: CustomerAuthResponse): CustomerAuthResponse {
  cachedAuth = {
    accessToken: responsePayload.accessToken,
    refreshToken: responsePayload.refreshToken,
    expiresAt: Date.now() + responsePayload.expiresIn * 1000,
  };

  return responsePayload;
}

function login(): CustomerAuthResponse {
  const baseUrl = getBaseUrl();
  const email = loginEmail();
  const password = loginPassword();

  if (!email || !password) {
    fail('AUTH_ENABLED=true requires AUTH_LOGIN_EMAIL and AUTH_LOGIN_PASSWORD.');
  }

  const response = http.post(
    buildUrl(baseUrl, loginPath()),
    JSON.stringify({ email, password }),
    {
      headers: getJsonHeaders(),
      tags: {
        endpoint: 'customer-auth-login',
        flow: 'auth',
      },
    },
  );

  if (response.status < 200 || response.status >= 300) {
    fail(`Customer login failed with status ${response.status}: ${response.body}`);
  }

  return persistAuth(parseAuthResponse(response));
}

function refresh(refreshToken: string): CustomerAuthResponse {
  const baseUrl = getBaseUrl();
  const response = http.post(
    buildUrl(baseUrl, refreshPath()),
    JSON.stringify({ refreshToken }),
    {
      headers: getJsonHeaders(),
      tags: {
        endpoint: 'customer-auth-refresh-token',
        flow: 'auth',
      },
    },
  );

  if (response.status < 200 || response.status >= 300) {
    fail(`Customer refresh token failed with status ${response.status}: ${response.body}`);
  }

  return persistAuth(parseAuthResponse(response));
}

export function getCustomerAccessToken(): string {
  if (!authEnabled()) {
    return envString('AUTH_TOKEN', '');
  }

  const now = Date.now();
  if (cachedAuth && cachedAuth.expiresAt - refreshTokenThresholdMs() > now) {
    return cachedAuth.accessToken;
  }

  if (cachedAuth?.refreshToken) {
    try {
      return refresh(cachedAuth.refreshToken).accessToken;
    } catch {
      cachedAuth = null;
    }
  }

  return login().accessToken;
}

export function getCustomerAuthHeaders(): Record<string, string> {
  const token = getCustomerAccessToken();
  return token ? getAuthorizationHeader(token) : {};
}

