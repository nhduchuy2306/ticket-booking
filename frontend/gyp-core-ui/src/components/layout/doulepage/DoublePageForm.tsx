import { notification } from "antd";
import React from "react";
import { BaseService, FormState } from "../models/LayoutModel.ts";
import { useDoublePageContext } from "./DoublePageContext.tsx";

export interface DoublePageFormProps {
    service: BaseService<any, any>;
    children: (params: {
        entity: any;
        mode: string;
        onSave: (values: any) => Promise<void>;
        onCancel: () => void;
    }) => React.ReactNode;
    successMessages?: {
        create?: string;
        update?: string;
    };
}

export const DoublePageForm: React.FC<DoublePageFormProps> = ({
                                                                  service,
                                                                  children,
                                                                  successMessages = {
                                                                      create: "Item created successfully",
                                                                      update: "Item updated successfully"
                                                                  }
                                                              }) => {
    const {
        selectedEntity,
        mode,
        setIsLoading,
        handleReload,
        handleClearForm
    } = useDoublePageContext();

    const handleSave = async (values: any) => {
        setIsLoading(true);
        try {
            const isCreateMode = mode === FormState.CREATE.key;
            const isEditMode = mode === FormState.EDIT.key;

            if (isCreateMode) {
                await service.create(values);
                notification.success({message: successMessages.create});
            } else if (isEditMode && selectedEntity) {
                await service.update(values, selectedEntity.id);
                notification.success({message: successMessages.update});
            }

            handleReload();
            handleClearForm();
        } catch (error) {
            console.error("Failed to save:", error);
            notification.error({message: "Failed to save item"});
        } finally {
            setIsLoading(false);
        }
    };

    const handleCancel = () => {
        handleClearForm();
        setIsLoading(false);
    };

    const showForm =
            mode === FormState.CREATE.key ||
            (selectedEntity && (mode === FormState.EDIT.key || mode === FormState.READ_ONLY.key));

    return <div className="flex-1 mt-10!">{showForm && children({
        entity: selectedEntity,
        mode,
        onSave: handleSave,
        onCancel: handleCancel
    })}</div>;
};