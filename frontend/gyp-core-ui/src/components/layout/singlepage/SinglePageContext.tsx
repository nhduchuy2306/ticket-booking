import { createContext, useContext } from "react";

export interface SinglePageContextProps {
    isLoading: boolean;
    setIsLoading: (loading: boolean) => void;
    reload: boolean;
    setReload: (reload: boolean) => void;
    handleCreate: () => void;
    handleEdit: (entity: any) => void;
    handleView: (entity: any) => void;
    handleReload: () => void;
    handleBack: () => void;
}

export const SinglePageContext = createContext<SinglePageContextProps | undefined>(undefined);

export const useSinglePageContext = (): SinglePageContextProps => {
    const context = useContext(SinglePageContext);
    if (!context) {
        throw new Error("useSinglePageContext must be used within a SinglePageProvider");
    }
    return context;
}