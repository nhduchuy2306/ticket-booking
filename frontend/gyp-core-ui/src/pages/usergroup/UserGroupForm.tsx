import { Button, Checkbox, Form, Input, Space } from "antd";
import React, { useEffect, useState } from "react";
import MetaData from "../../components/metadata/MetaData.tsx";
import PermissionTable from "../../components/permission/PermissionTable.tsx";
import { SPLITTER_CHARACTER } from "../../configs/Constants.ts";
import { FormState } from "../../models/enums/FormState.ts";
import {
    ApplicationPermissionDto,
    PermissionItem,
    UserGroupPermissions,
    UserGroupRequestDto,
    UserGroupResponseDto
} from "../../models/generated/auth-service-models";
import { UserGroupService } from "../../services/Auth/UserGroupService.ts";
import { IamService } from "../../services/Iam/IamService.ts";

interface UserGroupFormProps {
    entity: UserGroupResponseDto;
    mode: string;
    onSave: (values: UserGroupRequestDto) => Promise<void>;
    onCancel: () => void;
    allPermissions: ApplicationPermissionDto[];
}

const UserGroupForm: React.FC<UserGroupFormProps> = ({entity, mode, onSave, onCancel, allPermissions,}) => {
    const [form] = Form.useForm();
    const [selectedPermissions, setSelectedPermissions] = useState<React.Key[]>([]);

    const isAdmin = Form.useWatch('administrator', form);
    const isReadOnly = mode === FormState.READ_ONLY.key;
    const isCreateMode = mode === FormState.CREATE.key;

    useEffect(() => {
        if (entity) {
            initializeForm();
        } else {
            resetForm();
        }
    }, [entity, form]);

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
        if (entity) {
            const {administrator, name, description, id} = entity;
            const initialPermissions = extractPermissionKeys();

            form.setFieldsValue({administrator, name, description, id});
            form.setFieldsValue({userGroupPermissions: initialPermissions});
            setSelectedPermissions(initialPermissions);
        }
    };

    const extractPermissionKeys = (): React.Key[] => {
        if (entity) {
            return entity.userGroupPermissions.permissionItems.flatMap(permissionItem =>
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
            } as PermissionItem))
        };
    };

    const handleSubmit = async () => {
        const values = await form.validateFields();
        values.userGroupPermissions = buildPermissions(form.getFieldValue("userGroupPermissions"));
        await onSave(values);
        if (!isCreateMode) {
            const userAccount = await UserGroupService.getUserAccountByUserGroupId(entity.id);
            if (userAccount) {
                const userId = IamService.getUserId();
                if (userId && userAccount.id === userId) {
                    await IamService.handleRefreshToken();
                }
            }
        }
        resetForm();
    };

    const handleReset = () => {
        if (entity) {
            initializeForm();
        } else {
            resetForm();
        }
    };

    return (
            <Form form={form} layout="vertical" size="middle" onFinish={handleSubmit} disabled={isReadOnly}>
                <Form.Item name="id" label="Id" hidden={isCreateMode}>
                    <Input disabled/>
                </Form.Item>

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
                    <Checkbox disabled={isReadOnly}>Administrator</Checkbox>
                </Form.Item>

                {!isAdmin && (
                        <Form.Item name="userGroupPermissions" label="Permissions">
                            <PermissionTable
                                    disabled={isReadOnly}
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

                {isReadOnly &&
                    <MetaData
                        metadata={{
                            id: entity?.id,
                            createUser: entity?.createUser,
                            changeUser: entity?.changeUser,
                            createTimestamp: entity?.createTimestamp,
                            changeTimestamp: entity?.changeTimestamp
                        }}
                    />
                }

                {!isReadOnly && (
                        <Form.Item>
                            <Space className="float-right">
                                <Button type="default" className="bg-red-500" onClick={handleReset}>
                                    Reset
                                </Button>
                                <Button type="default" onClick={onCancel}>
                                    Cancel
                                </Button>
                                <Button type="primary" htmlType="submit">
                                    {isCreateMode ? "Create" : "Update"}
                                </Button>
                            </Space>
                        </Form.Item>
                )}
            </Form>
    );
};

export default UserGroupForm;