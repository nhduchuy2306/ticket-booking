import { Button, Checkbox, Form, Input, message } from "antd";
import React, { useEffect, useState } from "react";
import PermissionTable from "../../components/permission/PermissionTable.tsx";
import { Mode, SPLITTER_CHARACTER } from "../../configs/Constants.ts";
import {
    UserGroupModel,
    UserGroupPermissionModel,
    UserGroupPermissions
} from "../../models/AuthService/UserGroupModel.ts";
import { UserGroupService } from "../../services/Auth/UserGroupService.ts";

interface UserGroupFormProps {
    selectedUserGroup: UserGroupModel | null;
    mode: string;
    allPermissions: UserGroupPermissionModel[];
    isLoading: boolean;
    onClearForm: () => void;
    onReload?: () => void;
}

const UserGroupForm: React.FC<UserGroupFormProps> = ({
                                                         selectedUserGroup,
                                                         mode,
                                                         allPermissions,
                                                         isLoading,
                                                         onClearForm,
                                                         onReload
                                                     }) => {
    const [form] = Form.useForm();
    const [selectedPermissions, setSelectedPermissions] = useState<React.Key[]>([]);
    const [isFormDisabled, setIsFormDisabled] = useState(mode === Mode.READ_ONLY.key);
    const isAdmin = Form.useWatch('administrator', form);
    const isEditMode = mode === Mode.EDIT.key;
    const isCreateMode = mode === Mode.CREATE.key;

    useEffect(() => {
        setIsFormDisabled(mode === Mode.READ_ONLY.key);
        if (isCreateMode) {
            resetForm();
        } else {
            initializeForm();
        }
    }, [selectedUserGroup, mode]);

    const resetForm = () => {
        form.resetFields();
        form.setFieldsValue({
            administrator: false,
            name: '',
            description: '',
            userGroupPermissions: [],
        });
        setSelectedPermissions([]);
    };

    const initializeForm = () => {
        if (selectedUserGroup) {
            const {administrator, name, description} = selectedUserGroup;
            const initialPermissions = extractPermissionKeys();

            form.setFieldsValue({administrator, name, description});
            form.setFieldsValue({userGroupPermissions: initialPermissions});
            setSelectedPermissions(initialPermissions);
        }
    };

    const extractPermissionKeys = (): React.Key[] => {
        if (selectedUserGroup) {
            return selectedUserGroup.userGroupPermissions.permissionItems.flatMap(permissionItem =>
                    permissionItem.actions.map(action =>
                            `${permissionItem.applicationId}${SPLITTER_CHARACTER}${action}`
                    )
            );
        }
        return [];
    };

    const buildPermissions = (permissions: string[]): UserGroupPermissions => {
        const permissionMap = new Map<string, string[]>();

        permissions.forEach(permission => {
            const [applicationId, action] = permission.split(SPLITTER_CHARACTER);

            if (!applicationId || !action) {
                console.warn(`Invalid permission format: ${permission}`);
                return;
            }

            const actions = permissionMap.get(applicationId) || [];
            permissionMap.set(applicationId, [...actions, action]);
        });

        return {
            permissionItems: Array.from(permissionMap.entries()).map(([applicationId, actions]) => ({
                applicationId,
                actions,
            }))
        };
    };

    const handleSave = async () => {
        try {
            const values = await form.validateFields();
            values.userGroupPermissions = buildPermissions(form.getFieldValue("userGroupPermissions"));

            if (isCreateMode) {
                await UserGroupService.createUserGroup(values);
                message.success("User group created successfully");
            } else if (isEditMode && selectedUserGroup) {
                await UserGroupService.updateUserGroup(values, selectedUserGroup.id);
                message.success("User group updated successfully");
            }

            setIsFormDisabled(true);

            if(onReload) {
                onReload();
            }
        } catch (error) {
            console.error('Failed to save user group:', error);
            message.error("Failed to save user group");
        }
    };

    const handleCancel = () => {
        initializeForm();
        setIsFormDisabled(true);
        onClearForm();
    };

    return (
            <div className="!mt-[50px] !overflow-y-auto max-h-[calc(100vh-100px)]">
                <Form form={form} layout="vertical" disabled={isFormDisabled} size="middle">
                    <Form.Item
                            name="name"
                            label="Name"
                            rules={[
                                {required: true, message: 'Please enter a name'},
                                {min: 2, message: 'Name must be at least 2 characters'}
                            ]}
                    >
                        <Input placeholder="Enter group name"/>
                    </Form.Item>

                    <Form.Item
                            name="description"
                            label="Description"
                            rules={[{required: true, message: 'Please enter a description'}]}
                    >
                        <Input.TextArea placeholder="Enter group description" rows={3}/>
                    </Form.Item>

                    <Form.Item name="administrator" valuePropName="checked">
                        <Checkbox disabled={isFormDisabled}>Administrator</Checkbox>
                    </Form.Item>

                    {!isAdmin && (
                            <Form.Item name="userGroupPermissions" label="Permissions">
                                <PermissionTable
                                        disabled={isFormDisabled}
                                        allPermissions={allPermissions}
                                        selectedPermissions={selectedPermissions}
                                        onChange={(permissions) => {
                                            form.setFieldValue('userGroupPermissions', permissions);
                                            setSelectedPermissions(permissions);
                                        }}
                                        isAdmin={isAdmin}
                                />
                            </Form.Item>
                    )}

                    <Form.Item style={{marginTop: 24, textAlign: 'right'}}>
                        {!isFormDisabled && (
                                <>
                                    <Button
                                            style={{marginRight: 8}}
                                            onClick={handleCancel}
                                    >
                                        Cancel
                                    </Button>
                                    <Button
                                            type="primary"
                                            onClick={handleSave}
                                            loading={isLoading}
                                    >
                                        Save
                                    </Button>
                                </>
                        )}
                    </Form.Item>
                </Form>
            </div>
    );
};

export default UserGroupForm;