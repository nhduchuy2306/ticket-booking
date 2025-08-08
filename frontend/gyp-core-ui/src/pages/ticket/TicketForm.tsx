import { Button, Form, notification, Select } from "antd";
import React, { useEffect, useState } from "react";
import { EventResponseDto } from "../../models/generated/event-service-models";
import { EventService } from "../../services/Event/EventService.ts";
import { TicketService } from "../../services/Ticket/TicketService.ts";

interface TicketFormProps {
}

const TicketForm: React.FC<TicketFormProps> = () => {
    const [events, setEvents] = useState<EventResponseDto[]>([]);
    const [form] = Form.useForm();

    useEffect(() => {
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
        void fetchEvents();
    }, []);

    const handleGenerateTicket = async () => {
        try {
            const eventId = form.getFieldValue("eventId");
            await TicketService.generateTicket(eventId);
        } catch (error) {
            notification.error({
                message: "Failed to generate ticket",
                description: error instanceof Error ? error.message : "Unknown error"
            });
        }
    }

    return (
            <Form
                    form={form}
                    layout="horizontal"
                    onFinish={handleGenerateTicket}
            >
                <Form.Item
                        name="eventId"
                        label="Event"
                        rules={[{required: true, message: "Please select an event"}]}
                >
                    <Select
                            className="!w-full !mr-1"
                            options={events.map(event => ({
                                label: event.name,
                                value: event.id
                            }))}
                    />
                </Form.Item>

                <Form.Item>
                    <Button
                            type="primary"
                            htmlType="submit"
                            className="float-right"
                    >Generate</Button>
                </Form.Item>
            </Form>
    );
}

export default TicketForm;