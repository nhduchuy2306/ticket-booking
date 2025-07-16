import React from "react";
import { DoublePageForm } from "../../components/layout/doulepage/DoublePageForm.tsx";
import DoublePageLayout from "../../components/layout/doulepage/DoublePageLayout.tsx";
import { VenueServiceAdapter } from "../../services/Event/VenueService.ts";
import VenueForm from "./VenueForm.tsx";
import VenueTable from "./VenueTable.tsx";

interface VenuePageProps {
}

const VenuePage: React.FC<VenuePageProps> = () => {
    return (
            <DoublePageLayout>
                <VenueTable/>
                <DoublePageForm
                        service={VenueServiceAdapter}
                        successMessages={{
                            create: "Category created successfully",
                            update: "Category updated successfully"
                        }}
                >
                    {({entity, mode, onSave, onCancel}) => (
                            <VenueForm
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

export default VenuePage;

