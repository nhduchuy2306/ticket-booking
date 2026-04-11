import { RowLabelPosition, Seat, Section, SectionType } from "../../../models/generated/event-service-models";

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

export interface StageDraftState {
    label: string;
    width: string;
    height: string;
    borderRadius: string;
    positionX: string;
    positionY: string;
}

export interface SectionDraftState {
    name: string;
    type: SectionType;
    ticketTypeId: string;
    positionX: string;
    positionY: string;
    borderRadius: string;
    labelPosition: RowLabelPosition;
    rowCount: string;
    seatsPerRow: string;
    rowSpacing: string;
    seatSpacing: string;
    startX: string;
    capacity: string;
}

export type DraftErrorMap = Partial<Record<keyof StageDraftState | keyof SectionDraftState, string>>;