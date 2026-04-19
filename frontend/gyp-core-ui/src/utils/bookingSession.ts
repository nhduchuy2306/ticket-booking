import { BookingHoldSession } from "../models/booking/SeatHoldModels.ts";

const BOOKING_SESSION_KEY = "gyp:booking-hold-session";
const WAITING_ROOM_CLEARANCE_KEY = "gyp:waiting-room-clearance";
const DEFAULT_WAITING_ROOM_CLEARANCE_TTL_MS = 15 * 60 * 1000;

export interface WaitingRoomClearance {
    eventId: string;
    nextPath?: string;
    verifiedAt: string;
    expiresAt: string;
}

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

export const clearBookingSession = (): void => {
    sessionStorage.removeItem(BOOKING_SESSION_KEY);
};

export const saveWaitingRoomClearance = (eventId: string, nextPath?: string, expiresAtMs?: number): WaitingRoomClearance | null => {
    if (!eventId) {
        return null;
    }

    const expiresAt = new Date(expiresAtMs || Date.now() + DEFAULT_WAITING_ROOM_CLEARANCE_TTL_MS).toISOString();
    const clearance: WaitingRoomClearance = {
        eventId,
        nextPath,
        verifiedAt: new Date().toISOString(),
        expiresAt,
    };

    sessionStorage.setItem(WAITING_ROOM_CLEARANCE_KEY, JSON.stringify(clearance));
    return clearance;
};

export const loadWaitingRoomClearance = (): WaitingRoomClearance | null => {
    const rawClearance = sessionStorage.getItem(WAITING_ROOM_CLEARANCE_KEY);
    if (!rawClearance) {
        return null;
    }

    try {
        const parsed = JSON.parse(rawClearance) as WaitingRoomClearance;
        if (!parsed?.eventId || !parsed?.expiresAt) {
            sessionStorage.removeItem(WAITING_ROOM_CLEARANCE_KEY);
            return null;
        }

        return parsed;
    } catch {
        sessionStorage.removeItem(WAITING_ROOM_CLEARANCE_KEY);
        return null;
    }
};

export const clearWaitingRoomClearance = (): void => {
    sessionStorage.removeItem(WAITING_ROOM_CLEARANCE_KEY);
};

export const hasValidWaitingRoomClearance = (eventId?: string): boolean => {
    if (!eventId) {
        return false;
    }

    const clearance = loadWaitingRoomClearance();
    if (!clearance || clearance.eventId !== eventId) {
        return false;
    }

    const expiresAtMs = new Date(clearance.expiresAt).getTime();
    if (Number.isNaN(expiresAtMs) || expiresAtMs <= Date.now()) {
        clearWaitingRoomClearance();
        return false;
    }

    return true;
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
