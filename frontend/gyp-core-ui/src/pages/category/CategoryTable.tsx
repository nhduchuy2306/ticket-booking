import { TableProps } from "antd";
import React from "react";
import { DoublePageTable } from "../../components/layout/doulepage/DoublePageTable.tsx";
import { CategoryServiceAdapter } from "../../services/Event/CategoryService.ts";

export interface CategoryTableProps {
}

const CategoryTable: React.FC<CategoryTableProps> = () => {
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
                    service={CategoryServiceAdapter}
                    createButtonTooltip="Create New Category"
                    pagination={{
                        pageSize: 5,
                        showSizeChanger: false,
                        showTotal: (total, range) => `${range[0]}-${range[1]} of ${total} categories`
                    }}
            />
    );
}

export default CategoryTable;