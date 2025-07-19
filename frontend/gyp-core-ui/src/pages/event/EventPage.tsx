import React from "react";
import { useNavigate } from "react-router-dom";
import SinglePageLayout from "../../components/layout/singlepage/SinglePageLayout.tsx";
import { EventResponseDto } from "../../models/generated/event-service-models";
import { EventService } from "../../services/Event/EventService.ts";
import EventTable from "./EventTable.tsx";

interface EventPageProps {
}

const EventPage: React.FC<EventPageProps> = () => {
    const navigate = useNavigate();

    return (
            <div className="bg-white">
                <SinglePageLayout onNavigate={(path: string, entity?: EventResponseDto) =>
                        EventService.navigate(navigate, path, entity)}>
                    <EventTable/>
                </SinglePageLayout>
            </div>
    );
};

export default EventPage;