import { Button, TableProps, Tooltip } from 'antd';
import React from 'react';
import { RiLoopLeftFill } from "react-icons/ri";
import SinglePageTable from "../../components/layout/singlepage/SinglePageTable.tsx";
import { UserAccountService, UserAccountServiceAdapter } from "../../services/Auth/UserAccountService.ts";
import 'antd/dist/reset.css';

export interface UserAccountTableProps {
}

const UserAccountTable: React.FC<UserAccountTableProps> = () => {
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


    const handleSyncDataClick = async () => {
        try {
            await UserAccountService.syncOrganizer();
        } catch (error) {
            console.error('Failed to sync user accounts:', error);
        }
    };

    const customActions = (): React.ReactNode => {
        return (
                <>
                    <Tooltip title="Sync User Accounts">
                        <Button type="primary" icon={<RiLoopLeftFill/>} onClick={handleSyncDataClick}></Button>
                    </Tooltip>
                </>
        );
    };

    return (
            <SinglePageTable
                    columns={columns}
                    createButtonTooltip="Create New User Account"
                    service={UserAccountServiceAdapter}
                    customActions={customActions}
            />
    );
};

export default UserAccountTable;