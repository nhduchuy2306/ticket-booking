import { Button, Form, Input, notification } from "antd";
import React, { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import DataTransfer from "../../components/data-transfer/DataTransfer.tsx";
import { RoleItemModel } from "../../components/data-transfer/DataTransferModel.ts";
import SinglePageForm from "../../components/layout/singlepage/SinglePageForm.tsx";
import SinglePageLayout from "../../components/layout/singlepage/SinglePageLayout.tsx";
import { Mode } from "../../configs/Constants.ts";
import {
    UserAccountRequestDto,
    UserAccountResponseDto,
    UserGroupResponseDto
} from "../../models/generated/auth-service-models";
import { UserAccountService, UserAccountServiceAdapter } from "../../services/Auth/UserAccountService.ts";
import { UserGroupService } from "../../services/Auth/UserGroupService.ts";
import { DateUtils } from "../../utils/DateUtils.ts";

interface UserAccountFormProps {
    mode: string;
}

const UserAccountForm: React.FC<UserAccountFormProps> = ({mode}) => {
    const [data, setData] = useState<UserAccountResponseDto | null>(null);
    const [userGroups, setUserGroups] = useState<UserGroupResponseDto[]>([]);
    const [isLoading, setIsLoading] = useState<boolean>(false);
    const {id} = useParams<{ id: string }>();
    const [form] = Form.useForm();
    const navigate = useNavigate();

    useEffect(() => {
        const fetchData = async () => {
            setIsLoading(true);
            try {
                // Fetch user groups
                const userGroups = await UserGroupService.getAllUserGroups();
                setUserGroups(userGroups);

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
                notification.error({message: "Failed to fetch data"});
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
                roles: data.userGroupList || [],
            });
        } else {
            form.resetFields();
        }
    }, [data, form, mode]);

    const buildUserRoleItem = (userGroups: UserGroupResponseDto[]): RoleItemModel[] => {
        return userGroups.map((group) => ({
            key: group.id,
            title: group.name,
            description: group.description || "",
        }));
    };

    const renderForm = (_: UserAccountResponseDto, currentMode: string, onSave: (values: UserAccountRequestDto) => Promise<void>) => {
        const isReadOnly = currentMode === Mode.READ_ONLY.key;

        const handleSave = async (values: any) => {
            const validatedValues = await form.validateFields();
            const adaptedValues = {
                ...validatedValues,
                dob: values.dob ? DateUtils.formatToDateTime(values.dob) : null,
                userGroupList: values?.roles.map((role: RoleItemModel) => role.key)
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

                    <Form.Item name="roles" label="Role Assignment">
                        <DataTransfer
                                onChange={(data: RoleItemModel[]) => {
                                    form.setFieldsValue({roles: data});
                                }}
                                dataSource={buildUserRoleItem(userGroups)}
                                selectedKeys={data?.userGroupList?.map(item => item.id) || []}
                        />
                    </Form.Item>

                    {!isReadOnly && (
                            <Form.Item>
                                <Button
                                        type="primary"
                                        htmlType="submit"
                                        loading={isLoading}
                                        className="float-right"
                                >
                                    {currentMode === Mode.CREATE.key ? 'Create' : 'Update'}
                                </Button>
                            </Form.Item>
                    )}
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
    );
};

export default UserAccountForm;