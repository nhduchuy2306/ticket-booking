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
            title: 'Name',
            dataIndex: 'name',
            sorter: true,
        },
        {
            title: 'UserName',
            dataIndex: 'username',
        },
        {
            title: 'PhoneNumber',
            dataIndex: 'phoneNumber',
        },
        {
            title: 'Email',
            dataIndex: 'email',
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