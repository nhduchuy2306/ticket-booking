import { createContext, Dispatch, SetStateAction, useContext } from "react";
import { CustomerResponseDto } from "../models/generated/auth-service-models";

interface GypPageContextProps {
    customerResponseDto?: CustomerResponseDto | null;
    setCustomerResponseDto?: Dispatch<SetStateAction<CustomerResponseDto | null>>
}

export const GypPageContext = createContext<GypPageContextProps | undefined>(undefined);

export const useGypPageContext = (): GypPageContextProps => {
    const context = useContext(GypPageContext);
    if (!context) {
        throw new Error("useGypPageContext must be used within a GypPageProvider");
    }
    return context;
}