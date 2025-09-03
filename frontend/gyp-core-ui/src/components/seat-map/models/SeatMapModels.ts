import { Seat, Section, Table } from "../../../models/generated/event-service-models";

export interface SelectedTypeModel {
    type: string;
    data: Section | Seat | Table | undefined;
}