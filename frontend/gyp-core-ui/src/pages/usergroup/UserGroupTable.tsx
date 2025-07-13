import { TableProps } from "antd";
import React from "react";
import { DoublePageTable } from "../../components/layout/doulepage/DoublePageTable.tsx";
import { UserGroupServiceAdapter } from "./UserGroupServiceAdapter.ts";

interface UserGroupTableProps {
}

const UserGroupTable: React.FC<UserGroupTableProps> = () => {
    const columns: TableProps['columns'] = [
        {
            title: 'ID',
            dataIndex: 'id',
            width: '20%',
        },
        {
            title: 'Name',
            dataIndex: 'name',
            sorter: true,
            width: '20%',
        },
        {
            title: 'Description',
            dataIndex: 'description',
            width: '20%',
        },
        {
            title: 'Administrator',
            dataIndex: 'administrator',
            width: '20%',
            render: (value: boolean) => value ? "TRUE" : "FALSE"
        },
    ];

    return (
            <DoublePageTable
                    columns={columns}
                    service={UserGroupServiceAdapter}
                    createButtonTooltip="Create New User Group"
                    pagination={{
                        pageSize: 5,
                        showSizeChanger: false,
                        showTotal: (total, range) => `${range[0]}-${range[1]} of ${total} user groups`
                    }}
            />
    );
};

export default UserGroupTable;