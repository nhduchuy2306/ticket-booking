import React, { ReactNode, useEffect } from "react";
import { IamService } from "../services/Iam/IamService.ts";

interface ProtectedRouteProps {
    children?: ReactNode;
}

const ProtectRoute: React.FC<ProtectedRouteProps> = ({children}) => {
    useEffect(() => {
        // Check if we're returning from auth server with code
        const urlParams = new URLSearchParams(window.location.search);
        const authCode = urlParams.get('code');

        if (authCode) {
            // Exchange code for JWT token
            void IamService.handleAuthCallback(authCode);
        } else {
            // Check if we already have a valid token
            void IamService.checkExistingAuth();
        }
    }, []);

    if (children && IamService.getToken()) {
        return <>{children}</>
    }

    return <></>;
}

export default ProtectRoute;