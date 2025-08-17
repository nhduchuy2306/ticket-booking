import { Button, Form, Input, notification, Select, Tooltip, Upload, UploadProps } from "antd";
import React, { useEffect, useState } from "react";
import { BiArrowBack, BiCloudUpload } from "react-icons/bi";
import { useParams } from "react-router-dom";
import { useSinglePageContext } from "../../../components/layout/singlepage/SinglePageContext.tsx";
import MetaData from "../../../components/metadata/MetaData.tsx";
import { createErrorNotification, createSuccessNotification } from "../../../components/notification/Notification.ts";
import { Mode } from "../../../models/enums/Mode.ts";
import { SeatMapRequestDto } from "../../../models/generated/event-service-models";
import { SeatMapService, SeatMapServiceAdapter } from "../../../services/Event/SeatMapService.ts";
import { useSeatMapFormContext } from "../SeatMapFormContext.tsx";

export interface SeatMapFormTabProps {
    mode: string;
}

const SeatMapFormTab: React.FC<SeatMapFormTabProps> = ({mode}) => {
    const {id} = useParams();
    const [form] = Form.useForm();
    const [isLoading, setIsLoading] = useState<boolean>(false);
    const {handleBack} = useSinglePageContext();
    const {entity, setEntity} = useSeatMapFormContext();

    const isReadOnly = mode === Mode.READ_ONLY.key;
    const isEditMode = mode === Mode.EDIT.key;
    const isCreateMode = mode === Mode.CREATE.key;

    useEffect(() => {
        if (entity && (mode === Mode.EDIT.key || mode === Mode.READ_ONLY.key)) {
            form.setFieldsValue({
                id: entity.id,
                name: entity.name,
                venueType: entity.venueType,
            });
        } else {
            form.resetFields();
        }
    }, [entity, form, mode]);

    const handleSave = async (values: SeatMapRequestDto) => {
        setIsLoading(true);
        try {
            if (isCreateMode) {
                await SeatMapServiceAdapter.create(values);
                notification.success({message: "Seat Map created successfully"});
            } else if (isEditMode && entity) {
                await SeatMapServiceAdapter.update(values, entity.id);
                notification.success({message: "Seat Map updated successfully"});
            }
        } catch (error) {
            console.error('Failed to save:', error);
            notification.error({message: "Failed to save item"});
        } finally {
            setIsLoading(false);
        }
    };

    const uploadProps: UploadProps = {
        name: "file",
        accept: ".json",
        showUploadList: false,
        customRequest: async ({file}) => {
            const formData = new FormData();
            formData.append("file", file as File);
            try {
                const response = await SeatMapService.uploadSeatMap(formData);
                console.log("Upload successful:", response);
                if (response) {
                    const uploadedSeatMap = {
                        ...entity,
                        seatConfig: response?.seatConfig,
                        stageConfig: response?.stageConfig,
                    }
                    setEntity(uploadedSeatMap);
                }
                createSuccessNotification("Seat Map", "File uploaded successfully");
            } catch (error) {
                console.error("Upload failed:", error);
                createErrorNotification("Seat Map", "File upload failed");
            }
        },
    };

    return (
            <div className="p-2! overflow-auto! h-[calc(100vh-150px)]! bg-white">
                <div className="sticky top-0 z-10 pb-2!">
                    <Tooltip title="Back to List">
                        <Button
                                type="default"
                                icon={<BiArrowBack/>}
                                onClick={handleBack}
                        />
                    </Tooltip>
                </div>
                <Form
                        className="w-full"
                        form={form}
                        layout="vertical"
                        onFinish={handleSave}
                        disabled={isReadOnly}
                >
                    {!isCreateMode &&
                        <Form.Item
                            name="id"
                            label="Seat Map id"
                        >
                            <Input disabled={true}/>
                        </Form.Item>
                    }

                    <Form.Item
                            name="name"
                            label="Seat Map Name"
                            rules={[{required: true, message: 'Please input the seat map name!'}]}
                    >
                        <Input placeholder="Enter seat map name"/>
                    </Form.Item>

                    <Form.Item
                            name="venueType"
                            label="Venue Type"
                            rules={[{required: true, message: 'Please select the venue type!'}]}
                    >
                        <Select
                                placeholder="Select venue type"
                                options={[
                                    {label: 'Round', value: 'ROUND'},
                                    {label: 'Rectangle', value: 'RECTANGLE'},
                                ]}
                        />
                    </Form.Item>

                    <Form.Item
                            name="seatMapLayout"
                            label="Upload Seat Map Layout"
                            rules={[{required: true, message: "Please upload the seat map layout!"}]}
                    >
                        <Upload {...uploadProps}>
                            <Button icon={<BiCloudUpload/>}>Click to Upload</Button>
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
                                {mode === Mode.CREATE.key ? 'Create' : 'Update'}
                            </Button>
                        </div>
                    }
                </Form>
            </div>
    );
}

export default SeatMapFormTab;