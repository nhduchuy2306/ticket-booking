import { Card, Descriptions, List } from "antd";
import React, { useMemo } from "react";
import SeatMapViewer from "../../../components/seat-map/seat-map-viewer/SeatMapViewer";
import {
    EventResponseDto,
    SeatMapResponseDto,
    TicketTypeResponseDto,
} from "../../../models/generated/event-service-models";

interface EventReviewAndPublishFormProps {
    eventDraft?: EventResponseDto | null;
    seatMap?: SeatMapResponseDto | null;
    ticketTypes: TicketTypeResponseDto[];
    assignments: Record<string, string>;
    onBack?: () => void;
}

const EventReviewAndPublishForm: React.FC<EventReviewAndPublishFormProps> = ({
                                                                                 eventDraft,
                                                                                 seatMap,
                                                                                 ticketTypes,
                                                                                 assignments,
                                                                                 onBack,
                                                                             }) => {
    const sections = seatMap?.seatConfig?.sections || [];

    const assignedSections = useMemo(() => sections.map((section) => ({
        ...section,
        ticketTypeId: assignments[section.id] || section.ticketTypeId || "",
    })), [assignments, sections]);

    const previewVenueMap = useMemo(() => {
        if (!seatMap) {
            return null;
        }

        return {
            ...seatMap,
            seatConfig: {
                ...(seatMap.seatConfig || {}),
                sections: assignedSections,
            },
        } as SeatMapResponseDto;
    }, [assignedSections, seatMap]);

    const findTicketTypeName = (ticketTypeId?: string) => {
        return ticketTypes.find((item) => item.id === ticketTypeId)?.name || ticketTypeId || "Not Assigned";
    }

    return (
            <div className="grid grid-cols-1 gap-4 xl:grid-cols-2">
                <div className="space-y-4!">
                    <Card className="!border-[#3a3a3a] !bg-[#252525] text-white p-5!" styles={{body: {padding: 20}}}>
                        <h2 className="text-[18px] font-semibold text-white">Event Information</h2>
                        <Descriptions column={1} size="small" className="mt-4!"
                                      contentStyle={{color: "#d4d4d4"}}
                                      labelStyle={{color: "#d4d4d4", fontWeight: 500}}
                                      items={[
                                          {key: "name", label: "Name of event", children: eventDraft?.name || "-"},
                                          {key: "status", label: "Status", children: eventDraft?.status || "-"},
                                          {
                                              key: "venueName",
                                              label: "Venue name",
                                              children: eventDraft?.venueMap?.name || "-",
                                          },
                                          {
                                              key: "seasonName",
                                              label: "Season name",
                                              children: eventDraft?.season?.name || "-"
                                          },
                                      ]}
                        />
                    </Card>

                    <Card className="!border-[#3a3a3a] !bg-[#252525] !text-white !p-5" styles={{body: {padding: 20}}}>
                        <h2 className="text-[18px] font-semibold text-white">Assign ticket type</h2>
                        <List
                                className="mt-4!"
                                dataSource={sections}
                                renderItem={(section) => (
                                        <List.Item className="!border-white/10 text-white">
                                            <div className="flex w-full items-center justify-between gap-3">
                                                <div>
                                                    <div className="font-semibold text-white">{section.name}</div>
                                                    <div className="text-xs text-slate-400">{section.type}</div>
                                                </div>
                                                <div className="text-right text-sm text-slate-200">
                                                    {findTicketTypeName(assignments[section.id] || section.ticketTypeId)}
                                                </div>
                                            </div>
                                        </List.Item>
                                )}
                        />
                    </Card>
                </div>

                <Card className="!border-[#3a3a3a] !bg-[#252525] text-white" styles={{body: {padding: 20}}}>
                    <h2 className="text-[18px] font-semibold text-white">Seat Map Viewer</h2>
                    <div className="mt-4 h-[760px] overflow-hidden rounded-2xl bg-[#1f1f1f]">
                        {previewVenueMap ? (
                                <SeatMapViewer
                                        venueMap={previewVenueMap}
                                        title={eventDraft?.name}
                                        onBack={onBack}
                                        eventId={eventDraft?.id}
                                        showUiFunctionality={false}
                                />
                        ) : (
                                <div className="flex h-full items-center justify-center text-sm text-slate-400">
                                    Not able to load seat map preview. Please go back and make sure you have selected a
                                    seat map.
                                </div>
                        )}
                    </div>
                </Card>
            </div>
    );
};

export default EventReviewAndPublishForm;


