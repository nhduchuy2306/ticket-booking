import { Button, Collapse, CollapseProps, FormInstance, Space } from "antd";
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
                    <Space direction="vertical" size="middle" style={{display: 'flex'}}>
                        {renderForm()}
                        {!isReadOnly && (
                                <div className="float-right">
                                    <Button
                                            type="default"
                                            danger
                                            onClick={() => onRemove(index)}
                                            icon={<BiTrash/>}
                                    />
                                </div>
                        )}
                    </Space>
            ),
        }
    ];

    return (
            <Collapse
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