import { Button, Dropdown, Flex, Table, TableProps, Tooltip } from 'antd';
import React, { useEffect, useState } from 'react';
import { BiPlus } from "react-icons/bi";
import { RiLoopLeftFill } from "react-icons/ri";
import { useNavigate } from "react-router-dom";
import { UserAccountModel } from "../../models/AuthService/UserAccountModel.ts";
import { UserAccountService } from "../../services/Auth/UserAccountService.ts";

type ColumnsType<T extends object = object> = TableProps<T>['columns'];

const columns: ColumnsType<UserAccountModel> = [
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
        title: 'UserName',
        dataIndex: 'username',
        width: '20%',
    },
    {
        title: 'PhoneNumber',
        dataIndex: 'phoneNumber',
        width: '20%',
    },
    {
        title: 'Email',
        dataIndex: 'email',
        width: '20%',
    },
];

const UserAccountPage: React.FC = () => {
    const navigate = useNavigate();
    const [data, setData] = useState<UserAccountModel[]>();
    const [isLoading, setIsLoading] = useState(false);
    const [pagination, setPagination] = useState({
        current: 1,
        pageSize: 5,
        total: 0,
    });

    useEffect(() => {
        void fetchUserAccount();
    }, []);

    const fetchUserAccount = async () => {
        setIsLoading(true);
        try {
            const userAccounts = await UserAccountService.getAllUserAccounts();
            setData(userAccounts);
        } catch (error) {
            console.error('Failed to fetch user accounts:', error);
        } finally {
            setIsLoading(false);
        }
    };

    const buildColumn = () => {
        return [
            ...columns,
            {
                title: 'Actions',
                width: '10%',
                render: (_: any, record: UserAccountModel) => (
                        <Dropdown menu={{
                            items: [
                                {
                                    key: 'delete',
                                    label: 'Delete',
                                    danger: true,
                                    onClick: (e) => {
                                        e.domEvent.stopPropagation();
                                        console.log(record);
                                    }
                                },
                                {
                                    key: 'edit',
                                    label: 'Edit',
                                    danger: false,
                                    onClick: (e) => {
                                        e.domEvent.stopPropagation();
                                        console.log(record);
                                        navigate(`/user-account/${record.id}`)
                                    }
                                }
                            ]
                        }}>
                            <Button type="text" onClick={(e) => e.stopPropagation()}>⋮</Button>
                        </Dropdown>
                )
            }
        ];
    }

    const handleTableChange = (newPagination: any) => {
        setPagination({
            ...pagination,
            current: newPagination.current,
        });
    };

    const handleCreateClick = () => {
        navigate("/user-account/new");
    }

    const handleSyncDataClick = async () => {
        setIsLoading(true);
        try {
            await UserAccountService.syncOrganizer();
            await fetchUserAccount();
        } catch (error) {
            console.error('Failed to sync user accounts:', error);
        } finally {
            setIsLoading(false);
        }
    }

    const RowProps = (record: UserAccountModel): React.HTMLAttributes<HTMLElement> => ({
        onDoubleClick: (e) => {
            e.stopPropagation();
            navigate(`/user-account/${record.id}`);
        },
        style: {cursor: 'pointer'}
    });

    return (
            <Flex vertical={true} align="end" className="bg-white !pt-4">
                <Flex vertical={false} className="gap-2.5 !mr-2">
                    <Tooltip title="Sync">
                        <Button type="default" icon={<RiLoopLeftFill/>} onClick={handleSyncDataClick}
                                className="!mb-1"/>
                    </Tooltip>
                    <Tooltip title="Add New User Account">
                        <Button type="default" icon={<BiPlus/>} onClick={handleCreateClick} className="!mb-1"/>
                    </Tooltip>
                </Flex>
                <Table<UserAccountModel>
                        className="w-[100%]"
                        columns={buildColumn()}
                        rowKey={(record) => record.id}
                        dataSource={data}
                        onRow={RowProps}
                        pagination={{
                            ...pagination,
                            showSizeChanger: false,
                            "showTotal": (total, range) => `${range[0]}-${range[1]} of ${total} items`,
                        }}
                        loading={isLoading}
                        onChange={handleTableChange}
                />
            </Flex>
    );
};

export default UserAccountPage;