import { Button, Form, Input, Select, Space } from "antd";
import TextArea from "antd/es/input/TextArea";
import React, { useEffect, useState } from "react";
import MetaData from "../../components/metadata/MetaData.tsx";
import { createErrorNotification } from "../../components/notification/Notification.ts";
import { useFormLogic } from "../../hooks/form/useFormLogic.tsx";
import { EventResponseDto } from "../../models/generated/event-service-models";
import { SaleChannelRequestDto, SaleChannelResponseDto } from "../../models/generated/sale-channel-service-models";
import { EventService } from "../../services/Event/EventService.ts";

interface SaleChannelFormProps {
    entity: SaleChannelResponseDto;
    mode: string;
    onSave: (values: SaleChannelRequestDto) => Promise<void>;
    onCancel: () => void;
}

const SaleChannelForm: React.FC<SaleChannelFormProps> = ({entity, mode, onSave, onCancel}) => {
    const {
        form,
        isReadOnly,
        isCreateMode,
        isEditMode,
        handleSubmit,
        handleReset
    } = useFormLogic<SaleChannelRequestDto>({entity, mode, onSave});
    const [events, setEvents] = useState<EventResponseDto[]>([]);

    useEffect(() => {
        const fetchData = async () => {
            try {
                // Fetch Event data
                const eventsData = await EventService.getActiveEvents();
                if (eventsData) {
                    setEvents(eventsData);
                } else {
                    setEvents([]);
                }
            } catch (error) {
                console.error("Failed to fetch seat maps:", error);
                createErrorNotification("Error fetching events");
            }
        }
        void fetchData();
    }, []);

    return (
            <div className="p-6">
                <Form
                        form={form}
                        layout="vertical"
                        onFinish={handleSubmit}
                        disabled={isReadOnly}
                >
                    {isReadOnly && <Form.Item
                        name="id"
                        label="Id"
                    >
                        <Input disabled/>
                    </Form.Item>}

                    <Form.Item
                            name="name"
                            label="Sale Channel Name"
                            rules={[
                                {required: true, message: 'Please enter sale channel name'},
                            ]}
                    >
                        <Input placeholder="Enter sale channel name"/>
                    </Form.Item>

                    <Form.Item
                            name="description"
                            label="Description"
                    >
                        <TextArea rows={4} placeholder="Enter description" disabled={isReadOnly}/>
                    </Form.Item>

                    {!isCreateMode && <Form.Item
                        name="status"
                        label="Status"
                    >
                        <Select
                            options={[
                                {label: 'Active', value: 'ACTIVE'},
                                {label: 'Inactive', value: 'INACTIVE'}
                            ]}
                            placeholder="Select status"
                            allowClear
                            disabled={isReadOnly}
                        />
                    </Form.Item>}

                    <Form.Item
                            name="type"
                            label="Type"
                    >
                        <Select
                                options={[
                                    {label: 'Box Office', value: 'BOX_OFFICE'},
                                    {label: 'Ticket Shop', value: 'TICKET_SHOP'},
                                    {label: 'API Partner', value: 'API_PARTNER'},
                                    {label: 'Mobile App', value: 'MOBILE_APP'}
                                ]}
                                placeholder="Select status"
                                allowClear
                                disabled={isReadOnly}
                        />
                    </Form.Item>

                    <Form.Item
                            name="eventId"
                            label="Event"
                            rules={[
                                {required: true, message: 'Please Select an event'},
                            ]}
                    >
                        <Select
                                options={events.map(event => ({
                                    label: event.name,
                                    value: event.id
                                }))}
                                placeholder="Select an event"
                                allowClear
                        />
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

export default SaleChannelForm;