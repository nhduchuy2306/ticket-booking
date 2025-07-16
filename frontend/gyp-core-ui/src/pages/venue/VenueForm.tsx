import { Button, Form, Input, InputNumber, Space } from "antd";
import React, { useEffect } from "react";
import { FormState } from "../../components/layout/models/LayoutModel.ts";
import MetaData from "../../components/metadata/MetaData.tsx";
import { VenueRequestDto, VenueResponseDto } from "../../models/generated/event-service-models";

export interface VenueFormProps {
    entity: VenueResponseDto;
    mode: string;
    onSave: (values: VenueRequestDto) => Promise<void>;
    onCancel: () => void;
}

const VenueForm: React.FC<VenueFormProps> = ({entity, mode, onSave, onCancel}) => {
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

    const handleSubmit = async (values: VenueRequestDto) => {
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
                            label="Venue Name"
                            rules={[
                                {required: true, message: 'Please enter venue name'},
                                {min: 2, message: 'Venue name must be at least 2 characters'}
                            ]}
                    >
                        <Input placeholder="Enter venue name"/>
                    </Form.Item>

                    <Form.Item
                            name="address"
                            label="Address"
                            rules={[{required: true, message: 'Please enter address'}]}
                    >
                        <Input placeholder="Enter address"/>
                    </Form.Item>

                    <Form.Item
                            name="city"
                            label="City"
                            rules={[{required: true, message: 'Please enter city'}]}
                    >
                        <Input placeholder="Enter address"/>
                    </Form.Item>

                    <Form.Item
                            name="country"
                            label="Country"
                            rules={[{required: true, message: 'Please enter country'}]}
                    >
                        <Input placeholder="Enter address"/>
                    </Form.Item>

                    <Form.Item
                            name="capacity"
                            label="Capacity"
                            rules={[{required: true, message: 'Please enter capacity'}]}
                    >
                        <InputNumber placeholder="Enter capacity" min={1}/>
                    </Form.Item>

                    <Form.Item
                            name="latitude"
                            label="Latitude"
                            rules={[{required: true, message: 'Please enter latitude'}]}
                    >
                        <InputNumber type="number" placeholder="Enter latitude" step="any"/>
                    </Form.Item>

                    <Form.Item
                            name="longitude"
                            label="Longitude"
                            rules={[{required: true, message: 'Please enter longitude'}]}
                    >
                        <InputNumber type="number" placeholder="Enter longitude" step="any"/>
                    </Form.Item>

                    {isReadOnly &&
                        <MetaData
                            metadata={{
                                id: entity.id,
                                createUser: entity.createUser,
                                changeUser: entity.changeUser,
                                createTimestamp: entity.createTimestamp,
                                changeTimestamp: entity.changeTimestamp
                            }}
                        />
                    }

                    {(isCreateMode || isEditMode) &&
                        <Form.Item>
                            <Space className="float-right">
                                <Button type="primary" htmlType="submit" disabled={isReadOnly}>
                                    {isCreateMode ? "Create" : "Update"}
                                </Button>
                                <Button onClick={handleReset} disabled={isReadOnly}>
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

export default VenueForm;