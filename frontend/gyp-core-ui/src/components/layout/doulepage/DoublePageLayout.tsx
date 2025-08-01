import { Flex } from "antd";
import React, { useState } from "react";
import { FormState } from "../models/LayoutModel.ts";
import { DoublePageContext, DoublePageContextProps } from "./DoublePageContext.tsx";

export interface DoublePageLayoutProps {
    children: React.ReactNode;
    initialEntity?: any;
    onEntityChange?: (entity: any, mode: string) => void;
}

const DoublePageLayout: React.FC<DoublePageLayoutProps> = ({children, initialEntity, onEntityChange}) => {
    const [selectedEntity, setSelectedEntity] = useState(initialEntity || null);
    const [mode, setMode] = useState<string>(FormState.READ_ONLY.key);
    const [isLoading, setIsLoading] = useState(false);
    const [reload, setReload] = useState(false);

    const handleEntitySelect = (entity: any, newMode: string) => {
        setSelectedEntity(entity);
        setMode(newMode);
        onEntityChange?.(entity, newMode);
    };

    const handleCreate = () => {
        handleEntitySelect(null, FormState.CREATE.key);
    };

    const handleEdit = (entity: any) => {
        handleEntitySelect(entity, FormState.EDIT.key);
    };

    const handleView = (entity: any) => {
        handleEntitySelect(entity, FormState.READ_ONLY.key);
    };

    const handleReload = () => {
        setReload(true);
    };

    const handleClearForm = () => {
        setSelectedEntity(null);
        setMode(FormState.READ_ONLY.key);
    };

    const handleChangeMode = (newMode: string) => {
        setMode(newMode);
    }

    const contextValue: DoublePageContextProps = {
        selectedEntity,
        setSelectedEntity,
        mode,
        setMode,
        isLoading,
        setIsLoading,
        reload,
        setReload,
        handleEntitySelect,
        handleCreate,
        handleEdit,
        handleView,
        handleReload,
        handleClearForm,
        handleChangeMode
    };

    return (
            <DoublePageContext.Provider value={contextValue}>
                <Flex vertical={false}
                      align="flex-start"
                      justify="flex-start"
                      gap="large"
                      className="w-full bg-white !p-4">
                    {children}
                </Flex>
            </DoublePageContext.Provider>
    );
}

export default DoublePageLayout;