import { Button, Form, Input, Space } from "antd";
import React, { useEffect } from "react";
import { handleReset, handleSubmit } from "../../components/layout/LayoutUtils.ts";
import MetaData from "../../components/metadata/MetaData.tsx";
import { FormState } from "../../models/enums/FormState.ts";
import { OrganizationRequestDto, OrganizationResponseDto } from "../../models/generated/auth-service-models";

export interface OrganizationFormProps {
    entity?: OrganizationResponseDto | null;
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
                        >
                            <Input disabled={true}/>
                        </Form.Item>
                    }

                    <Form.Item
                            name="orgName"
                            label="Organization Name"
                            rules={[
                                {required: true, message: 'Please enter organization name'},
                                {min: 2, message: 'Organization name must be at least 2 characters'}
                            ]}
                    >
                        <Input placeholder="Enter organization name"/>
                    </Form.Item>

                    <Form.Item
                            name="orgSlug"
                            label="Organization Slug"
                            rules={[
                                {required: true, message: 'Please enter organization slug'},
                                {pattern: /^[a-z0-9-]+$/, message: 'Slug must contain only lowercase letters, numbers, and hyphens'}
                            ]}
                    >
                        <Input placeholder="org-slug"/>
                    </Form.Item>

                    <Form.Item
                            name="businessEmail"
                            label="Business Email"
                            rules={[{required: true, type: 'email', message: 'Please enter a valid business email'}]}
                    >
                        <Input placeholder="business@example.com"/>
                    </Form.Item>

                    <Form.Item
                            name="phone"
                            label="Phone"
                            rules={[{required: true, message: 'Please enter a phone number'}]}
                    >
                        <Input placeholder="Phone number"/>
                    </Form.Item>

                    <Form.Item
                            name="address"
                            label="Address"
                            rules={[{required: true, message: 'Please enter an address'}]}
                    >
                        <Input placeholder="Address"/>
                    </Form.Item>

                    <Form.Item
                            name="taxCode"
                            label="Tax Code"
                            rules={[{required: true, message: 'Please enter tax code'}]}
                    >
                        <Input placeholder="Tax code"/>
                    </Form.Item>

                    <Form.Item
                            name="representativeName"
                            label="Representative Name"
                            rules={[{required: true, message: 'Please enter representative name'}]}
                    >
                        <Input placeholder="Representative name"/>
                    </Form.Item>

                    <Form.Item
                            name="representativePhone"
                            label="Representative Phone"
                    >
                        <Input placeholder="Representative phone"/>
                    </Form.Item>

                    {isCreateMode && <>
                        <Form.Item
                                name="ownerFullName"
                                label="Owner Full Name"
                                rules={[{required: true, message: 'Please enter owner full name'}]}
                        >
                            <Input placeholder="Owner full name"/>
                        </Form.Item>

                        <Form.Item
                                name="ownerEmail"
                                label="Owner Email"
                                rules={[{required: true, type: 'email', message: 'Please enter a valid owner email'}]}
                        >
                            <Input placeholder="owner@example.com"/>
                        </Form.Item>

                        <Form.Item
                                name="ownerPassword"
                                label="Owner Password"
                                rules={[{required: true, message: 'Please enter owner password'}]}
                        >
                            <Input.Password placeholder="Owner password"/>
                        </Form.Item>

                        <Form.Item
                                name="confirmPassword"
                                label="Confirm Password"
                                dependencies={["ownerPassword"]}
                                rules={[
                                    {required: true, message: 'Please confirm password'},
                                    ({getFieldValue}) => ({
                                        validator(_, value) {
                                            if(!value || getFieldValue('ownerPassword') === value) {
                                                return Promise.resolve();
                                            }
                                            return Promise.reject(new Error('Passwords do not match'));
                                        },
                                    }),
                                ]}
                        >
                            <Input.Password placeholder="Confirm password"/>
                        </Form.Item>
                    </>}

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