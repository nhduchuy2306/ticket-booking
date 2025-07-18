import { Button, Form, Input, Space } from "antd";
import React, { useEffect } from "react";
import { handleReset, handleSubmit } from "../../components/layout/LayoutUtils.ts";
import { FormState } from "../../components/layout/models/LayoutModel.ts";
import { SeasonRequestDto, SeasonResponseDto } from "../../models/generated/event-service-models";

export interface SeasonFormProps {
    entity: SeasonResponseDto;
    mode: string;
    onSave: (values: SeasonRequestDto) => Promise<void>;
    onCancel: () => void;
}

const SeasonForm: React.FC<SeasonFormProps> = ({entity, mode, onSave, onCancel}) => {
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

    return (
            <div className="p-6">
                <Form
                        form={form}
                        layout="vertical"
                        onFinish={(values: SeasonRequestDto) => handleSubmit(values, form, onSave)}
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

                    {!isReadOnly && (
                            <Form.Item>
                                <Space className="float-right">
                                    <Button type="default" className="bg-red-500"
                                            onClick={() => handleReset(entity, form)}>
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
}

export default SeasonForm;