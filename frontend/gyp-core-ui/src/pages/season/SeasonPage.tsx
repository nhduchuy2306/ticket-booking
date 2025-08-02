import React from "react";
import { DoublePageForm } from "../../components/layout/doulepage/DoublePageForm.tsx";
import DoublePageLayout from "../../components/layout/doulepage/DoublePageLayout.tsx";
import { SeasonServiceAdapter } from "../../services/Event/SeasonService.ts";
import SeasonForm from "./SeasonForm.tsx";
import SeasonTable from "./SeasonTable.tsx";

export interface SeasonPageProps {
}

const SeasonPage: React.FC<SeasonPageProps> = () => {
    return (
            <DoublePageLayout>
                <SeasonTable/>
                <DoublePageForm
                        service={SeasonServiceAdapter}
                        successMessages={{
                            create: "Category created successfully",
                            update: "Category updated successfully"
                        }}
                >
                    {({entity, mode, onSave, onCancel}) => (
                            <SeasonForm
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

export default SeasonPage;