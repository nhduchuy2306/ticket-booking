import { Button, Checkbox, DatePicker, Form, Input, notification, Select } from "antd";
import React, { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import SinglePageForm from "../../components/layout/singlepage/SinglePageForm.tsx";
import SinglePageLayout from "../../components/layout/singlepage/SinglePageLayout.tsx";
import MetaData from "../../components/metadata/MetaData.tsx";
import { Mode } from "../../configs/Constants.ts";
import { CategoryResponseDto, EventRequestDto, EventResponseDto } from "../../models/generated/event-service-models";
import { CategoryService } from "../../services/Event/CategoryService.ts";
import { EventService, EventServiceAdapter } from "../../services/Event/EventService.ts";
import { DateUtils } from "../../utils/DateUtils.ts";

interface EventFormProps {
    mode: string;
}

const EventForm: React.FC<EventFormProps> = ({mode}) => {
    const [data, setData] = useState<EventResponseDto | null>(null);
    const [categories, setCategories] = useState<CategoryResponseDto[]>([]);
    const [isLoading, setIsLoading] = useState<boolean>(false);
    const {id} = useParams();
    const [form] = Form.useForm();
    const navigate = useNavigate();

    useEffect(() => {
        const fetchData = async () => {
            setIsLoading(true);
            try {
                // Fetch categories if in CREATE mode
                const categoryResponse = await CategoryService.getAllCategories();
                if (categoryResponse) {
                    setCategories(categoryResponse);
                }

                if (id && (mode === Mode.EDIT.key || mode === Mode.READ_ONLY.key)) {
                    const response = await EventService.getEventById(id);
                    if (response) {
                        setData(response);
                    }
                } else {
                    setData(null);
                }
            } catch (error) {
                console.error("Error fetching data:", error);
                notification.error({message: "Failed to fetch data"});
            } finally {
                setIsLoading(false);
            }
        };
        void fetchData();
    }, [id, mode]);

    useEffect(() => {
        if (data && (mode === Mode.EDIT.key || mode === Mode.READ_ONLY.key)) {
            form.setFieldsValue({
                id: data.id,
                name: data.name,
                description: data.description,
                startTime: data.startTime ? DateUtils.toIsoDate(data.startTime) : undefined,
                endTime: data.endTime ? DateUtils.toIsoDate(data.endTime) : undefined,
                doorOpenTime: data.doorOpenTime ? DateUtils.toIsoDate(data.doorOpenTime) : undefined,
                doorCloseTime: data.doorCloseTime ? DateUtils.toIsoDate(data.doorCloseTime) : undefined,
                eventCompleted: data.eventCompleted,
                eventInProgress: data.eventInProgress,
                categories: data.categories ? data.categories.map(category => category.id) : []
            });
        } else {
            form.resetFields();
        }
    }, [data, form, mode]);

    const renderForm = (
            entity: EventResponseDto,
            currentMode: string,
            onSave: (values: EventRequestDto) => Promise<void>,
            handleBack?: () => void
    ) => {
        const isReadOnly = currentMode === Mode.READ_ONLY.key;

        const handleSave = async (values: EventRequestDto) => {
            const validatedValues = await form.validateFields();
            const adaptedValues = {
                ...validatedValues,
                startTime: values.startTime ? DateUtils.toIsoDateTime(values.startTime) : null,
                endTime: values.endTime ? DateUtils.toIsoDateTime(values.endTime) : null,
                doorOpenTime: values.doorOpenTime ? DateUtils.toIsoDateTime(values.doorOpenTime) : null,
                doorCloseTime: values.doorCloseTime ? DateUtils.toIsoDateTime(values.doorCloseTime) : null,
            };
            await onSave(adaptedValues);
        };

        return (
                <Form
                        form={form}
                        layout="vertical"
                        onFinish={handleSave}
                        disabled={isReadOnly}
                >
                    <Form.Item
                            name="name"
                            label="Event Name"
                            rules={[{required: true, message: 'Please input the event name!'}]}
                    >
                        <Input placeholder="Enter event name"/>
                    </Form.Item>

                    <Form.Item
                            name="description"
                            label="Description"
                    >
                        <Input.TextArea placeholder="Enter event description"/>
                    </Form.Item>

                    <Form.Item
                            name="categories"
                            label="Categories"
                            rules={[{required: true, message: 'Please select at least one category!'}]}
                    >
                        <Select
                                mode="multiple"
                                placeholder="Select categories"
                                options={categories.map(category => ({
                                    label: category.name,
                                    value: category.id
                                }))}
                                disabled={isReadOnly}
                        />
                    </Form.Item>

                    <Form.Item
                            name="startTime"
                            label="Start Time"
                            rules={[{required: true, message: 'Please select the start time!'}]}
                    >
                        <DatePicker showTime/>
                    </Form.Item>

                    <Form.Item
                            name="endTime"
                            label="End Time"
                            rules={[{required: true, message: 'Please select the end time!'}]}
                    >
                        <DatePicker showTime/>
                    </Form.Item>

                    <Form.Item
                            name="doorOpenTime"
                            label="Door Open Time"
                            rules={[{required: true, message: 'Please select the door open time!'}]}
                    >
                        <DatePicker showTime/>
                    </Form.Item>

                    <Form.Item
                            name="doorCloseTime"
                            label="Door Close Time"
                            rules={[{required: true, message: 'Please select the door close time!'}]}
                    >
                        <DatePicker showTime/>
                    </Form.Item>

                    <Form.Item
                            name="eventCompleted"
                            valuePropName="checked"
                    >
                        <Checkbox disabled={isReadOnly}>Event Completed</Checkbox>
                    </Form.Item>

                    <Form.Item
                            name="eventInProgress"
                            valuePropName="checked"
                    >
                        <Checkbox disabled={isReadOnly}>Event In Progress</Checkbox>
                    </Form.Item>

                    {isReadOnly &&
                        <MetaData
                            metadata={{
                                id: id,
                                createUser: entity?.createUser,
                                changeUser: entity?.changeUser,
                                createTimestamp: entity?.createTimestamp,
                                changeTimestamp: entity?.changeTimestamp
                            }}
                        />
                    }
                    {!isReadOnly &&
                        <div className="flex justify-end items-center gap-1.5">
                            <Button type="default" onClick={handleBack}>Cancel</Button>
                            <Button
                                type="primary"
                                htmlType="submit"
                                loading={isLoading}>
                                {currentMode === Mode.CREATE.key ? 'Create' : 'Update'}
                            </Button>
                        </div>
                    }
                </Form>
        )
    }

    return (
            <div className="bg-white">
                <SinglePageLayout onNavigate={(path: string, entity?: EventResponseDto) =>
                        EventService.navigate(navigate, path, entity)}
                >
                    <SinglePageForm
                            service={EventServiceAdapter}
                            renderForm={renderForm}
                            entity={data}
                            mode={mode}
                            successMessages={{
                                create: "Event created successfully",
                                update: "Event updated successfully"
                            }}
                            showBackButton={true}
                    />
                </SinglePageLayout>
            </div>
    );
}

export default EventForm;