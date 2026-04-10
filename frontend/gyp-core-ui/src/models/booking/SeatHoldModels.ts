import { SeatInventoryStatus, SeatStatus } from "../generated/event-service-models";

export interface SeatAvailabilityDto {
    seatId?: string;
    seatKey?: string;
    seatLabel?: string;
    sectionId?: string;
    rowId?: string;
    ticketTypeId?: string;
    price?: number;
    status?: SeatInventoryStatus;
    available?: boolean;
    holdToken?: string;
    holdExpiresAt?: string;
}

export interface SeatHoldRequestDto {
    eventId?: string;
    seatIds?: string[];
    seatKeys?: string[];
    holdToken?: string;
    userId?: string | null;
}

export interface SeatHoldResponseDto {
    holdToken?: string;
    holdExpiresAt?: string;
    seatIds?: string[];
    seats?: SeatAvailabilityDto[];
}

export interface BookingSeatSummary {
    seatId: string;
    seatName?: string;
    sectionName?: string;
    ticketTypeId?: string;
    price: number;
    status?: SeatStatus;
    holdToken?: string;
    holdExpiresAt?: string;
}

export interface BookingHoldSession {
    eventId: string;
    eventName?: string;
    seatMapId?: string;
    orderId?: string;
    holdToken: string;
    holdExpiresAt: string;
    seatIds: string[];
    selectedSeats: BookingSeatSummary[];
    totalAmount: number;
    customerEmail?: string;
    userId?: string | null;
}
