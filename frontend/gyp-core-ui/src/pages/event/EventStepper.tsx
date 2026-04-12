import { Button, Steps } from "antd";
import React, { useEffect, useRef, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { createErrorNotification, createSuccessNotification } from "../../components/notification/Notification.ts";
import { EventStatus } from "../../models/enums/EventStatus";
import { Mode } from "../../models/enums/Mode";
import {
    EventRequestDto,
    SeatMapResponseDto,
    TicketTypeResponseDto,
    VenueMapResponseDto,
} from "../../models/generated/event-service-models";
import { EventService } from "../../services/Event/EventService.ts";
import { SeatMapService } from "../../services/Event/SeatMapService.ts";
import { TicketTypeService } from "../../services/Event/TicketTypeService.ts";
import EventBasicInformationForm, { EventBasicInformationFormHandle, } from "./steps/EventBasicInformationForm.tsx";
import EventReviewAndPublishForm from "./steps/EventReviewAndPublishForm.tsx";
import EventTicketTypeForm from "./steps/EventTicketTypeForm.tsx";

interface EventStepperProps {
    mode: string;
}

const EventStepper: React.FC<EventStepperProps> = ({mode}) => {
    const [current, setCurrent] = useState(0);
    const [draftEvent, setDraftEvent] = useState<EventRequestDto | null>(null);
    const [draftSeatMap, setDraftSeatMap] = useState<SeatMapResponseDto | null>(null);
    const [availableTicketTypes, setAvailableTicketTypes] = useState<TicketTypeResponseDto[]>([]);
    const [sectionAssignments, setSectionAssignments] = useState<Record<string, string>>({});
    const [isPublishing, setIsPublishing] = useState(false);
    const basicInfoFormRef = useRef<EventBasicInformationFormHandle>(null);
    const navigate = useNavigate();
    const {id} = useParams();

    useEffect(() => {
        void getAllTicketTypes();
    }, []);

    const isCreateMode = mode === Mode.CREATE.key;

    const prev = () => {
        setCurrent(current - 1);
    };

    const next = () => {
        if (current === 0) {
            basicInfoFormRef.current?.submit();
            return;
        }

        if (current === 1) {
            persistAssignmentsAndContinue();
            return;
        }

        if (current < steps.length - 1) {
            setCurrent(current + 1);
        }
    };

    const handleBasicInfoSubmit = (values: EventRequestDto, selectedVenueMap?: VenueMapResponseDto) => {
        setDraftEvent(values);
        if (selectedVenueMap?.seatMapId) {
            void getSeatMapById(selectedVenueMap);
            return;
        }

        setDraftSeatMap(null);
        setSectionAssignments({});
        setCurrent(1);
    };

    const getSeatMapById = async (selectedVenueMap: VenueMapResponseDto) => {
        const seatMapResponse = await SeatMapService.getSeatMapById(selectedVenueMap.seatMapId);
        if (seatMapResponse) {
            setDraftSeatMap(seatMapResponse);
            const nextAssignments: Record<string, string> = {};
            seatMapResponse.seatConfig?.sections?.forEach((section) => {
                nextAssignments[section.id] = section.ticketTypeId || "";
            });
            setSectionAssignments(nextAssignments);
            setCurrent(1);
        } else {
            setDraftSeatMap(null);
            setCurrent(1);
        }
    }

    const getAllTicketTypes = async () => {
        const ticketTypesResponse = await TicketTypeService.getAllTicketTypes();
        if (ticketTypesResponse) {
            setAvailableTicketTypes(ticketTypesResponse);
        } else {
            setAvailableTicketTypes([]);
        }
    }

    const persistAssignmentsAndContinue = () => {
        if (!draftEvent) {
            return;
        }

        setDraftEvent({
            ...draftEvent,
        });
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
        setCurrent(2);
    };

    const handlePublish = async () => {
        if (!draftEvent) {
            createErrorNotification("Event", "Please complete basic information first.");
            return;
        }

        try {
            setIsPublishing(true);
            const payload: EventRequestDto = {
                ...draftEvent,
                status: EventStatus.PUBLISHED.key as EventRequestDto["status"],
            };

            const eventId = id;
            if (mode === Mode.CREATE.key) {
                await EventService.createEvent(payload);
            } else if (eventId) {
                await EventService.updateEvent(eventId, payload);
            }

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
            title: 'Basic Information',
            content: <EventBasicInformationForm
                    ref={basicInfoFormRef}
                    mode={mode}
                    showBackButton={false}
                    showActionButtons={false}
                    isStepperFlow={true}
                    onStepperSubmit={handleBasicInfoSubmit}
            />,
        },
        {
            title: 'Assign Ticket Type',
            content: <EventTicketTypeForm
                    seatMap={draftSeatMap}
                    ticketTypes={availableTicketTypes}
                    assignments={sectionAssignments}
                    onAssignmentsChange={setSectionAssignments}
            />,
        },
        {
            title: 'Review & Publish',
            content: <EventReviewAndPublishForm
                    eventDraft={draftEvent}
                    seatMap={draftSeatMap}
                    ticketTypes={availableTicketTypes}
                    assignments={sectionAssignments}
                    onBack={() => setCurrent(1)}
            />,
        },
    ];

    const items = steps.map((item) => ({key: item.title, title: item.title}));

    const handleStepChange = (step: number) => {
        setCurrent(step);
    }

    const getPrimaryButtonLabel = () => {
        if (current === steps.length - 1) {
            return "Publish";
        }
        return isCreateMode ? "Create" : "Update";
    };

    const handlePrimaryAction = () => {
        if (current === 0) {
            basicInfoFormRef.current?.submit();
            return;
        }

        if (current === 1) {
            persistAssignmentsAndContinue();
            return;
        }

        void handlePublish();
    };

    const canGoPrevious = current > 0;
    const canGoNext = current < steps.length - 1;

    return (
            <div className="flex flex-col h-full">
                <div className="p-3!">
                    <Steps current={current} items={items} onChange={handleStepChange}/>
                </div>

                <div className="flex-1 overflow-auto">
                    {steps[current].content}
                </div>

                <div className="flex items-center justify-between gap-3 border-t border-white/10">
                    <div className="flex items-center gap-2 absolute bottom-1">
                        {canGoPrevious && <Button onClick={prev}>Previous</Button>}
                        {canGoNext && <Button type="default" onClick={next}>Next</Button>}
                    </div>

                    <div className="flex items-center gap-2 absolute bottom-1 right-5">
                        <Button onClick={() => navigate("/event")}>Cancel</Button>
                        <Button type="primary" onClick={handlePrimaryAction} loading={isPublishing}>
                            {getPrimaryButtonLabel()}
                        </Button>
                    </div>
                </div>
            </div>
    );
}

export default EventStepper;