import { TableProps } from "antd";
import React from "react";
import { DoublePageTable } from "../../components/layout/doulepage/DoublePageTable.tsx";
import { TicketTypeServiceAdapter } from "../../services/Event/TicketTypeService.ts";

export interface TicketTypeTableProps {
}

const TicketTypeTable: React.FC<TicketTypeTableProps> = () => {
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
            title: 'Description',
            dataIndex: 'description',
            key: 'description',
            width: '40%',
        },
        {
            title: 'Status',
            dataIndex: 'status',
            key: 'status',
            width: '40%',
        },
    ];

    return (
            <DoublePageTable
                    columns={columns}
                    service={TicketTypeServiceAdapter}
                    createButtonTooltip="Create New Ticket Type"
                    pagination={{
                        pageSize: 5,
                        showSizeChanger: false,
                        showTotal: (total, range) => `${range[0]}-${range[1]} of ${total} categories`
                    }}
            />
    );
}

export default TicketTypeTable;