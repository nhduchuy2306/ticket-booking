import { Button, Flex, notification } from "antd";
import React from "react";
import { BiArrowBack } from "react-icons/bi";
import { BaseService, FormState } from "../models/LayoutModel.ts";
import { useSinglePageContext } from "./SinglePageContext.tsx";
import SinglePageLayout from "./SinglePageLayout.tsx";
import { SinglePageTable, SinglePageTableProps } from "./SinglePageTable.tsx";

export interface SinglePageFormProps {
    service: BaseService<any, any>;
    renderForm: (entity: any, mode: string, onSave: (values: any) => Promise<void>) => React.ReactNode;
    entity?: any;
    mode: string;
    successMessages?: {
        create?: string;
        update?: string;
    };
    title?: string;
    showBackButton?: boolean;
}

export const SinglePageForm: React.FC<SinglePageFormProps> = ({
                                                                  service,
                                                                  renderForm,
                                                                  entity = null,
                                                                  mode,
                                                                  successMessages = {
                                                                      create: "Item created successfully",
                                                                      update: "Item updated successfully"
                                                                  },
                                                                  title,
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
                await service.update(values, (entity as any).id);
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

    const getTitle = () => {
        if (title) return title;

        switch (mode) {
            case FormState.CREATE.key:
                return "Create New Item";
            case FormState.EDIT.key:
                return "Edit Item";
            case FormState.READ_ONLY.key:
                return "View Item";
            default:
                return "Form";
        }
    };

    return (
            <div className="p-6">
                <Flex justify="space-between" align="center" className="mb-6">
                    <h1 className="text-2xl font-bold">{getTitle()}</h1>
                    {showBackButton && (
                            <Button
                                    type="default"
                                    icon={<BiArrowBack/>}
                                    onClick={handleBack}
                            >
                                Back to List
                            </Button>
                    )}
                </Flex>

                <div className="max-w-4xl">
                    {renderForm(entity, mode, handleSave)}
                </div>
            </div>
    );
};

// Usage example with React Router
export const createSinglePageRoutes = (
        tableProps: SinglePageTableProps,
        formProps: Omit<SinglePageFormProps, 'entity' | 'mode'>
) => {
    // This would be used with your routing library (React Router, Next.js, etc.)
    return {
        // Table route component
        TablePage: () => (
                <SinglePageLayout onNavigate={(path, entity) => {
                    // Handle navigation logic here
                    // For React Router: navigate(path, { state: { entity } })
                    // For Next.js: router.push({ pathname: path, query: { entity: JSON.stringify(entity) } })
                    console.log('Navigate to:', path, entity);
                }}>
                    <SinglePageTable {...tableProps} />
                </SinglePageLayout>
        ),

        // Create form route component
        CreatePage: () => (
                <SinglePageLayout>
                    <SinglePageForm
                            {...formProps}
                            entity={null}
                            mode={FormState.CREATE.key}
                    />
                </SinglePageLayout>
        ),

        // Edit form route component (entity would come from route params/state)
        EditPage: ({entity}: { entity: object }) => (
                <SinglePageLayout>
                    <SinglePageForm
                            {...formProps}
                            entity={entity}
                            mode={FormState.EDIT.key}
                    />
                </SinglePageLayout>
        ),

        // View form route component (entity would come from route params/state)
        ViewPage: ({entity}: { entity: object }) => (
                <SinglePageLayout>
                    <SinglePageForm
                            {...formProps}
                            entity={entity}
                            mode={FormState.READ_ONLY.key}
                    />
                </SinglePageLayout>
        )
    };
};