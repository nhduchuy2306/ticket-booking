import { TableProps } from "antd";
import React from "react";
import { DoublePageTable } from "../../components/layout/doulepage/DoublePageTable.tsx";
import { CategoryServiceAdapter } from "./CategoryAdapter.ts";

export interface CategoryTableProps {
}

const CategoryTable: React.FC<CategoryTableProps> = () => {
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
            width: '50%',
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