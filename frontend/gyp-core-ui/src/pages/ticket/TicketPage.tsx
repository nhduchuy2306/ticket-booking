import React, { useState } from "react";
import SinglePageLayout from "../../components/layout/singlepage/SinglePageLayout.tsx";
import TicketForm from "./TicketForm.tsx";
import TicketTable from "./TicketTable.tsx";

interface TicketPageProps {
}

const TicketPage: React.FC<TicketPageProps> = () => {
    const [eventId, setEventId] = useState<string | null>(null);
    const [refreshTrigger, setRefreshTrigger] = useState<number>(0);

    const handleShowTicket = (eventId: string) => {
        setEventId(eventId);
        setRefreshTrigger(prev => prev + 1);
    }

    return (
            <div className="bg-white !p-4">
                <SinglePageLayout>
                    <div>
                        <TicketForm onShowTicket={handleShowTicket}/>
                        <TicketTable
                                eventId={eventId || ""}
                                refreshTrigger={refreshTrigger}
                        />
                    </div>
                </SinglePageLayout>
            </div>
    );
}

export default TicketPage;