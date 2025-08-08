import { TableProps } from "antd";
import React from "react";
import { DoublePageTable } from "../../components/layout/doulepage/DoublePageTable.tsx";
import { SeasonServiceAdapter } from "../../services/Event/SeasonService.ts";

export interface SeasonTableProps {
}

const SeasonTable: React.FC<SeasonTableProps> = () => {
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
                    service={SeasonServiceAdapter}
                    createButtonTooltip="Create New Season"
                    pagination={{
                        pageSize: 5,
                        showSizeChanger: false,
                        showTotal: (total, range) => `${range[0]}-${range[1]} of ${total} categories`
                    }}
            />
    );
}

export default SeasonTable;