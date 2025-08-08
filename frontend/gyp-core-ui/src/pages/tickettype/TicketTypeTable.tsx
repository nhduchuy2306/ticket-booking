import { TableProps } from "antd";
import React from "react";
import { DoublePageTable } from "../../components/layout/doulepage/DoublePageTable.tsx";
import { TicketTypeServiceAdapter } from "../../services/Event/TicketTypeService.ts";

export interface TicketTypeTableProps {
}

const TicketTypeTable: React.FC<TicketTypeTableProps> = () => {
    const columns: TableProps['columns'] = [
        {
            title: 'Name',
            dataIndex: 'name',
            key: 'name',
            sorter: true,
        },
        {
            title: 'Description',
            dataIndex: 'description',
            key: 'description',
        },
        {
            title: 'Status',
            dataIndex: 'status',
            key: 'status',
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