import { TableProps } from "antd";
import React from "react";
import { DoublePageTable } from "../../components/layout/doulepage/DoublePageTable.tsx";
import { EventImageServiceAdapter } from "../../services/Event/EventImageService.ts";

interface EventImageTableProps {
}

const EventImageTable: React.FC<EventImageTableProps> = () => {
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
    ];

    return (
            <DoublePageTable
                    columns={columns}
                    service={EventImageServiceAdapter}
                    createButtonTooltip="Create New Event Image"
                    pagination={{
                        pageSize: 5,
                        showSizeChanger: false,
                        showTotal: (total, range) => `${range[0]}-${range[1]} of ${total} categories`
                    }}
            />
    );
}

export default EventImageTable;