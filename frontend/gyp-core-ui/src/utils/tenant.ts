const BASE_DOMAIN = import.meta.env.VITE_BASE_DOMAIN || "lvh.me";

export function extractOrgSlug(): string | null {
    const hostname = window.location.hostname;
    if (hostname === BASE_DOMAIN || hostname === `www.${BASE_DOMAIN}`) {
        return null;
    }
    if (!hostname.endsWith(`.${BASE_DOMAIN}`)) {
        return null;
    }
    const slug = hostname.replace(`.${BASE_DOMAIN}`, "");
    return slug || null;
}

export function isPlatformMode(): boolean {
    return extractOrgSlug() === null;
}

