import React from 'react';
import { useNavigate } from "react-router-dom";
import SinglePageLayout from "../../components/layout/singlepage/SinglePageLayout.tsx";
import { UserAccountResponseDto } from "../../models/generated/auth-service-models";
import UserAccountTable from "./UserAccountTable.tsx";

export interface UserAccountPageProps {
}

const UserAccountPage: React.FC<UserAccountPageProps> = () => {
    const navigate = useNavigate();

    const handleNavigate = (path: string, entity?: UserAccountResponseDto) => {
        if (path === '/create') {
            navigate('/user-account/create');
        } else if (path === '/edit') {
            navigate(`/user-account/edit/${entity?.id}`);
        } else if (path === '/view') {
            navigate(`/user-account/view/${entity?.id}`);
        } else {
            navigate('/user-account');
        }
    };

    return (
            <SinglePageLayout onNavigate={handleNavigate}>
                <UserAccountTable/>
            </SinglePageLayout>
    );
}

export default UserAccountPage;