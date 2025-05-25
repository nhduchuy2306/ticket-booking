import React, { ReactNode } from "react";
import { Navigate } from "react-router-dom";

interface ProtectedRouteProps {
    children: ReactNode;
}

const ProtectRoute: React.FC<ProtectedRouteProps> = (props: ProtectedRouteProps) => {
    const token = localStorage.getItem("token");
    if (!token) {
        return <Navigate to="/login" replace/>
    }
    return <>{props.children}</>
}

export default ProtectRoute;