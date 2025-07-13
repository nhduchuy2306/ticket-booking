import React, { useState } from "react";
import { SinglePageContext, SinglePageContextProps } from "./SinglePageContext.tsx";

export interface SinglePageLayoutProps {
    children: React.ReactNode;
    onNavigate?: (path: string, entity?: any) => void;
}

const SinglePageLayout: React.FC<SinglePageLayoutProps> = ({children, onNavigate}) => {
    const [isLoading, setIsLoading] = useState(false);
    const [reload, setReload] = useState<boolean>(false);

    const handleCreate = () => {
        onNavigate?.('/create');
    };

    const handleEdit = (entity: any) => {
        onNavigate?.('/edit', entity);
    };

    const handleView = (entity: any) => {
        onNavigate?.('/view', entity);
    };

    const handleReload = () => {
        setReload(true);
    };

    const handleBack = () => {
        onNavigate?.('/');
    };

    const contextValue: SinglePageContextProps = {
        isLoading,
        setIsLoading,
        reload,
        setReload,
        handleCreate,
        handleEdit,
        handleView,
        handleReload,
        handleBack,
    };

    return (
            <SinglePageContext.Provider value={contextValue}>
                <div className="w-full h-full bg-white">
                    {children}
                </div>
            </SinglePageContext.Provider>
    );
};

export default SinglePageLayout;