import React from "react";
import SinglePageLayout from "../../components/layout/singlepage/SinglePageLayout.tsx";
import TicketForm from "./TicketForm.tsx";
import TicketTable from "./TicketTable.tsx";

interface TicketPageProps {
}

const TicketPage: React.FC<TicketPageProps> = () => {
    return (
            <div className="bg-white !p-4">
                <SinglePageLayout>
                    <div>
                        <TicketForm/>
                        <TicketTable/>
                    </div>
                </SinglePageLayout>
            </div>
    );
}

export default TicketPage;