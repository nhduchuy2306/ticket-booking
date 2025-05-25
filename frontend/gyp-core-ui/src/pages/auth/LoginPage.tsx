import { Button, Col, Form, Input, Row, Typography } from "antd";
import React, { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import './login.scss';
import { LoginModel } from "../../models/AuthService/LoginModel.ts";
import { AuthService } from "../../services/Auth/AuthService.ts";

const {Title} = Typography;

const LoginPage: React.FC = () => {
    const [form] = Form.useForm();
    const [loginErrors, setLoginErrors] = useState<string[]>([]);
    const [loading, setLoading] = useState<boolean>(false);
    const navigate = useNavigate();

    const onFinish = async (values: LoginModel) => {
        setLoginErrors([]);
        setLoading(true);
        try {
            const response = await AuthService.login(values);
            if (response.token) {
                localStorage.setItem('token', response.token);
                navigate('/test');
            }
        } catch (error) {
            setLoginErrors(['LoginPage failed. Please try again.']);
        } finally {
            setLoading(false);
        }
    };

    const onFinishFailed = (errorInfo: any) => {
        console.log("Failed:", errorInfo);
    };

    return (
            <div className="login-container">
                <Row justify="center" align="middle" style={{minHeight: '100vh'}}>
                    <Col
                            className="form-item"
                            xs={{span: 24, offset: 0}}
                            lg={{span: 6, offset: 2}}
                            md={{span: 12}}
                            style={{margin: 'auto'}}
                    >
                        <Title level={2} style={{textAlign: 'center'}}>Login</Title>
                        <Form
                                form={form}
                                onFinish={onFinish}
                                onFinishFailed={onFinishFailed}
                                layout="vertical"
                        >
                            {loginErrors.length > 0 && (
                                    <Form.Item>
                                        <Form.ErrorList errors={loginErrors}/>
                                    </Form.Item>
                            )}

                            <Form.Item
                                    className="username"
                                    label="Username"
                                    name="username"
                                    rules={[
                                        {
                                            required: true,
                                            message: "Please input your username!",
                                        },
                                    ]}
                            >
                                <Input placeholder="Username"/>
                            </Form.Item>
                            <Form.Item
                                    className="password"
                                    label="Password"
                                    name="password"
                                    rules={[
                                        {
                                            required: true,
                                            message: "Please input your password!",
                                        },
                                    ]}
                            >
                                <Input.Password placeholder="Password"/>
                            </Form.Item>
                            <Form.Item>
                                <Button
                                        type="primary"
                                        htmlType="submit"
                                        loading={loading}
                                        style={{width: "100%"}}
                                >SIGN IN</Button>
                            </Form.Item>
                            <p className="font-semibold text-muted">
                                Don't have an account?{" "}
                                <Link to="/sign-up" className="text-dark font-bold">
                                    Sign Up
                                </Link>
                            </p>
                        </Form>
                    </Col>
                </Row>
            </div>
    );
}

export default LoginPage
