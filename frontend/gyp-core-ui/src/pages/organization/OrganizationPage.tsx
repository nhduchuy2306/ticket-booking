import React from "react";
import { DoublePageForm } from "../../components/layout/doulepage/DoublePageForm.tsx";
import DoublePageLayout from "../../components/layout/doulepage/DoublePageLayout.tsx";
import { OrganizationServiceAdapter } from "../../services/Auth/OrganizationService.ts";
import OrganizationForm from "./OrganizationForm.tsx";
import OrganizationTable from "./OrganizationTable.tsx";

const OrganizationPage: React.FC = () => {
    return (
            <DoublePageLayout>
                <OrganizationTable/>
                <DoublePageForm
                        service={OrganizationServiceAdapter}
                        successMessages={{
                            create: "Organization created successfully",
                            update: "Organization updated successfully"
                        }}
                >
                    {({entity, mode, onSave, onCancel}) => (
                            <OrganizationForm
                                    entity={entity}
                                    mode={mode}
                                    onSave={onSave}
                                    onCancel={onCancel}
                            />
                    )}
                </DoublePageForm>
            </DoublePageLayout>
    );
}

export default OrganizationPage;