import { createContext, Dispatch, SetStateAction, useContext } from "react";
import { SeatConfig, StageConfig, VenueMap } from "../../../models/generated/event-service-models";

export interface SeatMapContextProps {
    venueData: VenueMap,
    stageConfig: StageConfig,
    seatConfig: SeatConfig,
    seatTypeColors?: SeatConfig['seatTypeColors'],
    showSeatNumbers: boolean,
    selectedSeats: string[],
    setSelectedSeats: Dispatch<SetStateAction<string[]>>,
    draggable: boolean,
    setDraggable: Dispatch<SetStateAction<boolean>>,
}

export const SeatMapContext = createContext<SeatMapContextProps | undefined>(undefined);

export const useSeatMapContext = (): SeatMapContextProps => {
    const context = useContext(SeatMapContext);
    if (!context) {
        throw new Error("useSeatMapContext must be used within a SeatMapProvider");
    }
    return context;
}