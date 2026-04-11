import { Seat, Section } from "../../../models/generated/event-service-models";

export interface SelectedTypeModel {
    type: string;
    data: Section | Seat | undefined;
}

export interface SelectedSeatModel {
    seat: Seat;
    section?: Section;
    holdToken?: string;
    holdExpiresAt?: string;
}

export interface OrderDetailModel {
    eventId?: string;
    seatMapId?: string;
    selectedSeats?: SelectedSeatModel[];
    totalAmount?: number;
    ticketTypeMap?: Map<string, number>;
    holdToken?: string;
    holdExpiresAt?: string;
}