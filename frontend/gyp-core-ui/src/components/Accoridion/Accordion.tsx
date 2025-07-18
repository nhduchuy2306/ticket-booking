import React from "react";

export interface AccordionProps {
    children: React.ReactNode;
}

const Accordion: React.FC<AccordionProps> = ({children}) => {
    return (
            <div>{children}</div>
    );
}

export default Accordion;