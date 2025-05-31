import { Flex } from "antd";
import React, { useEffect, useState } from "react";
import { Mode } from "../../configs/Constants.ts";
import { UserGroupModel, UserGroupPermissionModel } from "../../models/AuthService/UserGroupModel.ts";
import { UserGroupService } from "../../services/Auth/UserGroupService.ts";
import UserGroupForm from "./UserGroupForm.tsx";
import UserGroupTable from "./UserGroupTable.tsx";

const UserGroupPage: React.FC = () => {
    const [selectedUserGroup, setSelectedUserGroup] = useState<UserGroupModel | null>(null);
    const [mode, setMode] = useState<string>(Mode.READ_ONLY.key);
    const [allPermissions, setAllPermissions] = useState<UserGroupPermissionModel[]>([]);
    const [isLoading, setIsLoading] = useState(false);
    const [reload, setReload] = useState<boolean>(false);

    const fetchPermissions = async () => {
        setIsLoading(true);
        try {
            const permissions = await UserGroupService.getApplicationPermissions();
            setAllPermissions(permissions);
        } catch (error) {
            console.error("Failed to fetch permissions:", error);
        } finally {
            setIsLoading(false);
        }
    };

    useEffect(() => {
        void fetchPermissions();
    }, []);


    const handleUserGroupSelect = (userGroup: UserGroupModel | null, newMode: string) => {
        setSelectedUserGroup(userGroup);
        setMode(newMode);
    };

    return (
            <Flex gap="middle" align="start" vertical={false} className="bg-white !p-4">
                <div className="flex-1">
                    <UserGroupTable
                            reload={reload}
                            onSelectUserGroup={(userGroup, mode) => handleUserGroupSelect(userGroup, mode)}
                            onReloadComplete={() => setReload(false)}
                    />
                </div>
                <div className="flex-1">
                    {(mode === Mode.EDIT.key || mode === Mode.READ_ONLY.key) ? (
                            <>
                                {selectedUserGroup && (
                                        <UserGroupForm
                                                allPermissions={allPermissions}
                                                selectedUserGroup={selectedUserGroup}
                                                mode={mode}
                                                isLoading={isLoading}
                                                onClearForm={() => {
                                                    setMode(Mode.READ_ONLY.key);
                                                    setSelectedUserGroup(null);
                                                }}
                                                onReload={() => setReload(true)}
                                        />
                                )}
                            </>
                    ) : (
                            <>
                                <UserGroupForm
                                        allPermissions={allPermissions}
                                        selectedUserGroup={selectedUserGroup}
                                        mode={mode}
                                        isLoading={isLoading}
                                        onClearForm={() => {
                                            setMode(Mode.READ_ONLY.key);
                                            setSelectedUserGroup(null);
                                        }}
                                        onReload={() => setReload(true)}
                                />
                            </>
                    )}

                </div>
            </Flex>
    );
};

export default UserGroupPage;