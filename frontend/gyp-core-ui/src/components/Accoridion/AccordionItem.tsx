import { Button, Collapse, CollapseProps, Form, FormInstance } from "antd";
import { FormListFieldData } from "antd/es/form/FormList";
import React from "react";
import { AiFillCaretRight } from "react-icons/ai";
import { BiTrash } from "react-icons/bi";

export interface AccordionItemProps {
    field: FormListFieldData;
    index: number;
    form: FormInstance;
    isReadOnly: boolean;
    onRemove: (index: number) => void;
    renderForm: () => React.ReactNode;
}

const AccordionItem: React.FC<AccordionItemProps> = ({
                                                         field,
                                                         index,
                                                         form,
                                                         isReadOnly,
                                                         onRemove,
                                                         renderForm
                                                     }) => {
    const getLabel = () => {
        try {
            const formValues = form.getFieldsValue();
            const venueMapList = formValues.venueMapList || [];
            const fieldData = venueMapList[field.name];

            if (fieldData?.name) {
                return fieldData.name;
            }
        } catch (error) {
            console.error('Error getting field data:', error);
        }

        return `New Item ${index + 1}`;
    };

    const items: CollapseProps['items'] = [
        {
            key: field.key,
            label: getLabel(),
            children: (
                    <div className="accordion-item">
                        {renderForm()}
                        {!isReadOnly && (
                                <Form.Item className="float-right">
                                    <Button
                                            type="link"
                                            danger
                                            onClick={() => onRemove(index)}
                                            icon={<BiTrash/>}
                                    />
                                </Form.Item>
                        )}
                    </div>
            ),
        }
    ];

    return (
            <Collapse
                    className="mb-2!"
                    items={items}
                    expandIcon={({isActive}) => (
                            <AiFillCaretRight
                                    style={{transform: `rotate(${isActive ? 90 : 0}deg)`}}
                            />
                    )}
            />
    );
}

export default AccordionItem;