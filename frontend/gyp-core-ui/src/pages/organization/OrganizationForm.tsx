import { Button, Form, Input, Space } from "antd";
import React, { useEffect } from "react";
import { handleReset, handleSubmit } from "../../components/layout/LayoutUtils.ts";
import MetaData from "../../components/metadata/MetaData.tsx";
import { FormState } from "../../models/enums/FormState.ts";
import { OrganizationRequestDto, OrganizationResponseDto } from "../../models/generated/auth-service-models";

export interface OrganizationFormProps {
    entity: OrganizationResponseDto;
    mode: string;
    onSave: (values: OrganizationRequestDto) => Promise<void>;
    onCancel: () => void;
}

const OrganizationForm: React.FC<OrganizationFormProps> = ({entity, mode, onSave, onCancel}) => {
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
    const isEditMode = mode === FormState.EDIT.key;

    return (
            <div className="p-6">
                <Form
                        form={form}
                        layout="vertical"
                        onFinish={(values: OrganizationRequestDto) => handleSubmit(values, form, onSave)}
                        disabled={isReadOnly}
                >
                    {!isCreateMode &&
                        <Form.Item
                            name="id"
                            label="Id"
                            rules={[
                                {required: true, message: 'Please enter venue name'},
                                {min: 2, message: 'Venue name must be at least 2 characters'}
                            ]}
                        >
                            <Input disabled={true}/>
                        </Form.Item>
                    }

                    <Form.Item
                            name="name"
                            label="Name"
                            rules={[
                                {required: true, message: 'Please enter name'},
                                {min: 2, message: 'Name must be at least 2 characters'}
                            ]}
                    >
                        <Input placeholder="Enter name"/>
                    </Form.Item>

                    <Form.Item
                            name="code"
                            label="Code"
                            rules={[
                                {required: true, message: 'Please enter code'},
                                {min: 2, message: 'Code must be at least 2 characters'}
                            ]}
                    >
                        <Input placeholder="Enter code"/>
                    </Form.Item>

                    <Form.Item
                            name="description"
                            label="Description"
                    >
                        <Input placeholder="Enter description"/>
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

                    {(isCreateMode || isEditMode) &&
                        <Form.Item>
                            <Space className="float-right">
                                <Button type="primary" htmlType="submit" disabled={isReadOnly}>
                                    {isCreateMode ? "Create" : "Update"}
                                </Button>
                                <Button onClick={() => handleReset(entity, form)} disabled={isReadOnly}>
                                    Reset
                                </Button>
                                <Button onClick={onCancel} disabled={isReadOnly}>
                                    Cancel
                                </Button>
                            </Space>
                        </Form.Item>
                    }
                </Form>
            </div>
    );
}

export default OrganizationForm;