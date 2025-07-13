import { Button, Flex, Form, Input, Tooltip } from "antd";
import React, { useEffect, useState } from "react";
import { AiOutlineClose } from "react-icons/ai";
import { useNavigate, useParams } from "react-router-dom";
import DataTransfer from "../../components/data-transfer/DataTransfer.tsx";
import { RoleItemModel } from "../../components/data-transfer/DataTransferModel.ts";
import { Mode } from "../../configs/Constants.ts";
import { UserAccountModel } from "../../models/AuthService/UserAccountModel.ts";
import { UserGroupResponseDto } from "../../models/generated/auth-service-models";
import { UserAccountService } from "../../services/Auth/UserAccountService.ts";
import { UserGroupCRUDService } from "../../services/Auth/UserGroupService.ts";
import { DateUtils } from "../../utils/DateUtils.ts";

interface UserAccountFormProps {
    mode: string
}

const UserAccountForm: React.FC<UserAccountFormProps> = ({mode}) => {
    const [data, setData] = useState<UserAccountModel | null>(null);
    const [userGroups, setUserGroups] = useState<UserGroupResponseDto[]>([]);
    const [isLoading, setIsLoading] = useState<boolean>(false);
    const navigate = useNavigate();
    const {id} = useParams<{ id: string }>();
    const [form] = Form.useForm();

    useEffect(() => {
        if (id && mode === Mode.EDIT.key) {
            void getUserAccountById(id);
        } else {
            setData(null);
        }

        void fetchUserGroup();
    }, [id]);

    useEffect(() => {
        if (data && mode === Mode.EDIT.key) {
            form.setFieldsValue({
                id: data.id,
                name: data.name,
                username: data.username,
                dob: data.dob ? DateUtils.formatDate(data.dob) : undefined,
                phoneNumber: data.phoneNumber,
                email: data.email,
            });
        } else {
            form.resetFields();
        }
    }, [data, form]);

    const getUserAccountById = async (id: string) => {
        try {
            setIsLoading(true);
            const response = await UserAccountService.getUserAccountById(id);
            if (response) {
                setData(response);
            }
        } catch (error) {
            console.error("Error fetching user account:", error);
        }
    }

    const fetchUserGroup = async () => {
        setIsLoading(true);
        try {
            const userGroups = await UserGroupCRUDService.getAll();
            setUserGroups(userGroups);
        } catch (error) {
            console.error('Failed to fetch user accounts:', error);
        } finally {
            setIsLoading(false);
        }
    }

    const buildUserRoleItem = (userGroups: UserGroupResponseDto[]): RoleItemModel[] => {
        return userGroups.map((group) => ({
            key: group.id,
            title: group.name,
            description: group.description || "",
        }));
    }

    const handleCloseClick = () => {
        navigate("/user-account");
    }

    return (
            <Flex vertical={true} align="flex-end" className="bg-white !p-4">
                <Tooltip title="Close">
                    <Button type="default" icon={<AiOutlineClose/>} onClick={handleCloseClick} className="!mb-1"/>
                </Tooltip>
                <Form
                        form={form}
                        layout="vertical"
                        className="w-full !overflow-y-auto !h-[calc(100vh-100px)]"
                        onFinish={async (values) => {
                            console.log("Form submitted with values:", values);
                            setIsLoading(true);
                            try {
                                if (mode === Mode.CREATE.key) {
                                    await UserAccountService.createUserAccount({
                                        name: values.name,
                                        username: values.username,
                                        dob: values.dob ? DateUtils.toIsoDateTime(values.dob) : undefined,
                                        phoneNumber: values.phoneNumber,
                                        email: values.email,
                                        userGroupList: values.roles.map((item: RoleItemModel) => item.key),
                                    });
                                } else if (mode === Mode.EDIT.key && data) {
                                    await UserAccountService.updateUserAccount(data.id, values);
                                }
                                navigate("/user-account");
                            } catch (error) {
                                console.error("Error saving user account:", error);
                            } finally {
                                setIsLoading(false);
                            }
                        }}>
                    <Form.Item name="id" hidden={mode === Mode.CREATE.key}>
                        <Input disabled={true} hidden={mode === Mode.CREATE.key}/>
                    </Form.Item>

                    <Form.Item
                            name="name"
                            label="Name"
                            rules={[{required: true, message: "Please input your name!"}]}>
                        <Input placeholder="Name"/>
                    </Form.Item>

                    <Form.Item
                            name="username"
                            label="Username"
                            rules={[{required: true, message: "Please input your username!"}]}>
                        <Input placeholder="Username"/>
                    </Form.Item>

                    <Form.Item
                            name="dob"
                            label="Date of Birth">
                        <Input type="date" placeholder="Date of Birth"/>
                    </Form.Item>

                    <Form.Item
                            name="phoneNumber"
                            label="Phone Number"
                            rules={[{required: true, message: "Please input your phone number!"}]}>
                        <Input placeholder="Phone Number"/>
                    </Form.Item>

                    <Form.Item
                            name="email"
                            label="Email"
                            rules={[{required: true, message: "Please input your email!"}]}>
                        <Input type="email" placeholder="Email"/>
                    </Form.Item>
                    <Form.Item name="roles" label="Role Assignment">
                        <DataTransfer
                                onChange={(data: RoleItemModel[]) => {
                                    form.setFieldsValue({"userGroupList": data});
                                    console.log("Roles submitted:", data);
                                }}
                                dataSource={buildUserRoleItem(userGroups)}
                                selectedKeys={data?.userGroupList.map(item => item.id) || []}
                        />
                    </Form.Item>

                    <Form.Item>
                        <Button
                                type="primary"
                                htmlType="submit"
                                loading={isLoading}
                                className="float-right"
                        >Save</Button>
                    </Form.Item>
                </Form>
            </Flex>
    )
}

export default UserAccountForm;