import { Button, Flex, Form, Select } from "antd";
import React, { useEffect, useState } from "react";
import { createErrorNotification, createSuccessNotification } from "../../components/notification/Notification.ts";
import { EventResponseDto } from "../../models/generated/event-service-models";
import { EventService } from "../../services/Event/EventService.ts";
import { SeatMapService } from "../../services/Event/SeatMapService.ts";
import { TicketService } from "../../services/Ticket/TicketService.ts";

interface TicketFormProps {
    onShowTicket?: (eventId: string) => void;
}

const TicketForm: React.FC<TicketFormProps> = ({onShowTicket}) => {
    const [events, setEvents] = useState<EventResponseDto[]>([]);
    const [isGenerated, setIsGenerated] = useState<boolean>(false);
    const [isOnSale, setIsOnSale] = useState<boolean>(false);
    const [selectedEvent, setSelectedEvent] = useState<EventResponseDto | null>(null);
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
                createErrorNotification("Failed to fetch events", "Failed to fetch events");
            }
        }
        void fetchEvents();
    }, []);

    const handleEventSelect = (value: string) => {
        const event = events.find(event => event.id === value);
        setIsGenerated(event?.isGenerated || false);
        setSelectedEvent(event || null);
        setIsOnSale(event?.status === "ON_SALE");
    }

    const handleShowTicket = async () => {
        const eventId = selectedEvent?.id;
        if (!eventId) {
            createErrorNotification("No event selected", "Please select an event to show the ticket.");
            return;
        }
        if (onShowTicket) {
            onShowTicket(eventId);
        }
    }

    const handleGenerateTicket = async () => {
        try {
            const eventId = form.getFieldValue("eventId");
            if (!eventId) {
                createErrorNotification("No event selected", "Please select an event to generate the ticket.");
                return;
            }
            await SeatMapService.generateSeatMapTicket(eventId);
            setIsGenerated(true);
            createSuccessNotification(
                    "Ticket generated successfully",
                    `Ticket for event ID ${eventId} has been generated.`
            );
        } catch (error) {
            createErrorNotification(
                    "Failed to generate ticket",
                    error instanceof Error ? error.message : "Unknown error"
            );
        }
    }

    const handleSaleTicket = async () => {
        await TicketService.startSaleTicket(selectedEvent?.id);
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
                                value: event.id,
                            }))}
                            onSelect={handleEventSelect}
                    />
                </Form.Item>
                <Form.Item>
                    <Flex gap={5} justify="flex-end">
                        {isGenerated && <Button type="default" onClick={handleShowTicket}>Show</Button>}
                        {isGenerated && !isOnSale &&
                            <Button type="primary" onClick={handleSaleTicket}>Sale Ticket</Button>}
                        {!isGenerated && <Button type="primary" htmlType="submit">Generate</Button>}
                    </Flex>
                </Form.Item>
            </Form>
    );
}

export default TicketForm;