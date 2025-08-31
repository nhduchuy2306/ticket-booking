import { Button, Form, Input, Select, Space, UploadFile } from "antd";
import React, { useEffect, useState } from "react";
import ImageUpload from "../../components/dataupload/ImageUpload.tsx";
import { useDoublePageContext } from "../../components/layout/doulepage/DoublePageContext.tsx";
import MetaData from "../../components/metadata/MetaData.tsx";
import { createErrorNotification, createSuccessNotification } from "../../components/notification/Notification.ts";
import { useFormLogic } from "../../hooks/form/useFormLogic.tsx";
import { Mode } from "../../models/enums/Mode.ts";
import {
    EventImageRequestDto,
    EventImageResponseDto,
    EventResponseDto
} from "../../models/generated/event-service-models";
import { EventImageService } from "../../services/Event/EventImageService.ts";
import { EventService } from "../../services/Event/EventService.ts";

interface EventImageFormProps {
    entity: EventImageResponseDto;
    mode: string;
    onSave: (values: EventImageRequestDto) => Promise<void>;
    onCancel: () => void;
}

const EventImageForm: React.FC<EventImageFormProps> = ({entity, mode, onSave, onCancel}) => {
    const {
        form,
        isReadOnly,
        isCreateMode,
        isEditMode,
        handleReset
    } = useFormLogic<EventImageRequestDto>({entity, mode, onSave});
    const [events, setEvents] = useState<EventResponseDto[]>([]);
    const [imageFileList, setImageFileList] = useState<UploadFile[]>([]);
    const [selectedImageFile, setSelectedImageFile] = useState<File | null>(null);
    const {handleReload, handleClearForm} = useDoublePageContext();

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

    useEffect(() => {
        if (entity && entity.imageUrl) {
            setImageFileList([{
                uid: '-1',
                name: entity.imageUrl || 'image.jpg',
                status: 'done',
                url: entity.imageUrl,
                thumbUrl: entity.imageBufferArray
            }]);
            setSelectedImageFile(null);
        } else {
            setImageFileList([]);
            setSelectedImageFile(null);
        }
    }, [entity]);

    const handleImageFileChange = (newFileList: UploadFile[], files: File[]) => {
        setImageFileList(newFileList);
        setSelectedImageFile(files.length > 0 ? files[0] : null);
    };

    const handleSubmit = async () => {
        try {
            const validatedValues = await form.validateFields();
            const adaptedValues: EventImageRequestDto = {
                ...validatedValues,
                imageUrl: undefined,
            }

            if (mode === Mode.CREATE.key) {
                if (selectedImageFile) {
                    await EventImageService.createEventImageWithUpload(adaptedValues, selectedImageFile);
                } else {
                    await EventImageService.createEventImage(adaptedValues);
                }
                createSuccessNotification("Event Image", "Event Image created successfully");
            } else if (mode === Mode.EDIT.key) {
                if (selectedImageFile) {
                    await EventImageService.updateEventImageWithUpload(entity.id!, adaptedValues, selectedImageFile);
                } else {
                    await EventImageService.updateEventImage(entity.id!, adaptedValues);
                }
                createSuccessNotification("Event Image", "Event Image updated successfully");
            }

            handleReload();
            handleClearForm();
        } catch (error) {
            console.error("Error saving event Image:", error);
            createErrorNotification("Event Image", "Failed to save Event Image. Please check the form and try again.");
        }
    }

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
                        label="Venue Id"
                    >
                        <Input disabled/>
                    </Form.Item>}

                    <Form.Item
                            name="name"
                            label="Name"
                            rules={[
                                {required: true, message: 'Please enter venue name'},
                                {min: 2, message: 'Venue name must be at least 2 characters'}
                            ]}
                    >
                        <Input placeholder="Enter venue name"/>
                    </Form.Item>

                    <Form.Item
                            name="eventId"
                            label="Event"
                            rules={[
                                {required: true, message: 'Please select a seat map'},
                            ]}
                    >
                        <Select
                                options={events.map(event => ({
                                    label: event.name,
                                    value: event.id
                                }))}
                        />
                    </Form.Item>

                    <Form.Item
                            name="imageUrl"
                            label="Image URL"
                    >
                        <ImageUpload
                                fileList={imageFileList}
                                onFileChange={handleImageFileChange}
                                disabled={isReadOnly}
                                acceptedTypes={['image/jpeg', 'image/png']}
                                multiple={false}
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

export default EventImageForm;