import { Button, Dropdown, Flex, Modal, notification, Table, TableProps, Tooltip } from "antd";
import React, { useEffect, useState } from "react";
import { BiPlus } from "react-icons/bi";
import { Mode } from "../../configs/Constants.ts";
import { UserGroupModel } from "../../models/AuthService/UserGroupModel.ts";
import { UserGroupService } from "../../services/Auth/UserGroupService.ts";

type ColumnsType<T extends object = object> = TableProps<T>['columns'];

const columns: ColumnsType<UserGroupModel> = [
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

interface UserGroupTableProps {
    onSelectUserGroup: (userGroup: UserGroupModel | null, mode: string) => void;
    reload: boolean;
    onReloadComplete?: () => void;
}

const UserGroupTable: React.FC<UserGroupTableProps> = ({onSelectUserGroup, reload, onReloadComplete}) => {
    const [modal, modalContextHolder] = Modal.useModal();
    const [data, setData] = useState<UserGroupModel[]>([]);
    const [isLoading, setIsLoading] = useState(false);
    const [pagination, setPagination] = useState({
        current: 1,
        pageSize: 5,
        total: 0,
    });

    useEffect(() => {
        void fetchUserGroups();
    }, []);

    useEffect(() => {
        if (reload) {
            void (async () => {
                await fetchUserGroups();
                onReloadComplete?.();
            })();
        }
    }, [reload]);

    const fetchUserGroups = async () => {
        setIsLoading(true);
        try {
            const userGroups = await UserGroupService.getAllUserGroups();
            setData(userGroups);
        } catch (error) {
            console.error('Failed to fetch user groups:', error);
        } finally {
            setIsLoading(false);
        }
    };

    const deleteUserGroup = async (id: string) => {
        try {
            const res = await UserGroupService.deleteUserGroup(id);
            if(res) {
                await new Promise(resolve => setTimeout(resolve, 300));
                void fetchUserGroups();
                notification.success({
                    message: "Delete successfully",
                    placement: "bottomRight"
                })
            }
        } catch (error: any) {
            modal.error({
                title: "Error",
                content: error.response.data,
            });
        }
    }

    const buildColumn = () => {
        return [
            ...columns,
            {
                title: 'Actions',
                width: '10%',
                render: (_: any, record: UserGroupModel) => (
                        <Dropdown menu={{
                            items: [
                                {
                                    key: 'delete',
                                    label: 'Delete',
                                    danger: true,
                                    onClick: (e) => {
                                        e.domEvent.stopPropagation();
                                        modal.confirm({
                                            title: 'Confirm',
                                            content: 'Do you want to remove this record?',
                                            "onOk": () => {
                                                void deleteUserGroup(record.id);
                                            },
                                            "footer": (_, {OkBtn, CancelBtn}) => (
                                                    <>
                                                        <CancelBtn/>
                                                        <OkBtn/>
                                                    </>
                                            ),
                                        });
                                    }
                                }
                            ]
                        }}>
                            <Button type="text" onClick={(e) => e.stopPropagation()}>⋮</Button>
                        </Dropdown>
                )
            }
        ]
    }

    const handleRowClick = (record: UserGroupModel) => {
        onSelectUserGroup(record, Mode.READ_ONLY.key);
    };

    const handleRowDoubleClick = (record: UserGroupModel) => {
        onSelectUserGroup(record, Mode.EDIT.key);
    };

    const handleCreateClick = () => {
        onSelectUserGroup(null, Mode.CREATE.key);
    };

    const handleTableChange = (newPagination: any) => {
        setPagination({
            ...pagination,
            current: newPagination.current,
        });
    };

    const rowProps = (record: UserGroupModel): React.HTMLAttributes<HTMLElement> => {
        return {
            "onDoubleClick": () => handleRowDoubleClick?.(record),
            "onClick": () => handleRowClick?.(record),
            style: {cursor: "pointer"},
        };
    };

    return (
            <>
                <Flex gap="middle" vertical align="flex-end">
                    <Tooltip title="Add New User Group">
                        <Button type="default" icon={<BiPlus/>} onClick={handleCreateClick}/>
                    </Tooltip>
                    <Table<UserGroupModel>
                            columns={buildColumn()}
                            dataSource={data}
                            loading={isLoading}
                            rowKey={(record) => record.id}
                            onRow={rowProps}
                            pagination={{
                                ...pagination,
                                showSizeChanger: false,
                                "showTotal": (total, range) => `${range[0]}-${range[1]} of ${total} items`,
                            }}
                            onChange={handleTableChange}
                    />
                </Flex>
                {modalContextHolder}
            </>
    );
};

export default UserGroupTable;