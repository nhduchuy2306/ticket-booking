import { Button, Form, Input, Select, Space } from "antd";
import TextArea from "antd/es/input/TextArea";
import React from "react";
import MetaData from "../../components/metadata/MetaData.tsx";
import { useFormLogic } from "../../hooks/form/useFormLogic.tsx";
import { SaleChannelRequestDto, SaleChannelResponseDto } from "../../models/generated/sale-channel-service-models";
import BoxOfficeSaleChannelForm from "./subforms/BoxOfficeSaleChannelForm.tsx";
import TicketShopSaleChannelForm from "./subforms/TicketShopSaleChannelForm.tsx";

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
    const saleChannelType = Form.useWatch("type", form);

    const handleFinalSubmit = async (values: SaleChannelRequestDto) => {
        if(values.saleChannelConfig) {
            values.saleChannelConfig = {
                ...values.saleChannelConfig,
                type: values.type
            }
        }
        await handleSubmit(values);
    }

    const renderSubForm = (saleChannelType?: string) => {
        switch (saleChannelType) {
            case 'BOX_OFFICE':
                return <BoxOfficeSaleChannelForm/>;
            case 'TICKET_SHOP':
                return <TicketShopSaleChannelForm/>;
            case 'API_PARTNER':
                return <div>API Partner Sale Channel Form</div>;
            case 'MOBILE_APP':
                return <div>Mobile App Sale Channel Form</div>;
            default:
                return <div>No support</div>;
        }
    }

    return (
            <div className="p-6">
                <Form
                        form={form}
                        layout="vertical"
                        onFinish={handleFinalSubmit}
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

                    {renderSubForm(saleChannelType)}

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