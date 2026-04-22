export function envString(name: string, fallback = ''): string {
  const value = __ENV?.[name];
  if (value === undefined || value === null || value === '') {
    return fallback;
  }

  return String(value);
}

export function envNumber(name: string, fallback: number): number {
  const raw = envString(name, '');
  if (raw === '') {
    return fallback;
  }

  const parsed = Number(raw);
  return Number.isFinite(parsed) ? parsed : fallback;
}

export function envBoolean(name: string, fallback = false): boolean {
  const raw = envString(name, '');
  if (raw === '') {
    return fallback;
  }

  return ['1', 'true', 'yes', 'on'].includes(raw.toLowerCase());
}

export function normalizeBaseUrl(value: string | undefined | null): string {
  const url = String(value ?? '').trim();
  if (!url) {
    return 'http://localhost:9999';
  }

  return url.endsWith('/') ? url.slice(0, -1) : url;
}

export function getBaseUrl(): string {
  return normalizeBaseUrl(envString('BASE_URL', 'http://localhost:9999'));
}

export function getJsonHeaders(): Record<string, string> {
  return {
    Accept: 'application/json',
    'Content-Type': 'application/json',
  };
}

export function getAuthorizationHeader(token: string): Record<string, string> {
  return {
    Authorization: token.startsWith('Bearer ') ? token : `Bearer ${token}`,
  };
}


