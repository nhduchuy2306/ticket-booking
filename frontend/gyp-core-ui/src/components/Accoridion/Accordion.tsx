import React from "react";

export interface AccordionProps<T> {
    label?: string;
    disabled?: boolean;
    onChange?: (data: T[]) => void;
    data?: T[];
}

const Accordion: React.FC = <T, >({label, disabled, onChange, data}: AccordionProps<T>) => {
    return (
            <>
                <div>{label}</div>
                <div>{disabled}</div>
                <div>{JSON.stringify(data)}</div>
                <div>
                    {onChange ? (
                            <button onClick={() => onChange(data || [])}>Trigger Change</button>
                    ) : (
                            <span>No change handler provided</span>
                    )}
                </div>
            </>
    );
}

export default Accordion;