import { TableProps } from "antd";
import React from "react";
import { DoublePageTable } from "../../components/layout/doulepage/DoublePageTable.tsx";
import { OrganizationServiceAdapter } from "../../services/Auth/OrganizationService.ts";

export interface OrganizationTableProps {
}

const OrganizationTable: React.FC<OrganizationTableProps> = () => {
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
                    service={OrganizationServiceAdapter}
                    createButtonTooltip="Create New Venue"
                    pagination={{
                        pageSize: 5,
                        showSizeChanger: false,
                        showTotal: (total, range) => `${range[0]}-${range[1]} of ${total} categories`
                    }}
            />
    );
}

export default OrganizationTable;