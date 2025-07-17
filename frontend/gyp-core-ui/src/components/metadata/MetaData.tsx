import { Collapse, CollapseProps } from "antd";
import React, { CSSProperties } from "react";
import { AiFillCaretRight } from "react-icons/ai";

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
                    <div>
                        {metadata?.id && (
                                <p><strong>ID:</strong> {metadata.id}</p>
                        )}
                        {showCreateUser && metadata?.createUser && (
                                <p><strong>Created By:</strong> {metadata.createUser}</p>
                        )}
                        {showChangeUser && metadata?.changeUser && (
                                <p><strong>Last Modified By:</strong> {metadata.changeUser}</p>
                        )}
                        {showCreateTimestamp && metadata?.createTimestamp && (
                                <p><strong>Created On:</strong> {metadata.createTimestamp}</p>
                        )}
                        {showChangeTimestamp && metadata?.changeTimestamp && (
                                <p><strong>Last Modified On:</strong> {metadata.changeTimestamp}</p>
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
            <Collapse
                    bordered={false}
                    expandIcon={({isActive}) => <AiFillCaretRight
                            style={{transform: `rotate(${isActive ? 90 : 0}deg)`}}/>}
                    items={getItems(panelStyle)}
            />
    );
}

export default MetaData;