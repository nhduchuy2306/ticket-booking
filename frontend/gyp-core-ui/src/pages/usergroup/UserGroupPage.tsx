import React, { useEffect, useState } from "react";
import { DoublePageForm } from "../../components/layout/doulepage/DoublePageForm.tsx";
import DoublePageLayout from "../../components/layout/doulepage/DoublePageLayout.tsx";
import { ApplicationPermissionDto } from "../../models/generated/auth-service-models";
import { UserGroupService, UserGroupServiceAdapter } from "../../services/Auth/UserGroupService.ts";
import UserGroupForm from "./UserGroupForm.tsx";
import UserGroupTable from "./UserGroupTable.tsx";

const UserGroupPage: React.FC = () => {
    const [allPermissions, setAllPermissions] = useState<ApplicationPermissionDto[]>([]);

    const fetchPermissions = async () => {
        try {
            const permissions = await UserGroupService.getApplicationPermissions();
            setAllPermissions(permissions);
        } catch (error) {
            console.error("Failed to fetch permissions:", error);
        }
    };

    useEffect(() => {
        void fetchPermissions();
    }, []);

    return (
            <DoublePageLayout>
                <UserGroupTable/>
                <DoublePageForm
                        service={UserGroupServiceAdapter}
                        successMessages={{
                            create: "Category created successfully",
                            update: "Category updated successfully"
                        }}
                >
                    {({entity, mode, onSave, onCancel}) => (
                            <UserGroupForm
                                    entity={entity}
                                    allPermissions={allPermissions}
                                    mode={mode}
                                    onSave={onSave}
                                    onCancel={onCancel}
                            />
                    )}
                </DoublePageForm>
            </DoublePageLayout>
    );
};

export default UserGroupPage;