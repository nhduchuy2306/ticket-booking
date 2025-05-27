import { Button, Flex, TableProps, Tooltip } from "antd";
import React, { useEffect, useState } from "react";
import { BiPlus } from "react-icons/bi";
import DataTable from "../../components/table/DataTable.tsx";
import { Mode } from "../../configs/Constants.ts";
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
    setMode?: (value: string) => void;
}

const UserGroupTable: React.FC<UserGroupTableProps> = (userGroupProps) => {
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
        if (userGroupProps.setSelectedUserGroup) {
            userGroupProps.setSelectedUserGroup(record);
        }
        if (userGroupProps.setMode) {
            userGroupProps.setMode(Mode.EDIT.key);
        }
    };

    const handleRowClick = (record: UserGroupModel, index?: number) => {
        console.log('Clicked row:', record, index);
        if (userGroupProps.setSelectedUserGroup) {
            userGroupProps.setSelectedUserGroup(record);
        }
        if (userGroupProps.setMode) {
            userGroupProps.setMode(Mode.READ_ONLY.key);
        }
    };

    const handleCreateClick = () => {
        if (userGroupProps.setMode) {
            userGroupProps.setMode(Mode.CREATE.key);
        }
    }

    return (
            <Flex gap="middle" vertical align="flex-end">
                <Tooltip title="Add new User Group">
                    <Button type="default" icon={<BiPlus/>} onClick={handleCreateClick}/>
                </Tooltip>
                <DataTable<UserGroupModel>
                        data={data}
                        columns={columns}
                        loading={loading}
                        rowKey={(record) => record.id}
                        onRowDoubleClick={handleRowDoubleClick}
                        onRowClick={handleRowClick}
                />
            </Flex>
    );
}

export default UserGroupTable;