import { useEffect, useState } from "react";
import { createErrorNotification } from "../../components/notification/Notification.ts";
import { EventResponseDto, SeatMapResponseDto, VenueResponseDto } from "../../models/generated/event-service-models";
import { EventService } from "../../services/Event/EventService.ts";
import { SeatMapService } from "../../services/Event/SeatMapService.ts";
import { VenueService } from "../../services/Event/VenueService.ts";

interface useEventDataProps {
    id?: string;
    tenantOrganizationId?: string | null;
}

export const useEventData = ({id, tenantOrganizationId}: useEventDataProps) => {
    const [event, setEvent] = useState<EventResponseDto>();
    const [isLoading, setIsLoading] = useState(false);
    const [venue, setVenue] = useState<VenueResponseDto>();
    const [seatMap, setSeatMap] = useState<SeatMapResponseDto>();

    useEffect(() => {
        const fetchData = async () => {
            try {
                setIsLoading(true);
                const eventResponse = await EventService.getEventById(id || "");
                if (eventResponse && (!tenantOrganizationId || eventResponse.organizationId === tenantOrganizationId)) {
                    setEvent(eventResponse);

                    const venueId = eventResponse.venueMap?.venueId;
                    const venueResponse = await VenueService.getVenueById(venueId || "");
                    if (venueResponse) {
                        setVenue(venueResponse);
                    }

                    const seatMapId = eventResponse.venueMap?.seatMapId;
                    const seatMapResponse = await SeatMapService.getSeatMapById(seatMapId || "");
                    if (seatMapResponse) {
                        setSeatMap(seatMapResponse);
                    }
                } else if (eventResponse && tenantOrganizationId && eventResponse.organizationId !== tenantOrganizationId) {
                    createErrorNotification("Access denied", "The requested event does not belong to this ticket shop.");
                }
            } catch {
                createErrorNotification("Failed to fetch data");
            } finally {
                setIsLoading(false);
            }
        }

        if (id) {
            void fetchData();
        }
    }, [id, tenantOrganizationId]);

    return {event, venue, seatMap, isLoading};
}