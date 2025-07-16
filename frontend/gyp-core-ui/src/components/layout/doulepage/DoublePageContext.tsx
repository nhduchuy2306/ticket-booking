import { createContext, useContext } from "react";

export interface DoublePageContextProps {
    selectedEntity: any;
    setSelectedEntity: (entity: any) => void;
    mode: string;
    setMode: (mode: string) => void;
    isLoading: boolean;
    setIsLoading: (loading: boolean) => void;
    reload: boolean;
    setReload: (reload: boolean) => void;
    handleEntitySelect: (entity: any, newMode: string) => void;
    handleCreate: () => void;
    handleEdit: (entity: any) => void;
    handleView: (entity: any) => void;
    handleReload: () => void;
    handleClearForm: () => void;
    handleChangeMode: (newMode: string) => void;
}

export const DoublePageContext = createContext<DoublePageContextProps | undefined>(undefined);

export const useDoublePageContext = (): DoublePageContextProps => {
    const context = useContext(DoublePageContext);
    if (!context) {
        throw new Error("useDoublePageContext must be used within a DoublePageProvider");
    }
    return context;
}