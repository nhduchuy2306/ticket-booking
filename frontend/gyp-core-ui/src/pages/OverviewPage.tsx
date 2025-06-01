import { Flex, Typography } from "antd";
import React from "react";

interface OverviewPageProps {
    // Define any props if needed in the future
}

const OverviewPage: React.FC<OverviewPageProps> = () => {
    return (
            <Flex vertical={true} align="center" justify="center" className="w-full h-full bg-white">
                <Typography.Title level={2}>Ticketing Admin Overview</Typography.Title>
                <Typography.Text type="success" className="!text-xl">
                    Welcome to the Ticketing Admin Configuration page. Here you can manage users, groups, events, and
                    system settings.
                </Typography.Text>
                <div className="mt-[24px] !text-gray-800 text-xl">
                    <em>Dashboard and configuration shortcuts will appear here.</em>
                </div>
            </Flex>
    );
};

export default OverviewPage;