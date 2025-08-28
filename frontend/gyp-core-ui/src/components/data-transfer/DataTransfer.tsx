import { Transfer } from 'antd';
import React, { useEffect, useState } from 'react';
import { DataItemModel } from "./DataTransferModel.ts";

interface DataTransferProps {
    onChange: (data: DataItemModel[]) => void;
    dataSource: DataItemModel[];
    selectedKeys?: React.Key[];
    titles: string[];
}

const DataTransfer: React.FC<DataTransferProps> = ({onChange, dataSource, selectedKeys, titles}) => {
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
                <Transfer<DataItemModel>
                        dataSource={dataSource}
                        targetKeys={targetKeys}
                        onChange={handleChange}
                        render={(item) => item.title}
                        rowKey={(item) => item.key}
                        listStyle={{width: '340px', height: '300px'}}
                        titles={titles}
                        footer={(_, info) => {
                            return (
                                    <div className="p-[8px] text-center text-[14px] text-gray-500">
                                        {info?.direction === 'left' ? titles[0] : titles[1]}
                                    </div>
                            );
                        }}
                />
            </div>
    );
}

export default DataTransfer;