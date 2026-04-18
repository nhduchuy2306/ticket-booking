import { Button, Checkbox, DatePicker, Form, Input, Select, Spin, UploadFile } from "antd";
import React, { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import DataTransfer from "../../components/data-transfer/DataTransfer.tsx";
import { DataItemModel } from "../../components/data-transfer/DataTransferModel.ts";
import ImageUpload from "../../components/dataupload/ImageUpload.tsx";
import SinglePageForm from "../../components/layout/singlepage/SinglePageForm.tsx";
import SinglePageLayout from "../../components/layout/singlepage/SinglePageLayout.tsx";
import MetaData from "../../components/metadata/MetaData.tsx";
import { createErrorNotification, createSuccessNotification } from "../../components/notification/Notification.ts";
import { EventStatus } from "../../models/enums/EventStatus.ts";
import { Mode } from "../../models/enums/Mode.ts";
import {
    CategoryResponseDto,
    EventRequestDto,
    EventResponseDto,
    SeasonResponseDto,
    TicketTypeResponseDto,
    VenueMapResponseDto
} from "../../models/generated/event-service-models";
import { SaleChannelResponseDto } from "../../models/generated/sale-channel-service-models";
import { CategoryService } from "../../services/Event/CategoryService.ts";
import { EventService, EventServiceAdapter } from "../../services/Event/EventService.ts";
import { SeasonService } from "../../services/Event/SeasonService.ts";
import { TicketTypeService } from "../../services/Event/TicketTypeService.ts";
import { VenueMapService } from "../../services/Event/VenueMapService.ts";
import { SaleChannelService } from "../../services/SaleChannel/SaleChannelService.ts";
import { DateUtils } from "../../utils/DateUtils.ts";

interface EventFormProps {
    mode: string;
    showBackButton?: boolean;
}

const EventForm: React.FC<EventFormProps> = ({mode, showBackButton = true}) => {
    const [data, setData] = useState<EventResponseDto | null>(null);
    const [categories, setCategories] = useState<CategoryResponseDto[]>([]);
    const [venueMaps, setVenueMaps] = useState<VenueMapResponseDto[]>([]);
    const [seasons, setSeasons] = useState<SeasonResponseDto[]>([]);
    const [isLoading, setIsLoading] = useState<boolean>(false);
    const [logoFileList, setLogoFileList] = useState<UploadFile[]>([]);
    const [selectedLogoFile, setSelectedLogoFile] = useState<File | null>(null);
    const [saleChannelsByEvent, setSaleChannelsByEvent] = useState<SaleChannelResponseDto[]>([]);
    const [saleChannels, setSaleChannels] = useState<SaleChannelResponseDto[]>([]);
    const [ticketTypes, setTicketTypes] = useState<TicketTypeResponseDto[]>([]);
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

                const saleChannelResponse = await SaleChannelService.getAllSaleChannels();
                if (saleChannelResponse) {
                    setSaleChannels(saleChannelResponse);
                } else {
                    setSaleChannels([]);
                }

                const ticketTypeResponse = await TicketTypeService.getAllTicketTypes();
                if (ticketTypeResponse) {
                    setTicketTypes(ticketTypeResponse);
                } else {
                    setTicketTypes([]);
                }

                const saleChannelsByEventResponse = id ? await SaleChannelService.getSaleChannelsByEventId(id) : [];
                if (saleChannelsByEventResponse) {
                    setSaleChannelsByEvent(saleChannelsByEventResponse);
                } else {
                    setSaleChannelsByEvent([]);
                }

                if (id && (mode === Mode.EDIT.key || mode === Mode.READ_ONLY.key)) {
                    const eventResponse = await EventService.getEventById(id);
                    if (eventResponse) {
                        setData(eventResponse);
                        if (eventResponse.logoUrl) {
                            setLogoFileList([{
                                uid: '-1',
                                name: eventResponse.logoUrl || 'current_logo',
                                status: 'done',
                                url: eventResponse.logoUrl,
                                thumbUrl: eventResponse.logoBufferArray,
                            }]);
                        }
                    }
                } else {
                    setData(null);
                }
            } catch (error) {
                console.error("Error fetching data:", error);
                createErrorNotification("Error", "Failed to fetch event data. Please try again later.");
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
                ticketTypes: data.ticketTypes ? data.ticketTypes.map(ticketType => ticketType.id) : [],
                venueMapId: data.venueMap?.id,
                seasonId: data.season?.id,
                status: data.status,
                saleChannelIds: saleChannelsByEvent?.map(item => item.id) || [],
            });
        } else {
            form.resetFields();
        }
    }, [data, form, mode, saleChannelsByEvent]);

    const buildSaleChannelItem = (saleChannelList: SaleChannelResponseDto[]): DataItemModel[] => {
        return saleChannelList.map((saleChannelItem) => ({
            key: saleChannelItem.id,
            title: saleChannelItem.name,
            description: saleChannelItem.description || "",
        }));
    };

    // Handle logo file changes
    const handleLogoFileChange = (newFileList: UploadFile[], files: File[]) => {
        setLogoFileList(newFileList);
        setSelectedLogoFile(files.length > 0 ? files[0] : null);
    };

    // Custom submit handler that integrates with your API
    const handleFormSubmit = async (values: EventRequestDto) => {
        setIsLoading(true);
        try {
            const validatedValues = await form.validateFields();

            const adaptedValues = {
                ...validatedValues,
                startTime: values.startTime ? DateUtils.toIsoDateTime(values.startTime) : null,
                endTime: values.endTime ? DateUtils.toIsoDateTime(values.endTime) : null,
                doorOpenTime: values.doorOpenTime ? DateUtils.toIsoDateTime(values.doorOpenTime) : null,
                doorCloseTime: values.doorCloseTime ? DateUtils.toIsoDateTime(values.doorCloseTime) : null,
                categoryIds: validatedValues.categories || [],
                ticketTypeIds: validatedValues.ticketTypes || [],
                saleChannelIds: validatedValues.saleChannelIds.map((saleChannel: { key: string }) => saleChannel.key) || [],
            };
            delete adaptedValues.categories;
            delete adaptedValues.ticketTypes;
            delete adaptedValues.eventCompleted;
            delete adaptedValues.eventInProgress;

            if (mode === Mode.CREATE.key) {
                if (selectedLogoFile) {
                    await EventService.createEventWithUpload(adaptedValues, selectedLogoFile);
                } else {
                    await EventService.createEvent(adaptedValues);
                }
                createSuccessNotification("Event", "Event created successfully");
                handleBack();
            } else if (mode === Mode.EDIT.key) {
                if (selectedLogoFile) {
                    await EventService.updateEventWithUpload(id!, adaptedValues, selectedLogoFile);
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
        setSelectedLogoFile(null);
        setLogoFileList([]);
        navigate('/event');
    };

    const renderForm = (
            entity: EventResponseDto,
            currentMode: string,
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
                            name="ticketTypes"
                            label="Ticket Types"
                            rules={[{required: true, message: 'Please select at least one ticket type!'}]}
                    >
                        <Select
                                mode="multiple"
                                placeholder="Select ticket types"
                                options={ticketTypes.map(ticketType => ({
                                    label: ticketType.name,
                                    value: ticketType.id
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

                    <Form.Item name="saleChannelIds" label="Sale Channels">
                        <DataTransfer
                                onChange={(data: DataItemModel[]) => {
                                    form.setFieldsValue({saleChannelIds: data});
                                }}
                                dataSource={buildSaleChannelItem(saleChannels)}
                                selectedKeys={saleChannelsByEvent?.map(item => item.id) || []}
                                titles={['Available Sale Channels', 'Assigned Sale Channels']}
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
                            className="pb-5!"
                    >
                        <ImageUpload
                                fileList={logoFileList}
                                onFileChange={handleLogoFileChange}
                                disabled={isReadOnly}
                                acceptedTypes={['image/jpeg', 'image/png']}
                                multiple={false}
                        />
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
                        <div className="flex justify-end items-center gap-1.5 absolute bottom-4 right-4">
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
                <Spin spinning={isLoading} size="large">
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
                                showBackButton={showBackButton}
                        />
                    </SinglePageLayout>
                </Spin>
            </div>
    );
}

export default EventForm;