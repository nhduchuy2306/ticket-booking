import { Button, Steps } from "antd";
import React, { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { createErrorNotification, createSuccessNotification } from "../../components/notification/Notification.ts";
import { Mode } from "../../models/enums/Mode.ts";
import {
    EventResponseDto,
    EventSectionMappingListRequestDto,
    SeatMapResponseDto,
} from "../../models/generated/event-service-models";
import { EventSectionMappingService } from "../../services/Event/EventSectionMappingService.ts";
import { EventService } from "../../services/Event/EventService.ts";
import { SeatMapService } from "../../services/Event/SeatMapService.ts";
import EventForm from "./EventForm.tsx";
import EventReviewAndPublishForm from "./steps/EventReviewAndPublishForm.tsx";
import EventTicketTypeForm from "./steps/EventTicketTypeForm.tsx";

const EventAssignForm: React.FC = () => {
    const [current, setCurrent] = useState(0);
    const [savedEvent, setSavedEvent] = useState<EventResponseDto | null>(null);
    const [draftSeatMap, setDraftSeatMap] = useState<SeatMapResponseDto | null>(null);
    const [sectionAssignments, setSectionAssignments] = useState<Record<string, string>>({});
    const [eventSectionMappings, setEventSectionMappings] = useState<Record<string, string>>({});
    const [isPublishing, setIsPublishing] = useState(false);
    const [isLoadingContext, setIsLoadingContext] = useState(false);
    const navigate = useNavigate();
    const {id} = useParams();

    useEffect(() => {
        if (!id) return;
        void loadEventContext(id);
    }, [id]);

    const loadEventContext = async (eventId: string) => {
        try {
            setIsLoadingContext(true);
            const eventResponse = await EventService.getEventById(eventId);
            setSavedEvent(eventResponse || null);

            const seatMapId = eventResponse?.venueMap?.seatMapId;
            if (!seatMapId) {
                setDraftSeatMap(null);
                setSectionAssignments({});
                return;
            }

            const seatMapResponse = await SeatMapService.getSeatMapById(seatMapId);
            setDraftSeatMap(seatMapResponse || null);

            const nextAssignments: Record<string, string> = {};
            seatMapResponse?.seatConfig?.sections?.forEach((section) => {
                nextAssignments[section.id] = section.ticketTypeId || "";
            });
            setSectionAssignments(nextAssignments);

            const eventSectionMappingResponse = await EventSectionMappingService.getAllEventSectionMappingsByEventId(eventId);
            const nextEventSectionMappings: Record<string, string> = {};
            eventSectionMappingResponse.forEach((mapping) => {
                nextEventSectionMappings[mapping.sectionId] = mapping.ticketTypeId;
            });
            setEventSectionMappings(nextEventSectionMappings);
        } catch (error) {
            console.error("Failed to load event context:", error);
            setSavedEvent(null);
            setDraftSeatMap(null);
            setSectionAssignments({});
        } finally {
            setIsLoadingContext(false);
        }
    };

    const prev = () => setCurrent((value) => Math.max(0, value - 1));

    const next = () => setCurrent((value) => Math.min(2, value + 1));

    const persistAssignmentsAndContinue = () => {
        if (draftSeatMap) {
            setDraftSeatMap({
                ...draftSeatMap,
                seatConfig: {
                    ...(draftSeatMap.seatConfig || {}),
                    sections: (draftSeatMap.seatConfig?.sections || []).map((section) => ({
                        ...section,
                        ticketTypeId: sectionAssignments[section.id] || section.ticketTypeId || "",
                    })),
                },
            });
        }
        void persistSeatMap();
    };

    const persistSeatMap = async () => {
        const body: EventSectionMappingListRequestDto = {
            eventSectionMappingRequestDtos: Object.entries(sectionAssignments).map(([sectionId, ticketTypeId]) => ({
                eventId: id,
                sectionId: sectionId,
                ticketTypeId: ticketTypeId,
                seatMapId: draftSeatMap?.id || "",
            }))
        };
        await EventSectionMappingService.updateEventSectionMappings(body);
        next();
    }

    const handlePublish = async () => {
        if (!savedEvent?.id) {
            createErrorNotification("Event", "Event id is missing.");
            return;
        }

        try {
            setIsPublishing(true);
            await EventService.publishEvent(savedEvent.id);
            createSuccessNotification("Event", "Event published successfully");
            navigate("/event");
        } catch (error) {
            console.error("Publish failed:", error);
            createErrorNotification("Event", "Failed to publish event. Please try again.");
        } finally {
            setIsPublishing(false);
        }
    };

    const steps = [
        {
            title: "General Information",
            content: <EventForm mode={Mode.READ_ONLY.key} showBackButton={false}/>,
        },
        {
            title: "Assign Ticket Type",
            content: <EventTicketTypeForm
                    seatMap={draftSeatMap}
                    ticketTypes={savedEvent?.ticketTypes || []}
                    assignments={sectionAssignments}
                    eventSectionMappings={eventSectionMappings}
                    onAssignmentsChange={setSectionAssignments}
            />,
        },
        {
            title: "Review & Publish",
            content: <EventReviewAndPublishForm
                    eventDraft={savedEvent}
                    seatMap={draftSeatMap}
                    ticketTypes={savedEvent?.ticketTypes || []}
                    assignments={sectionAssignments}
                    onBack={() => setCurrent(1)}
            />,
        },
    ];

    return (
            <div className="flex h-full flex-col">
                <div className="p-3!">
                    <Steps current={current} items={steps.map((item) => ({key: item.title, title: item.title}))}/>
                </div>

                <div className="flex-1 overflow-auto">
                    {isLoadingContext ? null : steps[current].content}
                </div>

                <div className="flex items-center justify-between gap-3 border-t border-white/10">
                    <div className="flex items-center gap-2 absolute bottom-1 right-5">
                        <Button onClick={() => navigate("/event")}>Cancel</Button>
                        {current === 1 && <Button type="primary" onClick={persistAssignmentsAndContinue}>Save</Button>}
                        {current > 0 && <Button onClick={prev}>Previous</Button>}
                        {current < 2 && <Button type="default" onClick={next}>Next</Button>}
                        {current === 2 &&
                            <Button type="primary" onClick={handlePublish} loading={isPublishing}>Publish</Button>
                        }
                    </div>
                </div>
            </div>
    );
};

export default EventAssignForm;