import React from "react";
import { useNavigate } from "react-router-dom";
import SinglePageLayout from "../../components/layout/singlepage/SinglePageLayout.tsx";
import { EventResponseDto } from "../../models/generated/event-service-models";
import EventTable from "./EventTable.tsx";

interface EventPageProps {
}

const EventPage: React.FC<EventPageProps> = () => {
    const navigate = useNavigate();

    const handleNavigate = (path: string, entity?: EventResponseDto) => {
        if (path === '/create') {
            navigate('/event/create');
        } else if (path === '/edit') {
            navigate(`/event/edit/${entity?.id}`);
        } else if (path === '/view') {
            navigate(`/event/view/${entity?.id}`);
        } else {
            navigate('/event');
        }
    };

    return (
            <SinglePageLayout onNavigate={handleNavigate}>
                <EventTable/>
            </SinglePageLayout>
    );
};

export default EventPage;