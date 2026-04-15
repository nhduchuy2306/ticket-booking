import React, { ReactNode, useState } from "react";
import { GypPageContext } from "./GypPageContext.tsx";
import { CustomerResponseDto } from "../models/generated/auth-service-models";

interface ProtectedRouteProps {
    children: ReactNode;
}

const GypProtectRoute: React.FC<ProtectedRouteProps> = (props: ProtectedRouteProps) => {
    const [customerResponseDto, setCustomerResponseDto] = useState<CustomerResponseDto | null>(null);

    return (
            <GypPageContext.Provider value={{customerResponseDto, setCustomerResponseDto}}>
                {props.children}
            </GypPageContext.Provider>
    )
}

export default GypProtectRoute;