export function envString(name, fallback = '') {
  const value = globalThis.__ENV?.[name];
  if (value === undefined || value === null || value === '') {
    return fallback;
  }

  return String(value);
}

export function envNumber(name, fallback) {
  const raw = envString(name, '');
  if (raw === '') {
    return fallback;
  }

  const parsed = Number(raw);
  return Number.isFinite(parsed) ? parsed : fallback;
}

export function envBoolean(name, fallback = false) {
  const raw = envString(name, '');
  if (raw === '') {
    return fallback;
  }

  return ['1', 'true', 'yes', 'on'].includes(raw.toLowerCase());
}

export function normalizeBaseUrl(value) {
  const url = String(value ?? '').trim();
  if (!url) {
    return 'http://localhost:9999';
  }

  return url.endsWith('/') ? url.slice(0, -1) : url;
}

export function getBaseUrl() {
  return normalizeBaseUrl(envString('BASE_URL', 'http://localhost:9999'));
}

export function getHeaders() {
  const headers = {
    Accept: 'application/json',
  };

  const token = envString('AUTH_TOKEN', '');
  if (token) {
    const headerName = envString('AUTH_HEADER', 'Authorization');
    headers[headerName] = token.startsWith('Bearer ') ? token : `Bearer ${token}`;
  }

  return headers;
}

