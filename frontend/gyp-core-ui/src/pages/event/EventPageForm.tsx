import { Button, Form, Input } from "antd";
import React, { useEffect } from "react";
import { useParams } from "react-router-dom";
import { Mode } from "../../configs/Constants.ts";
import { EventModel } from "../../models/EventService/EventModel.ts";
import { EventService } from "../../services/Event/EventService.ts";

interface EventPageFormProps {
    mode?: string;
}

const EventPageForm: React.FC<EventPageFormProps> = ({mode}) => {
    const [form] = Form.useForm<EventModel>();
    const {id} = useParams();

    useEffect(() => {
        if (id && mode === Mode.EDIT.key) {
            void getEventById(id);
        } else {
            form.resetFields();
        }
    }, []);

    const getEventById = async (eventId: string) => {
        try {
            const event = await EventService.getEventById(eventId);
            if (event) {
                form.setFieldsValue(event);
            } else {
                console.error("No event found with the given ID");
            }
        } catch (error) {
            console.error("Error fetching event by ID:", error);
        }
    }

    return (
            <Form
                    form={form}
                    layout="vertical"
                    name="eventForm"
                    initialValues={{mode: mode || Mode.CREATE.key}}
                    onFinish={async (values) => {
                        try {
                            if (mode === Mode.CREATE.key) {
                                await EventService.createEvent(values);
                            } else if (mode === Mode.EDIT.key && id) {
                                await EventService.updateEvent(id, values);
                            }
                            form.resetFields();
                        } catch (error) {
                            console.error("Error saving event:", error);
                        }
                    }}
            >
                <Form.Item
                    name="name"
                    label="Event Name"
                    rules={[{ required: true, message: 'Please input the event name!' }]}
                >
                    <Input placeholder="Enter event name" />
                </Form.Item>

                <Form.Item
                    name="description"
                    label="Description"
                >
                    <Input.TextArea placeholder="Enter event description" />
                </Form.Item>

                <Form.Item
                    name="startTime"
                    label="Start Time"
                    rules={[{ required: true, message: 'Please select the start time!' }]}
                >
                    <Input type="datetime-local" />
                </Form.Item>

                <Form.Item
                    name="endTime"
                    label="End Time"
                    rules={[{ required: true, message: 'Please select the end time!' }]}
                >
                    <Input type="datetime-local" />
                </Form.Item>

                <Form.Item>
                    <Button type="primary" htmlType="submit">
                        {mode === Mode.CREATE.key ? 'Create Event' : 'Update Event'}
                    </Button>
                </Form.Item>
            </Form>
    )
}

export default EventPageForm;