import React, { ReactNode } from "react";

interface ProtectedRouteProps {
    children: ReactNode;
}

const GypProtectRoute: React.FC<ProtectedRouteProps> = (props: ProtectedRouteProps) => {
    return <>{props.children}</>
}

export default GypProtectRoute;