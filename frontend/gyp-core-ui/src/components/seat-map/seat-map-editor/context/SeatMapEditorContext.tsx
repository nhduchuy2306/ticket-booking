import { createContext, Dispatch, SetStateAction, useContext } from "react";
import {
    SeatConfig,
    StageConfig,
    TicketTypeResponseDto,
    VenueMap
} from "../../../../models/generated/event-service-models";
import { SelectedTypeModel } from "../models/SeatMapModels.ts";

interface SeatMapEditorContextProps {
    venueData: VenueMap,
    stageConfig: StageConfig,
    seatConfig: SeatConfig,
    seatTypes?: TicketTypeResponseDto[],
    showSeatNumbers: boolean,
    selectedSeats: string[],
    setSelectedSeats: Dispatch<SetStateAction<string[]>>,
    draggable?: boolean,
    setDraggable?: Dispatch<SetStateAction<boolean>>,
    selectedType?: SelectedTypeModel,
    setSelectedType?: Dispatch<SetStateAction<SelectedTypeModel | undefined>>,
}

export const SeatMapEditorContext = createContext<SeatMapEditorContextProps | undefined>(undefined);

export const useSeatMapEditorContext = (): SeatMapEditorContextProps => {
    const context = useContext(SeatMapEditorContext);
    if (!context) {
        throw new Error("useSeatMapContext must be used within a SeatMapProvider");
    }
    return context;
}