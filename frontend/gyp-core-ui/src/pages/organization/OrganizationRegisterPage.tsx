import React from "react";
import { createErrorNotification, createSuccessNotification } from "../../components/notification/Notification.ts";
import { OrganizationRequestDto } from "../../models/generated/auth-service-models";
import { OrganizationService } from "../../services/Auth/OrganizationService.ts";
import OrganizationForm from "./OrganizationForm.tsx";

const OrganizationRegisterPage: React.FC = () => {
    const handleSave = async (values: OrganizationRequestDto) => {
        try {
            await OrganizationService.registerOrganization(values);
            createSuccessNotification("Organization", "Organization registered successfully");
        } catch (error) {
            const message = error instanceof Error ? error.message : "Failed to register organization";
            createErrorNotification("Organization", message);
        }
    };

    return <OrganizationForm mode="create" onSave={handleSave} onCancel={() => window.history.back()} />;
};

export default OrganizationRegisterPage;

