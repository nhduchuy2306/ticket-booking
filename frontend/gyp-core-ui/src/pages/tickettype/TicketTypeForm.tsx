import { Button, DatePicker, Form, Input, InputNumber, Select, Space, Switch } from "antd";
import moment from "moment";
import React, { useEffect } from "react";
import { handleReset, handleSubmit } from "../../components/layout/LayoutUtils.ts";
import { FormState } from "../../components/layout/models/LayoutModel.ts";
import MetaData from "../../components/metadata/MetaData.tsx";
import { TicketTypeRequestDto, TicketTypeResponseDto } from "../../models/generated/event-service-models";

export interface TicketTypeFormProps {
    entity: TicketTypeResponseDto;
    mode: string;
    onSave: (values: TicketTypeRequestDto) => Promise<void>;
    onCancel: () => void;
}

const TicketTypeForm: React.FC<TicketTypeFormProps> = ({entity, mode, onSave, onCancel}) => {
    const [form] = Form.useForm();

    useEffect(() => {
        if (entity) {
            form.setFieldsValue({
                ...entity,
                saleStartDate: entity.saleStartDate ? moment(entity.saleStartDate) : null,
                saleEndDate: entity.saleEndDate ? moment(entity.saleEndDate) : null,
            });
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
                        onFinish={() => handleSubmit(entity, form, onSave)}
                        disabled={isReadOnly}
                >
                    <Form.Item
                            name="name"
                            label="Ticket Type Name"
                            rules={[
                                {required: true, message: 'Please enter ticket type name'},
                                {min: 2, message: 'Name must be at least 2 characters'}
                            ]}
                    >
                        <Input placeholder="Enter ticket type name"/>
                    </Form.Item>

                    <Form.Item
                            name="description"
                            label="Description"
                            rules={[{required: true, message: 'Please enter description'}]}
                    >
                        <Input.TextArea placeholder="Enter ticket description"/>
                    </Form.Item>

                    <Form.Item
                            name="price"
                            label="Price"
                            rules={[{required: true, message: 'Please enter price'}]}
                    >
                        <InputNumber
                                min={0}
                                step={0.01}
                                placeholder="Enter ticket price"
                                style={{width: '100%'}}
                        />
                    </Form.Item>

                    <Form.Item
                            name="quantityAvailable"
                            label="Quantity Available"
                            rules={[{required: true, message: 'Please enter quantity available'}]}
                    >
                        <InputNumber min={0} placeholder="Enter quantity available" style={{width: '100%'}}/>
                    </Form.Item>

                    <Form.Item
                            name="status"
                            label="Status"
                            rules={[{required: true, message: 'Please select status'}]}
                    >
                        <Select placeholder="Select status">
                            <Select.Option value="AVAILABLE">AVAILABLE</Select.Option>
                            <Select.Option value="SOLD_OUT">SOLD OUT</Select.Option>
                            <Select.Option value="INACTIVE">INACTIVE</Select.Option>
                        </Select>
                    </Form.Item>

                    <Form.Item
                            name="saleStartDate"
                            label="Sale Start Date"
                            rules={[{required: true, message: 'Please select start date'}]}
                    >
                        <DatePicker showTime style={{width: '100%'}}/>
                    </Form.Item>

                    <Form.Item
                            name="saleEndDate"
                            label="Sale End Date"
                            rules={[{required: true, message: 'Please select end date'}]}
                    >
                        <DatePicker showTime style={{width: '100%'}}/>
                    </Form.Item>

                    <Form.Item
                            name="saleActive"
                            label="Sale Active"
                            valuePropName="checked"
                    >
                        <Switch/>
                    </Form.Item>

                    <Form.Item
                            name="soldOut"
                            label="Sold Out"
                            valuePropName="checked"
                    >
                        <Switch/>
                    </Form.Item>

                    <Form.Item
                            name="soldTickets"
                            label="Sold Tickets"
                    >
                        <InputNumber disabled style={{width: '100%'}}/>
                    </Form.Item>

                    {isReadOnly && (
                            <MetaData
                                    metadata={{
                                        id: entity?.id,
                                        createUser: entity?.createUser,
                                        changeUser: entity?.changeUser,
                                        createTimestamp: entity?.createTimestamp,
                                        changeTimestamp: entity?.changeTimestamp
                                    }}
                            />
                    )}

                    {(isCreateMode || isEditMode) && (
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
                    )}
                </Form>
            </div>
    );
}

export default TicketTypeForm;