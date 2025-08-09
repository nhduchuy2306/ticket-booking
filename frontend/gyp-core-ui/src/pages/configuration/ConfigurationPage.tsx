import { Avatar, Badge, Button, Card, Divider, Flex, Select, Space, Switch, Typography } from 'antd';
import React, { useState } from 'react';
import {
    RiBellLine,
    RiDatabaseLine,
    RiGlobalLine,
    RiLoopLeftFill,
    RiMoonFill,
    RiRefreshLine,
    RiShieldCheckLine,
    RiSunFill,
    RiTicketLine,
    RiUserSettingsLine
} from 'react-icons/ri';
import { createSuccessNotification } from "../../components/notification/Notification.ts";

const {Title, Text} = Typography;
const {Option} = Select;

interface ConfigCardProps {
    title: string;
    description: string;
    children?: React.ReactNode;
    icon: React.ReactNode;
    badge?: boolean;
}

const ConfigurationPage: React.FC = () => {
    const [darkMode, setDarkMode] = useState(false);
    const [notifications, setNotifications] = useState(true);
    const [autoSync, setAutoSync] = useState(false);
    const [language, setLanguage] = useState('en');
    const [syncInterval, setSyncInterval] = useState('15');
    const [loading, setLoading] = useState({
        organizer: false,
        ticketType: false,
        database: false
    });

    const handleSync = async (syncType: string) => {
        setLoading(prev => ({...prev, [syncType]: true}));

        // Simulate API call
        setTimeout(() => {
            setLoading(prev => ({...prev, [syncType]: false}));
            createSuccessNotification(
                    'Sync Complete',
                    `${syncType.charAt(0).toUpperCase() + syncType.slice(1)} has been synchronized successfully.`
            )
        }, 2000);
    };

    const ConfigCard = ({title, description, children, icon, badge}: ConfigCardProps) => (
            <Card className="mb-4 shadow-sm hover:shadow-md transition-shadow duration-300 p-5 w-full">
                <div className="flex items-start gap-3">
                    <div className="flex-shrink-0">
                        {badge ? (
                                <Badge dot color="#52c41a">
                                    <Avatar
                                            icon={icon}
                                            className="bg-blue-100 text-blue-600"
                                            size="large"
                                    />
                                </Badge>
                        ) : (
                                <Avatar
                                        icon={icon}
                                        className="bg-blue-100 text-blue-600"
                                        size="large"
                                />
                        )}
                    </div>
                    <div className="flex-1">
                        <Title level={5} className="mb-1">{title}</Title>
                        <Text type="secondary" className="text-sm block mb-3">
                            {description}
                        </Text>
                        {children}
                    </div>
                </div>
            </Card>
    );

    return (
            <div className="bg-white !p-6 overflow-auto h-full w-full">
                <div className="!w-full">
                    <div className="mb-8">
                        <Title level={1} className="mb-2">
                            <RiUserSettingsLine className="inline mr-3 text-blue-600"/>
                            Configuration
                        </Title>
                        <Text type="secondary" className="text-base">
                            Manage your application settings and preferences
                        </Text>
                    </div>

                    <div className="flex flex-col gap-4 justify-center items-center !w-full !mb-5">
                        <ConfigCard
                                title="Data Synchronization"
                                description="Keep your data up-to-date with external sources"
                                icon={<RiRefreshLine/>}
                                badge={autoSync}
                        >
                            <Space direction="vertical" className="w-full" size="middle">
                                <Flex justify="space-between" align="center">
                                    <strong>Sync Organizer Data</strong>
                                    <Button
                                            icon={<RiLoopLeftFill/>}
                                            loading={loading.organizer}
                                            onClick={() => handleSync('organizer')}
                                            type="primary"
                                    >
                                        Sync Now
                                    </Button>
                                </Flex>

                                <Flex justify="space-between" align="center">
                                    <strong>Sync Ticket Types</strong>
                                    <Button
                                            icon={<RiTicketLine/>}
                                            loading={loading.ticketType}
                                            onClick={() => handleSync('ticketType')}
                                            type="primary"
                                    >
                                        Sync Now
                                    </Button>
                                </Flex>

                                <Flex justify="space-between" align="center">
                                    <strong>Database Backup</strong>
                                    <Button
                                            icon={<RiDatabaseLine/>}
                                            loading={loading.database}
                                            onClick={() => handleSync('database')}
                                            type="primary"
                                    >
                                        Backup Now
                                    </Button>
                                </Flex>

                                <Divider className="my-3"/>

                                <Flex justify="space-between" align="center">
                                    <div>
                                        <Text strong>Auto Sync</Text>
                                        <br/>
                                        <Text type="secondary" className="text-xs">
                                            Enable automatic synchronization
                                        </Text>
                                    </div>
                                    <Switch
                                            checked={autoSync}
                                            onChange={setAutoSync}
                                            checkedChildren="ON"
                                            unCheckedChildren="OFF"
                                    />
                                </Flex>

                                {autoSync && (
                                        <Flex justify="space-between" align="center">
                                            <Text>Sync Interval</Text>
                                            <Select
                                                    value={syncInterval}
                                                    onChange={setSyncInterval}
                                                    style={{width: 120}}
                                            >
                                                <Option value="5">5 minutes</Option>
                                                <Option value="15">15 minutes</Option>
                                                <Option value="30">30 minutes</Option>
                                                <Option value="60">1 hour</Option>
                                            </Select>
                                        </Flex>
                                )}
                            </Space>
                        </ConfigCard>

                        <ConfigCard
                                title="Appearance"
                                description="Customize the look and feel of your application"
                                icon={darkMode ? <RiMoonFill/> : <RiSunFill/>}
                        >
                            <Space direction="vertical" className="w-full" size="middle">
                                <Flex justify="space-between" align="center">
                                    <div>
                                        <Text strong>Dark Mode</Text>
                                        <br/>
                                        <Text type="secondary" className="text-xs">
                                            Switch between light and dark themes
                                        </Text>
                                    </div>
                                    <Switch
                                            checked={darkMode}
                                            onChange={setDarkMode}
                                            checkedChildren={<RiMoonFill/>}
                                            unCheckedChildren={<RiSunFill/>}
                                    />
                                </Flex>

                                <Flex justify="space-between" align="center">
                                    <Text>Language</Text>
                                    <Select
                                            value={language}
                                            onChange={setLanguage}
                                            style={{width: 120}}
                                            suffixIcon={<RiGlobalLine/>}
                                    >
                                        <Option value="en">English</Option>
                                        <Option value="es">Spanish</Option>
                                        <Option value="fr">French</Option>
                                        <Option value="de">German</Option>
                                    </Select>
                                </Flex>
                            </Space>
                        </ConfigCard>

                        <ConfigCard
                                title="Notifications"
                                description="Control how you receive updates and alerts"
                                icon={<RiBellLine/>}
                                badge={notifications}
                        >
                            <Space direction="vertical" className="w-full" size="middle">
                                <Flex justify="space-between" align="center">
                                    <div>
                                        <Text strong>Push Notifications</Text>
                                        <br/>
                                        <Text type="secondary" className="text-xs">
                                            Receive real-time updates
                                        </Text>
                                    </div>
                                    <Switch
                                            checked={notifications}
                                            onChange={setNotifications}
                                            checkedChildren="ON"
                                            unCheckedChildren="OFF"
                                    />
                                </Flex>

                                <Flex justify="space-between" align="center">
                                    <Text>Email Notifications</Text>
                                    <Switch defaultChecked/>
                                </Flex>

                                <Flex justify="space-between" align="center">
                                    <Text>SMS Alerts</Text>
                                    <Switch/>
                                </Flex>
                            </Space>
                        </ConfigCard>

                        <ConfigCard
                                title="Security"
                                description="Manage your account security and privacy"
                                icon={<RiShieldCheckLine/>}
                        >
                            <Space direction="vertical" className="w-full" size="middle">
                                <Flex justify="space-between" align="center">
                                    <div>
                                        <Text strong>Two-Factor Authentication</Text>
                                        <br/>
                                        <Text type="secondary" className="text-xs">
                                            Add extra security to your account
                                        </Text>
                                    </div>
                                    <Switch/>
                                </Flex>

                                <Flex justify="space-between" align="center">
                                    <Text>Session Timeout</Text>
                                    <Select defaultValue="30" style={{width: 120}}>
                                        <Option value="15">15 minutes</Option>
                                        <Option value="30">30 minutes</Option>
                                        <Option value="60">1 hour</Option>
                                        <Option value="0">Never</Option>
                                    </Select>
                                </Flex>

                                <Button type="link" className="p-0 text-left">
                                    Change Password
                                </Button>
                            </Space>
                        </ConfigCard>

                        <ConfigCard
                                title="Advanced"
                                description="Developer and advanced user settings"
                                icon={<RiDatabaseLine/>}
                        >
                            <Space direction="vertical" className="w-full" size="middle">
                                <Flex justify="space-between" align="center">
                                    <Text>Debug Mode</Text>
                                    <Switch/>
                                </Flex>

                                <Flex justify="space-between" align="center">
                                    <Text>Cache Management</Text>
                                    <Button size="small">Clear Cache</Button>
                                </Flex>

                                <Button type="link" className="p-0 text-left" danger>
                                    Reset All Settings
                                </Button>
                            </Space>
                        </ConfigCard>
                    </div>
                </div>
            </div>
    );
};

export default ConfigurationPage;