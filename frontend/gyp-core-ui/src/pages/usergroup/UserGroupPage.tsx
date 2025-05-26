import React, { useState } from "react";
import { UserGroupModel } from "../../models/AuthService/UserGroupModel.ts";
import UserGroupForm from "./UserGroupForm.tsx";
import UserGroupTable from "./UserGroupTable.tsx";
import './user-group.scss';

const UserGroupPage: React.FC = () => {
    const USER_GROUP_CONTAINER_CLASS = "user-group-container";

    const [selectedUserGroup, setSelectedUserGroup] = useState<UserGroupModel | null>(null);

    return (
            <div className={USER_GROUP_CONTAINER_CLASS}>
                <div style={{flex: 1}}>
                    <UserGroupTable setSelectedUserGroup={setSelectedUserGroup}/>
                </div>
                <div style={{flex: 1}}>
                    {selectedUserGroup ? <UserGroupForm selectedUserGroup={selectedUserGroup}/> : <h1>Never</h1>}
                </div>
            </div>
    );
}

export default UserGroupPage;