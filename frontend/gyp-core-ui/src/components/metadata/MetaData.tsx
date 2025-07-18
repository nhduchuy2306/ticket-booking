import { Collapse, CollapseProps } from "antd";
import React, { CSSProperties } from "react";
import { AiFillCaretRight } from "react-icons/ai";
import { DateUtils } from "../../utils/DateUtils.ts";
import MetaDataItem from "./MetaDataItem.tsx";

export interface MetaDataModel {
    id?: string;
    createUser?: string;
    changeUser?: string;
    createTimestamp?: string;
    changeTimestamp?: string;
}

export interface MetaDataProps {
    metadata?: MetaDataModel;
}

const MetaData: React.FC<MetaDataProps> = ({metadata}) => {
    const getItems: (panelStyle: CSSProperties) => CollapseProps['items'] = (panelStyle) => [
        {
            key: 'metadata',
            label: 'Metadata',
            children: (
                    <div className="bg-gray-100 p-2! rounded-md flex flex-col justify-center items-start">
                        <MetaDataItem label="ID" data={metadata?.id}/>
                        <MetaDataItem label="Create User" data={metadata?.createUser}/>
                        <MetaDataItem label="Change User" data={metadata?.changeUser}/>
                        <MetaDataItem label="Create Timestamp"
                                      data={metadata?.createTimestamp}
                                      formatter={(data) => DateUtils.formatToDateTime(data)}/>
                        <MetaDataItem label="Change Timestamp"
                                      data={metadata?.changeTimestamp}
                                      formatter={(data) => DateUtils.formatToDateTime(data)}/>
                    </div>
            ),
            style: panelStyle
        }
    ];

    const panelStyle: React.CSSProperties = {
        border: 'none',
        background: 'transparent',
    };

    return (
            <div className="-ml-2!">
                <Collapse
                        ghost
                        expandIcon={({isActive}) => <AiFillCaretRight
                                style={{transform: `rotate(${isActive ? 90 : 0}deg)`}}/>}
                        items={getItems(panelStyle)}
                />
            </div>
    );
}

export default MetaData;