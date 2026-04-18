import React, { useEffect } from "react";
import { FormState } from "../../../models/enums/FormState.ts";
import { BaseService } from "../../../services/BaseService.ts";
import { createErrorNotification, createSuccessNotification } from "../../notification/Notification.ts";
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
        handleView,
        handleClearForm,
        handleChangeMode
    } = useDoublePageContext();

    const [dataById, setDataById] = React.useState(null);

    useEffect(() => {
        const getDataById = async (id: string) => {
            setIsLoading(true);
            try {
                const response = await service.getById(id);
                if (response) {
                    setDataById(response);
                } else {
                    handleClearForm();
                }
            } catch (error) {
                console.error("Failed to fetch data:", error);
                createErrorNotification("Error", "Failed to fetch item. Please try again later.");
            } finally {
                setIsLoading(false);
            }
        };
        if (selectedEntity) {
            void getDataById(selectedEntity?.id)
        } else {
            setDataById(null);
        }
    }, [selectedEntity]);

    const handleSave = async (values: any) => {
        setIsLoading(true);
        try {
            const isCreateMode = mode === FormState.CREATE.key;
            const isEditMode = mode === FormState.EDIT.key;

            delete values.id;
            if (isCreateMode) {
                await service.create(values);
                createSuccessNotification("Success", successMessages.create || "Item created successfully");
            } else if (isEditMode && selectedEntity) {
                await service.update(values, selectedEntity.id);
                createSuccessNotification("Success", successMessages.update || "Item updated successfully");
            }

            handleReload();
            handleClearForm();
        } catch (error) {
            console.error("Failed to save:", error);
            createErrorNotification("Error", "Failed to save item. Please try again later.");
        } finally {
            setIsLoading(false);
        }
    };

    const handleCancel = () => {
        handleChangeMode(FormState.READ_ONLY.key);
        setIsLoading(false);
        handleView(dataById);
    };

    const showForm =
            mode === FormState.CREATE.key ||
            (selectedEntity && (mode === FormState.EDIT.key || mode === FormState.READ_ONLY.key));

    return (
            <div className="flex-1 mt-10! overflow-auto! h-[calc(100vh-120px)]!">
                {showForm && children({
                    entity: dataById,
                    mode,
                    onSave: handleSave,
                    onCancel: handleCancel
                })}
            </div>
    );
};