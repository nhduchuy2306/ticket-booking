import React from "react";
import { DoublePageForm } from "../../components/layout/doulepage/DoublePageForm.tsx";
import DoublePageLayout from "../../components/layout/doulepage/DoublePageLayout.tsx";
import { SaleChannelServiceAdapter } from "../../services/SaleChannel/SaleChannelService.ts";
import SaleChannelForm from "./SaleChannelForm.tsx";
import SaleChannelTable from "./SaleChannelTable.tsx";

interface SaleChannelPageProps {
}

const SaleChannelPage: React.FC<SaleChannelPageProps> = () => {
    return (
            <DoublePageLayout>
                <SaleChannelTable/>
                <DoublePageForm
                        service={SaleChannelServiceAdapter}
                        successMessages={{
                            create: "Sale Channel created successfully",
                            update: "Sale Channel updated successfully"
                        }}
                >
                    {({entity, mode, onSave, onCancel}) => (
                            <SaleChannelForm
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

export default SaleChannelPage;