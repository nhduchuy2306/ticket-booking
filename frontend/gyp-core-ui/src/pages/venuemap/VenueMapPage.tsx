import React from "react";
import { DoublePageForm } from "../../components/layout/doulepage/DoublePageForm.tsx";
import DoublePageLayout from "../../components/layout/doulepage/DoublePageLayout.tsx";
import { VenueMapServiceAdapter } from "../../services/Event/VenueMapService.ts";
import VenueMapForm from "./VenueMapForm.tsx";
import VenueMapTable from "./VenueMapTable.tsx";

interface VenueMapPageProps {

}

const VenueMapPage: React.FC<VenueMapPageProps> = () => {
    return (
            <DoublePageLayout>
                <VenueMapTable/>
                <DoublePageForm
                        service={VenueMapServiceAdapter}
                        successMessages={{
                            create: "Venue map created successfully",
                            update: "Venue map updated successfully"
                        }}
                >
                    {({entity, mode, onSave, onCancel}) => (
                            <VenueMapForm
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

export default VenueMapPage;