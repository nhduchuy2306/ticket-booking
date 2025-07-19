import { createContext, useContext } from "react";
import { SeatMapResponseDto } from "../../models/generated/event-service-models";

export interface SeatMapFormContextProps {
    entity: SeatMapResponseDto | null;
    setEntity: (entity: SeatMapResponseDto) => void;
}

export const SeatMapFormContext = createContext<SeatMapFormContextProps | undefined>(undefined);

export const useSeatMapFormContext = (): SeatMapFormContextProps => {
    const context = useContext(SeatMapFormContext);
    if (!context) {
        throw new Error("useSeatMapContext must be used within a SeatMapProvider");
    }
    return context;
}