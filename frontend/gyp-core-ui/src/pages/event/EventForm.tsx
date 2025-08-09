import {
    Button,
    Checkbox,
    DatePicker,
    Form,
    GetProp,
    Input,
    Modal,
    notification,
    Select,
    Upload,
    UploadFile,
    UploadProps
} from "antd";
import React, { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import SinglePageForm from "../../components/layout/singlepage/SinglePageForm.tsx";
import SinglePageLayout from "../../components/layout/singlepage/SinglePageLayout.tsx";
import MetaData from "../../components/metadata/MetaData.tsx";
import { createErrorNotification, createSuccessNotification } from "../../components/notification/Notification.ts";
import { Mode } from "../../configs/Constants.ts";
import {
    CategoryResponseDto,
    EventRequestDto,
    EventResponseDto,
    SeasonResponseDto,
    VenueMapResponseDto
} from "../../models/generated/event-service-models";
import { CategoryService } from "../../services/Event/CategoryService.ts";
import { EventService, EventServiceAdapter } from "../../services/Event/EventService.ts";
import { SeasonService } from "../../services/Event/SeasonService.ts";
import { VenueMapService } from "../../services/Event/VenueMapService.ts";
import { DateUtils } from "../../utils/DateUtils.ts";

type FileType = Parameters<GetProp<UploadProps, 'beforeUpload'>>[0];

const EventStatus = Object.freeze({
    DRAFT: Object.freeze({key: 'DRAFT', value: 'Draft'}),
    PENDING_APPROVAL: Object.freeze({key: 'PENDING_APPROVAL', value: 'Pending Approval'}),
    PUBLISHED: Object.freeze({key: 'PUBLISHED', value: 'Published'}),
    CANCELLED: Object.freeze({key: 'CANCELLED', value: 'Cancelled'}),
    COMPLETED: Object.freeze({key: 'COMPLETED', value: 'Completed'}),
    POSTPONED: Object.freeze({key: 'POSTPONED', value: 'Postponed'}),
});

interface EventFormProps {
    mode: string;
}

const EventForm: React.FC<EventFormProps> = ({mode}) => {
    const [data, setData] = useState<EventResponseDto | null>(null);
    const [categories, setCategories] = useState<CategoryResponseDto[]>([]);
    const [venueMaps, setVenueMaps] = useState<VenueMapResponseDto[]>([]);
    const [seasons, setSeasons] = useState<SeasonResponseDto[]>([]);
    const [isLoading, setIsLoading] = useState<boolean>(false);
    const [fileList, setFileList] = useState<UploadFile[]>([]);
    const [selectedFile, setSelectedFile] = useState<File | null>(null); // Store the actual file
    const {id} = useParams();
    const [form] = Form.useForm();
    const navigate = useNavigate();
    const [modal, modalContextHolder] = Modal.useModal();

    useEffect(() => {
        const fetchData = async () => {
            setIsLoading(true);
            try {
                // Fetch categories if in CREATE mode
                const categoryResponse = await CategoryService.getAllCategories();
                if (categoryResponse) {
                    setCategories(categoryResponse);
                } else {
                    setCategories([]);
                }

                // Fetch venue map
                const venueMapResponse = await VenueMapService.getAllVenueMaps();
                if (venueMapResponse) {
                    setVenueMaps(venueMapResponse);
                } else {
                    setVenueMaps([]);
                }

                // Fetch season
                const seasonResponse = await SeasonService.getAllSeasons();
                if (seasonResponse) {
                    setSeasons(seasonResponse);
                } else {
                    setSeasons([]);
                }

                if (id && (mode === Mode.EDIT.key || mode === Mode.READ_ONLY.key)) {
                    const response = await EventService.getEventById(id);
                    if (response) {
                        setData(response);
                        if (response.logoUrl) {
                            setFileList([{
                                uid: '-1',
                                name: 'Current Logo',
                                status: 'done',
                                url: response.logoUrl,
                            }]);
                        }
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
                categories: data.categories ? data.categories.map(category => category.id) : [],
                venueMapId: data.venueMap?.id,
                seasonId: data.season?.id,
                status: data.status
            });
        } else {
            form.resetFields();
        }
    }, [data, form, mode]);

    // Custom upload onChange handler that prevents automatic upload
    const onChange: UploadProps['onChange'] = ({fileList: newFileList}) => {
        setFileList(newFileList);

        // Store the actual file for later upload
        if (newFileList.length > 0 && newFileList[0].originFileObj) {
            setSelectedFile(newFileList[0].originFileObj as File);
        } else {
            setSelectedFile(null);
        }
    };

    // Prevent automatic upload
    const beforeUpload = (file: File) => {
        // Validate file type and size here if needed
        const isJpgOrPng = file.type === 'image/jpeg' || file.type === 'image/png';
        if (!isJpgOrPng) {
            notification.error({message: 'You can only upload JPG/PNG files!'});
            return false;
        }

        const isLt2M = file.size / 1024 / 1024 < 2;
        if (!isLt2M) {
            notification.error({message: 'Image must be smaller than 2MB!'});
            return false;
        }

        // Return false to prevent automatic upload
        return false;
    };

    const onPreview = async (file: UploadFile) => {
        let src = file.url as string;
        if (!src && file.originFileObj) {
            src = await new Promise((resolve) => {
                const reader = new FileReader();
                reader.readAsDataURL(file.originFileObj as FileType);
                reader.onload = () => resolve(reader.result as string);
            });
        }
        modal.info({
            title: 'Image Preview',
            content: (
                    <div style={{textAlign: 'center'}}>
                        <img src={src} alt="Preview" style={{maxWidth: '100%', maxHeight: '80vh'}}/>
                    </div>
            ),
            onOk() {
            },
            width: 600,
        });
    };

    // Custom submit handler that integrates with your API
    const handleFormSubmit = async (values: any) => {
        setIsLoading(true);
        try {
            const validatedValues = await form.validateFields();

            const adaptedValues: EventRequestDto = {
                ...validatedValues,
                startTime: values.startTime ? DateUtils.toIsoDateTime(values.startTime) : null,
                endTime: values.endTime ? DateUtils.toIsoDateTime(values.endTime) : null,
                doorOpenTime: values.doorOpenTime ? DateUtils.toIsoDateTime(values.doorOpenTime) : null,
                doorCloseTime: values.doorCloseTime ? DateUtils.toIsoDateTime(values.doorCloseTime) : null,
                categoryIds: validatedValues.categories || [],
            };

            if (mode === Mode.CREATE.key) {
                if (selectedFile) {
                    await EventService.createEventWithUpload(adaptedValues, selectedFile);
                } else {
                    await EventService.createEvent(adaptedValues);
                }
                createSuccessNotification("Event", "Event created successfully");
                handleBack();
            } else if (mode === Mode.EDIT.key) {
                if (selectedFile) {
                    await EventService.updateEventWithUpload(id!, adaptedValues, selectedFile);
                } else {
                    await EventService.updateEvent(id!, adaptedValues);
                }
                createSuccessNotification("Event", "Event updated successfully");
                handleBack();
            }
        } catch (error) {
            console.error("Error saving event:", error);
            createErrorNotification("Event", "Failed to save event. Please check the form and try again.");
        } finally {
            setIsLoading(false);
        }
    };

    const handleBack = () => {
        setSelectedFile(null);
        setFileList([]);
        navigate('/event');
    };

    const renderForm = (
            entity: EventResponseDto,
            currentMode: string,
            _onSave: (values: EventRequestDto) => Promise<void>,
            _handleBack?: () => void
    ) => {
        const isReadOnly = currentMode === Mode.READ_ONLY.key;

        return (
                <Form
                        form={form}
                        layout="vertical"
                        onFinish={handleFormSubmit}
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
                            name="status"
                            label="Status"
                    >
                        <Select
                                options={Object.values(EventStatus).map(status => ({
                                    label: status.value,
                                    value: status.key
                                }))}
                        />
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
                            name="venueMapId"
                            label="Venue Map"
                            rules={[{required: true, message: 'Please select Venue Map!'}]}
                    >
                        <Select
                                placeholder="Select Venue Map"
                                options={venueMaps.map(venueMap => ({
                                    label: venueMap.name,
                                    value: venueMap.id
                                }))}
                                disabled={isReadOnly}
                        />
                    </Form.Item>

                    <Form.Item
                            name="seasonId"
                            label="Season"
                            rules={[{required: true, message: 'Please select Season!'}]}
                    >
                        <Select
                                placeholder="Select Season"
                                options={seasons.map(season => ({
                                    label: season.name,
                                    value: season.id
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

                    <Form.Item
                            label="Logo"
                            name="logoUrl"
                    >
                        <Upload
                                listType="picture-card"
                                fileList={fileList}
                                onChange={onChange}
                                onPreview={onPreview}
                                beforeUpload={beforeUpload}
                                disabled={isReadOnly}
                                maxCount={1}
                        >
                            {fileList.length < 1 && !isReadOnly && '+ Upload'}
                        </Upload>
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
                {modalContextHolder}
            </div>
    );
}

export default EventForm;