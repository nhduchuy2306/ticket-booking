import { Seat, Section, Table } from "../../../models/generated/event-service-models";

export interface SelectedTypeModel {
    type: string;
    data: Section | Seat | Table | undefined;
}

export interface SelectedSeatModel {
    seat: Seat;
    section?: Section;
}

export interface OrderDetailModel {
    eventId?: string;
    seatMapId?: string;
    selectedSeats?: SelectedSeatModel[];
    totalAmount?: number;
    ticketTypeMap?: Map<string, number>;
}