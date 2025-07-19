import { Button, Flex, Tooltip } from "antd";
import React from "react";
import { BiPlus } from "react-icons/bi";

export interface AccordionProps {
    disabled?: boolean;
    onAddNew: () => void;
    createButtonTooltip?: string;
    children: React.ReactNode;
}

const Accordion: React.FC<AccordionProps> = ({
                                                 createButtonTooltip = "Create New Item",
                                                 children,
                                                 onAddNew,
                                                 disabled = false
                                             }) => {
    return (
            <Flex vertical>
                <div className="flex justify-end mb-2!">
                    <Tooltip title={createButtonTooltip}>
                        <Button
                                type="primary"
                                icon={<BiPlus/>}
                                onClick={onAddNew}
                                disabled={disabled}
                        />
                    </Tooltip>
                </div>
                <div className="accordion-content">
                    {children}
                </div>
            </Flex>
    );
};

export default Accordion;