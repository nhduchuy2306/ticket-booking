import { Spin } from "antd";
import React, { useEffect, useMemo, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import SeatMapViewer from "../../components/seat-map/seat-map-viewer/SeatMapViewer.tsx";
import { useEventData } from "../../hooks/form/useEventData.tsx";
import { SeatAvailabilityDto } from "../../models/booking/SeatHoldModels.ts";
import { Row, Seat, SeatConfig, Section } from "../../models/generated/event-service-models";
import { SeatMapService } from "../../services/Event/SeatMapService.ts";
import { hasValidWaitingRoomClearance } from "../../utils/bookingSession.ts";

const cloneSeatConfigWithAvailability = (seatConfig: SeatConfig, availabilityMap: Map<string, SeatAvailabilityDto>) => {
    if (!seatConfig?.sections) {
        return seatConfig;
    }

    const buildSeatKey = (sectionId?: string, rowId?: string, seatId?: string) => {
        if (!sectionId || !rowId || !seatId) {
            return seatId || "";
        }

        return `${sectionId}-${rowId}-${seatId}`;
    };

    const patchSeat = (seat: Seat, sectionId?: string, rowId?: string) => {
        if (!seat?.id) {
            return seat;
        }

        const availability = availabilityMap.get(buildSeatKey(sectionId, rowId, String(seat.id)))
                || availabilityMap.get(String(seat.id));
        if (!availability) {
            return seat;
        }

        return {
            ...seat,
            status: availability.status || seat.status,
            holdToken: availability.holdToken,
            holdExpiresAt: availability.holdExpiresAt,
            available: availability.available,
        };
    };

    const patchRow = (row: Row) => ({
        ...row,
        seats: row?.seats?.map((seat: Seat) => patchSeat(seat, undefined, row?.id)) || [],
    });

    return {
        ...seatConfig,
        sections: seatConfig.sections.map((section: Section) => ({
            ...section,
            rows: section?.rows?.map((row: Row) => ({
                ...patchRow(row),
                seats: row?.seats?.map((seat: Seat) => patchSeat(seat, section?.id, row?.id)) || [],
            })) || [],
        })),
    };
};

const EventSeatMap: React.FC = () => {
    const {id} = useParams();
    const hasClearance = hasValidWaitingRoomClearance(id);
    const {event, seatMap, isLoading} = useEventData({id});
    const navigate = useNavigate();
    const [seatAvailability, setSeatAvailability] = useState<SeatAvailabilityDto[]>([]);

    useEffect(() => {
        if (!id) {
            navigate("/gyp/", {replace: true});
            return;
        }

        if (!hasClearance) {
            navigate(`/gyp/events/${id}/waiting-room?next=${encodeURIComponent(`/gyp/events/${id}/choose-seats`)}`, {replace: true});
        }
    }, [hasClearance, id, navigate]);

    useEffect(() => {
        const loadAvailability = async () => {
            if (!event?.id || !hasClearance) {
                return;
            }

            try {
                const response = await SeatMapService.getSeatAvailability(event.id);
                setSeatAvailability(response || []);
            } catch {
                setSeatAvailability([]);
            }
        };

        void loadAvailability();
    }, [event?.id, hasClearance]);

    if (!hasClearance) {
        return (
                <div className="flex flex-col items-center justify-center w-full h-full">
                    <Spin size="large" className="!mt-20"/>
                </div>
        );
    }

    const mergedVenueMap = useMemo(() => {
        if (!seatMap) {
            return undefined;
        }

        const availabilityMap = new Map<string, SeatAvailabilityDto>();
        seatAvailability.forEach((item) => {
            if (item.seatKey) {
                availabilityMap.set(String(item.seatKey), item);
            } else if (item.seatId) {
                availabilityMap.set(String(item.seatId), item);
            }
        });

        return {
            ...seatMap,
            seatConfig: cloneSeatConfigWithAvailability(seatMap?.seatConfig || {}, availabilityMap),
        };
    }, [seatAvailability, seatMap]);

    if (isLoading) {
        return (
                <div className="flex flex-col items-center justify-center w-full h-full">
                    <Spin size="large" className="!mt-20"/>
                </div>
        );
    }

    return (
            <div className="w-full h-screen overflow-hidden">
                <SeatMapViewer
                        title={event?.name}
                        venueMap={mergedVenueMap}
                        onBack={() => navigate(-1)}
                        eventId={event?.id}
                        seatMapId={seatMap?.id}
                />
            </div>
    );
}

export default EventSeatMap;