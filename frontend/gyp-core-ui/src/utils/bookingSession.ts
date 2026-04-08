import { BookingHoldSession } from "../models/booking/SeatHoldModels.ts";

const BOOKING_SESSION_KEY = "gyp:booking-hold-session";

export const createHoldToken = (): string => {
    if (typeof crypto !== "undefined" && typeof crypto.randomUUID === "function") {
        return crypto.randomUUID();
    }

    return `hold_${Date.now()}_${Math.random().toString(16).slice(2)}`;
};

export const saveBookingSession = (session: BookingHoldSession): void => {
    sessionStorage.setItem(BOOKING_SESSION_KEY, JSON.stringify(session));
};

export const loadBookingSession = (): BookingHoldSession | null => {
    const rawSession = sessionStorage.getItem(BOOKING_SESSION_KEY);
    if (!rawSession) {
        return null;
    }

    try {
        return JSON.parse(rawSession) as BookingHoldSession;
    } catch {
        sessionStorage.removeItem(BOOKING_SESSION_KEY);
        return null;
    }
};

export const updateBookingSession = (patch: Partial<BookingHoldSession>): BookingHoldSession | null => {
    const currentSession = loadBookingSession();
    if (!currentSession) {
        return null;
    }

    const nextSession = {...currentSession, ...patch};
    saveBookingSession(nextSession);
    return nextSession;
};

export const clearBookingSession = (): void => {
    sessionStorage.removeItem(BOOKING_SESSION_KEY);
};

export const getHoldCountdownSeconds = (expiresAt?: string): number => {
    if (!expiresAt) {
        return 0;
    }

    const expiresAtMs = new Date(expiresAt).getTime();
    if (Number.isNaN(expiresAtMs)) {
        return 0;
    }

    return Math.max(0, Math.floor((expiresAtMs - Date.now()) / 1000));
};

export const formatCountdown = (seconds: number): string => {
    const safeSeconds = Math.max(0, seconds);
    const minutes = Math.floor(safeSeconds / 60);
    const remainingSeconds = safeSeconds % 60;

    return `${minutes.toString().padStart(2, "0")}:${remainingSeconds.toString().padStart(2, "0")}`;
};

export const isHoldExpired = (expiresAt?: string): boolean => getHoldCountdownSeconds(expiresAt) === 0;
