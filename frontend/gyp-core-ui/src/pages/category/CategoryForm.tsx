import { Button, Form, Input, Space } from "antd";
import React, { useEffect } from "react";
import MetaData from "../../components/metadata/MetaData.tsx";
import { FormState } from "../../models/enums/FormState.ts";
import { CategoryRequestDto, CategoryResponseDto } from "../../models/generated/event-service-models";

export interface CategoryFormProps {
    entity: CategoryResponseDto;
    mode: string;
    onSave: (values: CategoryRequestDto) => Promise<void>;
    onCancel: () => void;
}

const CategoryForm: React.FC<CategoryFormProps> = ({entity, mode, onSave, onCancel}) => {
    const [form] = Form.useForm();

    useEffect(() => {
        if (entity) {
            form.setFieldsValue(entity);
        } else {
            form.resetFields();
        }
    }, [entity, form]);

    const isReadOnly = mode === FormState.READ_ONLY.key;
    const isCreateMode = mode === FormState.CREATE.key;

    const handleSubmit = async (values: CategoryRequestDto) => {
        await onSave(values);
        form.resetFields();
    };

    const handleReset = () => {
        if (entity) {
            form.setFieldsValue(entity);
        } else {
            form.resetFields();
        }
    };

    return (
            <div className="p-6">
                <Form
                        form={form}
                        layout="vertical"
                        onFinish={handleSubmit}
                        disabled={isReadOnly}
                >
                    <Form.Item
                            name="name"
                            label="Category Name"
                            rules={[
                                {required: true, message: 'Please enter category name'},
                                {min: 2, message: 'Category name must be at least 2 characters'}
                            ]}
                    >
                        <Input placeholder="Enter category name"/>
                    </Form.Item>

                    <Form.Item
                            name="description"
                            label="Description"
                            rules={[{required: true, message: 'Please enter category description'}]}
                    >
                        <Input.TextArea rows={4} placeholder="Enter category description"/>
                    </Form.Item>

                    {isReadOnly &&
                        <MetaData
                            metadata={{
                                id: entity?.id,
                                createUser: entity?.createUser,
                                changeUser: entity?.changeUser,
                                createTimestamp: entity?.createTimestamp,
                                changeTimestamp: entity?.changeTimestamp
                            }}
                        />
                    }

                    {!isReadOnly && (
                            <Form.Item>
                                <Space className="float-right">
                                    <Button type="default" className="bg-red-500" onClick={handleReset}>
                                        Reset
                                    </Button>
                                    <Button type="default" onClick={onCancel}>
                                        Cancel
                                    </Button>
                                    <Button type="primary" htmlType="submit">
                                        {isCreateMode ? "Create" : "Update"}
                                    </Button>
                                </Space>
                            </Form.Item>
                    )}
                </Form>
            </div>
    );
};

export default CategoryForm;