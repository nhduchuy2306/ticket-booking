import { Button, Form, Input, notification } from "antd";
import React, { useEffect, useState } from "react";
import { UserAccountResponseDto } from "../../models/generated/auth-service-models";
import { UserAccountService } from "../../services/Auth/UserAccountService.ts";
import { DateUtils } from "../../utils/DateUtils.ts";

const ProfileDetailPage: React.FC = () => {
    const [form] = Form.useForm();
    const [isLoading, setIsLoading] = useState<boolean>(false);
    const [data, setData] = useState<UserAccountResponseDto | null>(null);
    const id = localStorage.getItem("userId") || "";

    useEffect(() => {
        const fetchData = async () => {
            setIsLoading(true);
            try {
                if (id) {
                    const response = await UserAccountService.getUserAccountById(id);
                    if (response) {
                        setData(response);
                    }
                } else {
                    notification.error({message: "User ID not found"});
                }
            } catch (error) {
                console.error("Error fetching data:", error);
                notification.error({message: "Failed to fetch data"});
            } finally {
                setIsLoading(false);
            }
        };
        void fetchData();
    }, []);

    useEffect(() => {
        if (data) {
            form.setFieldsValue({
                id: data.id,
                name: data.name,
                username: data.username,
                dob: data.dob ? DateUtils.formatDate(data.dob) : undefined,
                phoneNumber: data.phoneNumber,
                email: data.email,
                organizationId: data.organizationId,
            });
        }
    }, [data, form]);

    const handleSave = async () => {
        try {
            const validatedValues = await form.validateFields();
            const adaptedValues = {
                ...validatedValues,
                dob: validatedValues.dob ? DateUtils.toIsoDateTime(validatedValues.dob) : null,
                userGroupList: []
            };
            await UserAccountService.updateUserAccount(adaptedValues, id);
            notification.success({message: "Profile updated successfully"});
        } catch (error) {
            if (error instanceof Error) {
                notification.error({message: error.message});
            } else {
                notification.error({message: "Failed to update profile"});
            }
        }
    };

    return (
            <div className="w-full flex items-center justify-center">
                <div className="bg-white !p-4 w-1/2">
                    <Form
                            form={form}
                            layout="vertical"
                            onFinish={handleSave}
                    >
                        <Form.Item name="id">
                            <Input disabled={true}/>
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
                                name="password"
                                label="Password"
                                rules={[{required: true, message: "Please input your password!"}]}>
                            <Input.Password placeholder="Password" visibilityToggle={false}/>
                        </Form.Item>

                        <Form.Item name="dob" label="Date of Birth">
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

                        <Form.Item
                                name="organizationId"
                                label="Organization"
                                rules={[{required: true, message: "Please select an organization!"}]}>
                            <Input disabled={true}/>
                        </Form.Item>

                        <div className="flex justify-end items-center gap-1.5">
                            <Button type="primary" htmlType="submit" loading={isLoading}>Update</Button>
                        </div>
                    </Form>
                </div>
            </div>
    );
}

export default ProfileDetailPage;