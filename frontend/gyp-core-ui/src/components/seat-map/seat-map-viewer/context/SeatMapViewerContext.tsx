import { createContext, Dispatch, SetStateAction, useContext } from "react";
import {
    SeatConfig,
    StageConfig,
    TicketTypeResponseDto,
    VenueMap
} from "../../../../models/generated/event-service-models";
import { SelectedSeatModel } from "../../models/SeatMapModels.ts";

interface SeatMapViewerContextProps {
    eventId?: string,
    seatMapId?: string,
    venueMapData: VenueMap,
    stageConfig: StageConfig,
    seatConfig: SeatConfig,
    ticketTypes?: TicketTypeResponseDto[],
    showSeatNumbers: boolean,
    selectedSeats: SelectedSeatModel[],
    setSelectedSeats: Dispatch<SetStateAction<SelectedSeatModel[]>>,
    draggable?: boolean,
    setDraggable?: Dispatch<SetStateAction<boolean>>,
}

export const SeatMapViewerContext = createContext<SeatMapViewerContextProps | undefined>(undefined);

export const useSeatMapViewerContext = (): SeatMapViewerContextProps => {
    const context = useContext(SeatMapViewerContext);
    if (!context) {
        throw new Error("useSeatMapContext must be used within a SeatMapProvider");
    }
    return context;
}