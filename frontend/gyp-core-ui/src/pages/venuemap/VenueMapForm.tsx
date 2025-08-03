import { Button, Form, Input, InputNumber, notification, Select, Space } from "antd";
import React, { useEffect, useState } from "react";
import MetaData from "../../components/metadata/MetaData.tsx";
import { useFormLogic } from "../../hooks/form/useFormLogic.tsx";
import {
    SeatMapResponseDto,
    VenueMapRequestDto,
    VenueMapResponseDto,
    VenueResponseDto
} from "../../models/generated/event-service-models";
import { SeatMapService } from "../../services/Event/SeatMapService.ts";
import { VenueService } from "../../services/Event/VenueService.ts";

interface VenueMapFormProps {
    entity: VenueMapResponseDto;
    mode: string;
    onSave: (values: VenueMapRequestDto) => Promise<void>;
    onCancel: () => void;
}

const VenueMapForm: React.FC<VenueMapFormProps> = ({entity, mode, onSave, onCancel}) => {
    const {
        form,
        isReadOnly,
        isCreateMode,
        isEditMode,
        handleSubmit,
        handleReset
    } = useFormLogic<VenueMapRequestDto>({entity, mode, onSave});
    const [seatMaps, setSeatMaps] = useState<SeatMapResponseDto[]>([]);
    const [venues, setVenues] = useState<VenueResponseDto[]>([]);

    useEffect(() => {
        const fetchData = async () => {
            try {
                // Fetch Seat Map data
                const seatMaps = await SeatMapService.getAllSeatMaps();
                if (seatMaps) {
                    setSeatMaps(seatMaps);
                } else {
                    setSeatMaps([]);
                }

                // Fetch Venue Data
                const venues = await VenueService.getAllVenues();
                if (venues) {
                    setVenues(venues);
                } else {
                    setVenues([]);
                }
            } catch (error) {
                console.error("Failed to fetch seat maps:", error);
                notification.error({message: "Error fetching seat maps"});
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
                        label="Venue Id"
                    >
                        <Input disabled/>
                    </Form.Item>}

                    <Form.Item
                            name="name"
                            label="Venue Name"
                            rules={[
                                {required: true, message: 'Please enter venue name'},
                                {min: 2, message: 'Venue name must be at least 2 characters'}
                            ]}
                    >
                        <Input placeholder="Enter venue name"/>
                    </Form.Item>

                    <Form.Item
                            name="height"
                            label="Height"
                            rules={[
                                {required: true, message: 'Please enter height'},
                            ]}
                    >
                        <InputNumber min={2} placeholder="Enter height in pixels" style={{width: '100%'}}/>
                    </Form.Item>

                    <Form.Item
                            name="width"
                            label="Width"
                            rules={[
                                {required: true, message: 'Please enter width'},
                            ]}
                    >
                        <InputNumber min={2} placeholder="Enter width in pixels" style={{width: '100%'}}/>
                    </Form.Item>

                    <Form.Item
                            name="seatMapId"
                            label="Seat Map"
                            rules={[
                                {required: true, message: 'Please select a seat map'},
                            ]}
                    >
                        <Select
                                options={seatMaps.map(seatMap => ({
                                    label: seatMap.name,
                                    value: seatMap.id
                                }))}
                        />
                    </Form.Item>

                    <Form.Item
                            name="venueId"
                            label="Venue"
                            rules={[
                                {required: true, message: 'Please select a venue'},
                            ]}
                    >
                        <Select
                                options={venues.map(venue => ({
                                    label: venue.name,
                                    value: venue.id
                                }))}
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

export default VenueMapForm;