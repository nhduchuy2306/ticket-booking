import { Form, FormInstance, Input, InputNumber } from "antd";
import { FormListFieldData } from "antd/es/form/FormList";
import React from "react";
import Accordion from "../../components/accoridion/Accordion.tsx";
import AccordionItem from "../../components/accoridion/AccordionItem.tsx";
import { FormState } from "../../models/enums/FormState.ts";
import { VenueMapResponseDto } from "../../models/generated/event-service-models";

export interface VenueFormAccordionProps {
    mode?: string;
    fields: FormListFieldData[];
    onAdd: (defaultValue?: any) => void;
    onRemove: (index: number) => void;
    errors: React.ReactNode[];
    form: FormInstance;
}

const VenueFormAccordion: React.FC<VenueFormAccordionProps> = ({
                                                                   mode,
                                                                   fields,
                                                                   onAdd,
                                                                   onRemove,
                                                                   errors,
                                                                   form
                                                               }) => {
    const isReadOnly = mode === FormState.READ_ONLY.key;

    const handleAddNew = () => {
        const newItem: Partial<VenueMapResponseDto> = {
            name: "",
            width: undefined,
            height: undefined
        };
        onAdd(newItem);
    };

    const renderForm = (field: FormListFieldData) => {
        return (
                <div>
                    {isReadOnly && (
                            <Form.Item
                                    name={[field.name, 'id']}
                                    label="Venue Map Id"
                            >
                                <Input disabled/>
                            </Form.Item>
                    )}

                    <Form.Item
                            name={[field.name, 'name']}
                            label="Venue Name"
                            rules={[{required: true, message: 'Please enter venue name'}]}
                    >
                        <Input
                                placeholder="Enter venue name"
                                disabled={isReadOnly}
                        />
                    </Form.Item>

                    <Form.Item
                            name={[field.name, 'width']}
                            label="Width"
                            rules={[{required: true, message: 'Please enter width'}]}
                    >
                        <InputNumber
                                placeholder="Enter width"
                                disabled={isReadOnly}
                                min={1}
                                style={{width: '100%'}}
                        />
                    </Form.Item>

                    <Form.Item
                            name={[field.name, 'height']}
                            label="Height"
                            rules={[{required: true, message: 'Please enter height'}]}
                    >
                        <InputNumber
                                placeholder="Enter height"
                                disabled={isReadOnly}
                                min={1}
                                style={{width: '100%'}}
                        />
                    </Form.Item>
                </div>
        );
    };

    return (
            <div>
                <Accordion
                        disabled={isReadOnly}
                        onAddNew={handleAddNew}
                        createButtonTooltip="Add New Venue Map"
                >
                    {fields.map((field, index) => (
                            <AccordionItem
                                    key={field.key}
                                    field={field}
                                    index={index}
                                    form={form}
                                    isReadOnly={isReadOnly}
                                    onRemove={onRemove}
                                    renderForm={() => renderForm(field)}
                            />
                    ))}
                </Accordion>

                <Form.ErrorList errors={errors}/>
            </div>
    );
};

export default VenueFormAccordion;