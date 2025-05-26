import { TableProps } from "antd";
import React, { useEffect, useState } from "react";
import DataTable from "../../components/table/DataTable.tsx";
import { UserGroupModel } from "../../models/AuthService/UserGroupModel.ts";
import { UserGroupService } from "../../services/Auth/UserGroupService.ts";

type ColumnsType<T extends object = object> = TableProps<T>['columns'];

const columns: ColumnsType<UserGroupModel> = [
    {
        title: 'Id',
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
        render: (value: boolean) => value ? "Yes" : "No"
    },
];

export interface UserGroupTableProps {
    setSelectedUserGroup?: (record: UserGroupModel) => void;
}

const UserGroupTable: React.FC<UserGroupTableProps> = ({setSelectedUserGroup}) => {
    const [data, setData] = useState<UserGroupModel[]>([]);
    const [loading, setLoading] = useState(false);

    useEffect(() => {
        setLoading(true);
        UserGroupService.getAllUserGroups().then((item) => {
            setData(item);
        }).finally(() => setLoading(false));
    }, []);

    const handleRowDoubleClick = (record: UserGroupModel, index?: number) => {
        console.log('Double-clicked row:', record, index);
        alert(`Double-clicked on: ${record.name} (ID: ${record.id})`);
        if (setSelectedUserGroup) {
            setSelectedUserGroup(record);
        }
    };

    const handleRowClick = (record: UserGroupModel, index?: number) => {
        console.log('Clicked row:', record, index);
        if (setSelectedUserGroup) {
            setSelectedUserGroup(record);
        }
    };

    return (
            <DataTable<UserGroupModel>
                    data={data}
                    columns={columns}
                    loading={loading}
                    rowKey={(record) => record.id}
                    onRowDoubleClick={handleRowDoubleClick}
                    onRowClick={handleRowClick}
            />
    );
}

export default UserGroupTable;