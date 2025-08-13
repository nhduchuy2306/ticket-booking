import { TableProps, Tag } from "antd";
import React from "react";
import { DoublePageTable } from "../../components/layout/doulepage/DoublePageTable.tsx";
import { SaleChannelServiceAdapter } from "../../services/SaleChannel/SaleChannelService.ts";

interface SaleChannelTableProps {
}

const SaleChannelTable: React.FC<SaleChannelTableProps> = () => {
    const columns: TableProps['columns'] = [
        {
            title: 'Name',
            dataIndex: 'name',
            key: 'name',
        },
        {
            title: 'Description',
            dataIndex: 'description',
            key: 'description',
        },
        {
            title: 'Type',
            dataIndex: 'type',
            key: 'type',
        },
        {
            title: 'Status',
            dataIndex: 'status',
            key: 'status',
            render: (status: string) => (
                    <Tag color={status === 'ACTIVE' ? 'green' : 'red'}>{status}</Tag>
            ),
        },
    ];

    return (
            <DoublePageTable
                    columns={columns}
                    service={SaleChannelServiceAdapter}
                    createButtonTooltip="Create New Venue Map"
                    pagination={{
                        pageSize: 5,
                        showSizeChanger: false,
                        showTotal: (total, range) => `${range[0]}-${range[1]} of ${total} categories`
                    }}
            />
    );
}

export default SaleChannelTable;