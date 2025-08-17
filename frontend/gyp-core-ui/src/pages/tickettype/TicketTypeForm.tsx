import { Button, DatePicker, Form, Input, InputNumber, notification, Select, Space, Switch } from "antd";
import moment from "moment";
import React, { useEffect, useState } from "react";
import MetaData from "../../components/metadata/MetaData.tsx";
import { FormState } from "../../models/enums/FormState.ts";
import {
    EventResponseDto,
    TicketTypeRequestDto,
    TicketTypeResponseDto
} from "../../models/generated/event-service-models";
import { EventService } from "../../services/Event/EventService.ts";

export interface TicketTypeFormProps {
    entity: TicketTypeResponseDto;
    mode: string;
    onSave: (values: TicketTypeRequestDto) => Promise<void>;
    onCancel: () => void;
}

const TicketTypeForm: React.FC<TicketTypeFormProps> = ({entity, mode, onSave, onCancel}) => {
    const [form] = Form.useForm();
    const [events, setEvents] = useState<EventResponseDto[]>([]);

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
        void fetchEvents();
    }, [entity, form]);

    const fetchEvents = async () => {
        try {
            const events = await EventService.getActiveEvents();
            if (events) {
                setEvents(events);
            } else {
                setEvents([]);
            }
        } catch (error) {
            notification.error({message: "Failed to fetch events"});
        }
    }

    const isReadOnly = mode === FormState.READ_ONLY.key;
    const isCreateMode = mode === FormState.CREATE.key;
    const isEditMode = mode === FormState.EDIT.key;

    const handleSubmit = async (values: TicketTypeRequestDto) => {
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
                            name="eventId"
                            label="Event"
                            rules={[{required: true, message: 'Please choose an event'}]}
                    >
                        <Select
                                options={events.map(event => ({
                                    label: event.name,
                                    value: event.id
                                }))}
                        />
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
                                    <Button onClick={handleReset} disabled={isReadOnly}>
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