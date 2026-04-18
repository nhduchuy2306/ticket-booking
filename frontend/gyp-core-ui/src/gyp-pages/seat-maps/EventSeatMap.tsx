import { Spin } from "antd";
import React, { useEffect, useMemo, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import SeatMapViewer from "../../components/seat-map/seat-map-viewer/SeatMapViewer.tsx";
import { useEventData } from "../../hooks/form/useEventData.tsx";
import { SeatAvailabilityDto } from "../../models/booking/SeatHoldModels.ts";
import { Row, Seat, SeatConfig, Section } from "../../models/generated/event-service-models";
import { SeatMapService } from "../../services/Event/SeatMapService.ts";

const cloneSeatConfigWithAvailability = (seatConfig: SeatConfig, availabilityMap: Map<string, SeatAvailabilityDto>) => {
    if (!seatConfig?.sections) {
        return seatConfig;
    }

    const patchSeat = (seat: Seat) => {
        if (!seat?.id) {
            return seat;
        }

        const availability = availabilityMap.get(String(seat.id));
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
        seats: row?.seats?.map((seat: Seat) => patchSeat(seat)) || [],
    });

    return {
        ...seatConfig,
        sections: seatConfig.sections.map((section: Section) => ({
            ...section,
            rows: section?.rows?.map((row: Row) => patchRow(row)) || [],
        })),
    };
};

const EventSeatMap: React.FC = () => {
    const {id} = useParams();
    const {event, seatMap, isLoading} = useEventData({id});
    const navigate = useNavigate();
    const [seatAvailability, setSeatAvailability] = useState<SeatAvailabilityDto[]>([]);

    useEffect(() => {
        const loadAvailability = async () => {
            if (!event?.id) {
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
    }, [event?.id]);

    const mergedVenueMap = useMemo(() => {
        if (!seatMap) {
            return undefined;
        }

        const availabilityMap = new Map<string, SeatAvailabilityDto>();
        seatAvailability.forEach((item) => {
            if (item.seatId) {
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