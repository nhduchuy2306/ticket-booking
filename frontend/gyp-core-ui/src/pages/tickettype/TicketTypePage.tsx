import React from "react";
import { DoublePageForm } from "../../components/layout/doulepage/DoublePageForm.tsx";
import DoublePageLayout from "../../components/layout/doulepage/DoublePageLayout.tsx";
import { TicketTypeServiceAdapter } from "../../services/Event/TicketTypeService.ts";
import TicketTypeForm from "./TicketTypeForm.tsx";
import TicketTypeTable from "./TicketTypeTable.tsx";

export interface TicketTypePageProps {
}

const TicketTypePage: React.FC<TicketTypePageProps> = () => {
    return (
            <DoublePageLayout>
                <TicketTypeTable/>
                <DoublePageForm
                        service={TicketTypeServiceAdapter}
                        successMessages={{
                            create: "Category created successfully",
                            update: "Category updated successfully"
                        }}
                >
                    {({entity, mode, onSave, onCancel}) => (
                            <TicketTypeForm
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

export default TicketTypePage;