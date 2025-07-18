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
    showCreateUser?: boolean;
    showChangeUser?: boolean;
    showCreateTimestamp?: boolean;
    showChangeTimestamp?: boolean;
}

const MetaData: React.FC<MetaDataProps> = ({
                                               metadata,
                                               showCreateUser = true,
                                               showChangeUser = true,
                                               showCreateTimestamp = true,
                                               showChangeTimestamp = true
                                           }) => {
    const getItems: (panelStyle: CSSProperties) => CollapseProps['items'] = (panelStyle) => [
        {
            key: 'metadata',
            label: 'Metadata',
            children: (
                    <div className="bg-gray-100 p-2! rounded-md flex flex-col justify-center items-start">
                        {metadata?.id && (
                                <MetaDataItem label="ID" data={metadata?.id}/>
                        )}
                        {showCreateUser && metadata?.createUser && (
                                <MetaDataItem label="Create User" data={metadata.createUser}/>
                        )}
                        {showChangeUser && metadata?.changeUser && (
                                <MetaDataItem label="Change User" data={metadata.changeUser}/>
                        )}
                        {showCreateTimestamp && metadata?.createTimestamp && (
                                <MetaDataItem label="Create Timestamp"
                                              data={metadata.createTimestamp}
                                              formatter={(data) => DateUtils.formatToDateTime(data)}/>
                        )}
                        {showChangeTimestamp && metadata?.changeTimestamp && (
                                <MetaDataItem label="Change Timestamp"
                                              data={metadata.changeTimestamp}
                                              formatter={(data) => DateUtils.formatToDateTime(data)}/>
                        )}
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