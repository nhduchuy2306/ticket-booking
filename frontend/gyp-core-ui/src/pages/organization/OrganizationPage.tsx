import React from "react";
import { DoublePageForm } from "../../components/layout/doulepage/DoublePageForm.tsx";
import DoublePageLayout from "../../components/layout/doulepage/DoublePageLayout.tsx";
import { OrganizationServiceAdapter } from "../../services/Auth/OrganizationService.ts";
import OrganizationForm from "./OrganizationForm.tsx";
import OrganizationTable from "./OrganizationTable.tsx";

export interface OrganizationPageProps {
}

const OrganizationPage: React.FC<OrganizationPageProps> = () => {
    return (
            <DoublePageLayout>
                <OrganizationTable/>
                <DoublePageForm
                        service={OrganizationServiceAdapter}
                        successMessages={{
                            create: "Category created successfully",
                            update: "Category updated successfully"
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