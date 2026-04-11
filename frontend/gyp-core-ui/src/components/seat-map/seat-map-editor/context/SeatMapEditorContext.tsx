import { createContext, Dispatch, SetStateAction, useContext } from "react";

interface SeatMapEditorContextProps {
    showSeatNumbers?: boolean,
    setShowSeatNumbers?: Dispatch<SetStateAction<boolean>>,
}

export const SeatMapEditorContext = createContext<SeatMapEditorContextProps | undefined>(undefined);

export const useSeatMapEditorContext = (): SeatMapEditorContextProps => {
    const context = useContext(SeatMapEditorContext);
    if (!context) {
        throw new Error("useSeatMapContext must be used within a SeatMapProvider");
    }
    return context;
}