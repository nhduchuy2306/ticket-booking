import React from "react";
import { UserGroupModel } from "../../models/AuthService/UserGroupModel.ts";

export interface UserGroupFormProps {
    selectedUserGroup: UserGroupModel;
}

const UserGroupForm: React.FC<UserGroupFormProps> = ({selectedUserGroup}) => {
    return (
            <div>
                <div>{selectedUserGroup.id}</div>
                <div>{selectedUserGroup.name}</div>
            </div>
    );
}

export default UserGroupForm;