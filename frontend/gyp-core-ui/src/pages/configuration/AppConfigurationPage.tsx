import { notification, Select } from "antd";
import React, { useEffect, useState } from "react";
import { EventResponseDto } from "../../models/generated/event-service-models";
import { EventService } from "../../services/Event/EventService.ts";

export interface AppConfigurationPageProps {
    collapsed?: boolean;
}

const AppConfigurationPage: React.FC<AppConfigurationPageProps> = () => {
    const [events, setEvents] = useState<EventResponseDto[]>([]);

    useEffect(() => {
        const fetchEvents = async () => {
            try {
                const events = await EventService.getActiveEvents();
                if (events) {
                    setEvents(events);
                } else {
                    setEvents([]);
                }
            } catch (error) {
                notification.error({message: "Failed to fetch events"});
            }
        }
        void fetchEvents();
    }, []);

    return (
            <Select
                    className="!w-full !mr-1"
                    options={events.map(event => ({
                        label: event.name,
                        value: event.id
                    }))}
            />
    );
}

export default AppConfigurationPage;