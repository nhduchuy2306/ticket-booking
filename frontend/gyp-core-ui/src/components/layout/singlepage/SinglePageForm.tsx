import { Button, Flex, notification, Tooltip } from "antd";
import React from "react";
import { BiArrowBack } from "react-icons/bi";
import { BaseService, FormState } from "../models/LayoutModel.ts";
import { useSinglePageContext } from "./SinglePageContext.tsx";

export interface SinglePageFormProps {
    service: BaseService<any, any>;
    renderForm: (
            entity: any,
            mode: string,
            onSave: (values: any) => Promise<void>,
            handleBack?: () => void
    ) => React.ReactNode;
    entity?: any;
    mode: string;
    successMessages?: {
        create?: string;
        update?: string;
    };
    showBackButton?: boolean;
}

const SinglePageForm: React.FC<SinglePageFormProps> = ({
                                                           service,
                                                           renderForm,
                                                           entity = null,
                                                           mode,
                                                           successMessages = {
                                                               create: "Item created successfully",
                                                               update: "Item updated successfully"
                                                           },
                                                           showBackButton = true
                                                       }) => {
    const {
        setIsLoading,
        handleBack
    } = useSinglePageContext();

    const handleSave = async (values: any) => {
        setIsLoading(true);
        try {
            const isCreateMode = mode === FormState.CREATE.key;
            const isEditMode = mode === FormState.EDIT.key;

            if (isCreateMode) {
                await service.create(values);
                notification.success({message: successMessages.create});
            } else if (isEditMode && entity) {
                await service.update(values, entity.id);
                notification.success({message: successMessages.update});
            }

            // Navigate back to table after successful save
            handleBack();
        } catch (error) {
            console.error('Failed to save:', error);
            notification.error({message: "Failed to save item"});
        } finally {
            setIsLoading(false);
        }
    };

    return (
            <div>
                <Flex justify="space-between" align="center" className="p-4!">
                    {showBackButton && (
                            <Tooltip title="Back to List">
                                <Button
                                        type="default"
                                        icon={<BiArrowBack/>}
                                        onClick={handleBack}
                                />
                            </Tooltip>
                    )}
                </Flex>
                <div className="p-4! overflow-auto! h-[calc(100vh-150px)]!">
                    {renderForm(entity, mode, handleSave, handleBack)}
                </div>
            </div>
    );
};

export default SinglePageForm;