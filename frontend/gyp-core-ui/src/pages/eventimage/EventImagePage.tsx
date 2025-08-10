import React from "react";
import { DoublePageForm } from "../../components/layout/doulepage/DoublePageForm.tsx";
import DoublePageLayout from "../../components/layout/doulepage/DoublePageLayout.tsx";
import { EventImageServiceAdapter } from "../../services/Event/EventImageService.ts";
import EventImageForm from "./EventImageForm.tsx";
import EventImageTable from "./EventImageTable.tsx";

interface EventImagePageProps {
}

const EventImagePage: React.FC<EventImagePageProps> = () => {
    return (
            <DoublePageLayout>
                <EventImageTable/>
                <DoublePageForm
                        service={EventImageServiceAdapter}
                        successMessages={{
                            create: "Event Image created successfully",
                            update: "Event Image updated successfully"
                        }}
                >
                    {({entity, mode, onSave, onCancel}) => (
                            <EventImageForm
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

export default EventImagePage;