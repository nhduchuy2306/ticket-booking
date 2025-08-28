import { Button, Form, Input, Select } from "antd";
import React, { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import DataTransfer from "../../components/data-transfer/DataTransfer.tsx";
import { DataItemModel } from "../../components/data-transfer/DataTransferModel.ts";
import SinglePageForm from "../../components/layout/singlepage/SinglePageForm.tsx";
import SinglePageLayout from "../../components/layout/singlepage/SinglePageLayout.tsx";
import { createErrorNotification } from "../../components/notification/Notification.ts";
import { Mode } from "../../models/enums/Mode.ts";
import {
    OrganizationResponseDto,
    UserAccountRequestDto,
    UserAccountResponseDto,
    UserGroupResponseDto
} from "../../models/generated/auth-service-models";
import { OrganizationService } from "../../services/Auth/OrganizationService.ts";
import { UserAccountService, UserAccountServiceAdapter } from "../../services/Auth/UserAccountService.ts";
import { UserGroupService } from "../../services/Auth/UserGroupService.ts";
import { DateUtils } from "../../utils/DateUtils.ts";

interface UserAccountFormProps {
    mode: string;
}

const UserAccountForm: React.FC<UserAccountFormProps> = ({mode}) => {
    const [data, setData] = useState<UserAccountResponseDto | null>(null);
    const [userGroups, setUserGroups] = useState<UserGroupResponseDto[]>([]);
    const [organizations, setOrganizations] = useState<OrganizationResponseDto[]>([]);
    const [isLoading, setIsLoading] = useState<boolean>(false);
    const {id} = useParams();
    const [form] = Form.useForm();
    const navigate = useNavigate();

    useEffect(() => {
        const fetchData = async () => {
            setIsLoading(true);
            try {
                // Fetch user groups
                const userGroups = await UserGroupService.getAllUserGroups();
                setUserGroups(userGroups);

                const organizations = await OrganizationService.getAllOrganizations();
                setOrganizations(organizations);

                // Fetch user account if editing
                if (id && (mode === Mode.EDIT.key || mode === Mode.READ_ONLY.key)) {
                    const response = await UserAccountService.getUserAccountById(id);
                    if (response) {
                        setData(response);
                    }
                } else {
                    setData(null);
                }
            } catch (error) {
                console.error("Error fetching data:", error);
                createErrorNotification("Error", "Failed to fetch user account data. Please try again later.");
            } finally {
                setIsLoading(false);
            }
        };
        void fetchData();
    }, [id, mode]);

    useEffect(() => {
        if (data && (mode === Mode.EDIT.key || mode === Mode.READ_ONLY.key)) {
            form.setFieldsValue({
                id: data.id,
                name: data.name,
                username: data.username,
                dob: data.dob ? DateUtils.formatDate(data.dob) : undefined,
                phoneNumber: data.phoneNumber,
                email: data.email,
                roles: buildUserRoleItem(data.userGroupList) || [],
                organizationId: data.organizationId,
            });
        } else {
            form.resetFields();
        }
    }, [data, form, mode]);

    const buildUserRoleItem = (userGroups: UserGroupResponseDto[]): DataItemModel[] => {
        return userGroups.map((group) => ({
            key: group.id,
            title: group.name,
            description: group.description || "",
        }));
    };

    const renderForm = (
            _: UserAccountResponseDto,
            currentMode: string,
            onSave: (values: UserAccountRequestDto) => Promise<void>,
            handleBack?: () => void
    ) => {
        const isReadOnly = currentMode === Mode.READ_ONLY.key;

        const handleSave = async (values: any) => {
            const validatedValues = await form.validateFields();
            const adaptedValues = {
                ...validatedValues,
                dob: values.dob ? DateUtils.toIsoDateTime(values.dob) : null,
                userGroupList: values?.roles.map((role: DataItemModel) => role.key)
            };
            delete adaptedValues.roles;
            await onSave(adaptedValues);
        };

        return (
                <Form
                        form={form}
                        layout="vertical"
                        onFinish={handleSave}
                        disabled={isReadOnly}
                >
                    <Form.Item name="id" hidden={currentMode === Mode.CREATE.key}>
                        <Input disabled={true} hidden={currentMode === Mode.CREATE.key}/>
                    </Form.Item>

                    <Form.Item
                            name="name"
                            label="Name"
                            rules={[{required: true, message: "Please input your name!"}]}
                    >
                        <Input placeholder="Name"/>
                    </Form.Item>

                    <Form.Item
                            name="username"
                            label="Username"
                            rules={[{required: true, message: "Please input your username!"}]}
                    >
                        <Input placeholder="Username"/>
                    </Form.Item>

                    {currentMode === Mode.CREATE.key && (
                            <Form.Item
                                    name="password"
                                    label="Password"
                                    rules={[{required: true, message: "Please input your password!"}]}
                            >
                                <Input.Password placeholder="Password"/>
                            </Form.Item>
                    )}

                    <Form.Item name="dob" label="Date of Birth">
                        <Input type="date" placeholder="Date of Birth"/>
                    </Form.Item>

                    <Form.Item
                            name="phoneNumber"
                            label="Phone Number"
                            rules={[{required: true, message: "Please input your phone number!"}]}
                    >
                        <Input placeholder="Phone Number"/>
                    </Form.Item>

                    <Form.Item
                            name="email"
                            label="Email"
                            rules={[{required: true, message: "Please input your email!"}]}
                    >
                        <Input type="email" placeholder="Email"/>
                    </Form.Item>

                    <Form.Item
                            name="organizationId"
                            label="Organization"
                            rules={[{required: true, message: "Please select an organization!"}]}
                    >
                        <Select
                                disabled={isReadOnly}
                                className="w-full"
                                options={organizations.map(org => ({
                                    label: org.name,
                                    value: org.id
                                }))}
                        />
                    </Form.Item>

                    <Form.Item name="roles" label="Role Assignment">
                        <DataTransfer
                                onChange={(data: DataItemModel[]) => {
                                    form.setFieldsValue({roles: data});
                                }}
                                dataSource={buildUserRoleItem(userGroups)}
                                selectedKeys={data?.userGroupList?.map(item => item.id) || []}
                                titles={['Available Roles', 'Assigned Roles']}
                        />
                    </Form.Item>

                    <div className="flex justify-end items-center gap-1.5 absolute bottom-14 right-14">
                        <Button type="default" onClick={handleBack}>Cancel</Button>
                        {!isReadOnly &&
                            <Button
                                type="primary"
                                htmlType="submit"
                                loading={isLoading}>
                                {currentMode === Mode.CREATE.key ? 'Create' : 'Update'}
                            </Button>
                        }
                    </div>

                </Form>
        );
    };

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
            <div className="bg-white">
                <SinglePageLayout onNavigate={handleNavigate}>
                    <SinglePageForm
                            service={UserAccountServiceAdapter}
                            renderForm={renderForm}
                            entity={data}
                            mode={mode}
                            successMessages={{
                                create: "User account created successfully",
                                update: "User account updated successfully"
                            }}
                            showBackButton={true}
                    />
                </SinglePageLayout>
            </div>
    );
};

export default UserAccountForm;