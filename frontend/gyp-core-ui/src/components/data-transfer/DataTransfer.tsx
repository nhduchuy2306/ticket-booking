import { Transfer } from 'antd';
import React, { useEffect, useState } from 'react';
import { RoleItemModel } from "./DataTransferModel.ts";

interface DataTransferProps {
    onChange: (data: RoleItemModel[]) => void;
    dataSource: RoleItemModel[];
    selectedKeys?: React.Key[];
};

const DataTransfer: React.FC<DataTransferProps> = ({onChange, dataSource, selectedKeys}) => {
    const [targetKeys, setTargetKeys] = useState<React.Key[]>([]);

    useEffect(() => {
        if (selectedKeys) {
            setTargetKeys(selectedKeys);
        }
    }, [selectedKeys]);

    const handleChange = (newTargetKeys: React.Key[]) => {
        setTargetKeys(newTargetKeys);
        onChange(dataSource.filter(item => newTargetKeys.includes(item.key)));
    };

    return (

            <div className="p-[24px] w-[100%] flex flex-col items-center justify-center">
                <Transfer<RoleItemModel>
                        dataSource={dataSource}
                        targetKeys={targetKeys}
                        onChange={handleChange}
                        render={(item) => item.title}
                        rowKey={(item) => item.key}
                        listStyle={{width: '340px', height: '300px'}}
                        titles={['Available Roles', 'Assigned Roles']}
                        footer={(_, info) => {
                            return (
                                    <div className="p-[8px] text-center text-[14px] text-gray-500">
                                        {info?.direction === 'left' ? 'Available roles' : 'Assigned roles'}
                                    </div>
                            );
                        }}
                />
            </div>
    );
}

export default DataTransfer;