import React from "react";

export interface DataTableWrapperProps {
    children: React.ReactNode;
}

const DataTableWrapper: React.FC<DataTableWrapperProps> = ({children}) => {
    return (
            <div className="data-table-wrapper">
                {children}
            </div>
    );
}

export default DataTableWrapper;