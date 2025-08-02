import { notification } from "antd";
import React, { useEffect } from "react";
import { FormState } from "../models/LayoutModel.ts";
import { useDoublePageContext } from "./DoublePageContext.tsx";
import { BaseService } from "../../../services/BaseService.ts";

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
                notification.error({message: "Failed to fetch item"});
            } finally {
                setIsLoading(false);
            }
        };
        if(selectedEntity) {
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
        handleChangeMode(FormState.READ_ONLY.key);
        setIsLoading(false);
        handleView(dataById);
    };

    const showForm =
            mode === FormState.CREATE.key ||
            (selectedEntity && (mode === FormState.EDIT.key || mode === FormState.READ_ONLY.key));

    return (
            <div className="flex-1 mt-10! overflow-auto! h-[calc(100vh-100px)]!">
                {showForm && children({
                    entity: dataById,
                    mode,
                    onSave: handleSave,
                    onCancel: handleCancel
                })}
            </div>
    );
};