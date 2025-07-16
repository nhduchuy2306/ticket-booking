import { TableProps } from "antd";
import React from "react";
import { DoublePageTable } from "../../components/layout/doulepage/DoublePageTable.tsx";
import { VenueServiceAdapter } from "../../services/Event/VenueService.ts";

export interface VenueTableProps {
}

const VenueTable: React.FC<VenueTableProps> = () => {
    const columns: TableProps['columns'] = [
        {
            title: 'ID',
            dataIndex: 'id',
            key: 'id',
            width: '10%',
        },
        {
            title: 'Name',
            dataIndex: 'name',
            key: 'name',
            width: '40%',
        },
        {
            title: 'Address',
            dataIndex: 'address',
            key: 'address',
            width: '40%',
        },
        {
            title: 'Description',
            dataIndex: 'description',
            key: 'description',
            width: '50%',
        },
    ];

    return (
            <DoublePageTable
                    columns={columns}
                    service={VenueServiceAdapter}
                    createButtonTooltip="Create New Venue"
                    pagination={{
                        pageSize: 5,
                        showSizeChanger: false,
                        showTotal: (total, range) => `${range[0]}-${range[1]} of ${total} categories`
                    }}
            />
    );
}

export default VenueTable;