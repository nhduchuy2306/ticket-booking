import { Button, Flex, Typography } from "antd";
import React from "react";
import { RiLoopLeftFill } from "react-icons/ri";
import { UserAccountService } from "../../services/Auth/UserAccountService.ts";

interface ConfigurationPageProps {
    // Define any props if needed
}

const ConfigurationPage: React.FC<ConfigurationPageProps> = () => {
    const syncOrganizer = async () => {
        await UserAccountService.syncOrganizer();
    }

    return (
            <Flex vertical={true} className="w-full h-full bg-white !p-4">
                <h1 className="text-2xl font-bold !mb-[20px]">Configuration</h1>
                <Flex vertical={false} className="gap-4" align="center">
                    <Typography.Title level={5}>Sync Organizer: </Typography.Title>
                    <Button
                            icon={<RiLoopLeftFill/>}
                            type="default"
                            onClick={syncOrganizer}
                    />
                </Flex>
            </Flex>
    );
}

export default ConfigurationPage;