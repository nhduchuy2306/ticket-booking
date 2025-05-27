import { Flex } from "antd";
import React, { useState } from "react";
import { Mode } from "../../configs/Constants.ts";
import { UserGroupModel } from "../../models/AuthService/UserGroupModel.ts";
import UserGroupForm from "./UserGroupForm.tsx";
import UserGroupTable from "./UserGroupTable.tsx";
import './user-group.scss';

const UserGroupPage: React.FC = () => {
    const [selectedUserGroup, setSelectedUserGroup] = useState<UserGroupModel | null>(null);
    const [mode, setMode] = useState<string>(Mode.READ_ONLY.key);

    return (
            <Flex gap="middle" align="start" vertical={false} style={{backgroundColor: 'white', padding: '10px'}}>
                <div style={{flex: 1}}>
                    <UserGroupTable
                            setSelectedUserGroup={setSelectedUserGroup}
                            setMode={setMode}
                    />
                </div>
                <div style={{flex: 1}}>
                    {selectedUserGroup && <UserGroupForm
                        selectedUserGroup={selectedUserGroup}
                        mode={mode}
                    />}
                </div>
            </Flex>
    );
}

export default UserGroupPage;