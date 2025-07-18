import React from "react";

export interface MetaDataModel {
    id?: string;
    createUser?: string;
    changeUser?: string;
    createTimestamp?: string;
    changeTimestamp?: string;
}

export interface MetaDataItemProps {
    label?: string;
    data: any;
    formatter?: (data: any) => void;
}

const MetaDataItem: React.FC<MetaDataItemProps> = ({label, data, formatter}) => {
    return (
            <div className="flex items-center justify-center w-full">
                <strong className="flex-3">{label ? label : 'Data'}:</strong>
                <span className="ml-1 text-gray-600 flex-2/3">
                    {formatter ? formatter(data) : data}
                </span>
            </div>
    );
}

export default MetaDataItem;