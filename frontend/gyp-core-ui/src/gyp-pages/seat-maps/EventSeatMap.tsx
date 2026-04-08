import { Spin } from "antd";
import React, { useEffect, useMemo, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import SeatMapViewer from "../../components/seat-map/seat-map-viewer/SeatMapViewer.tsx";
import { useEventData } from "../../hooks/form/useEventData.tsx";
import { SeatAvailabilityDto } from "../../models/booking/SeatHoldModels.ts";
import { SeatMapService } from "../../services/Event/SeatMapService.ts";

const cloneSeatConfigWithAvailability = (seatConfig: any, availabilityMap: Map<string, SeatAvailabilityDto>): any => {
    if (!seatConfig?.sections) {
        return seatConfig;
    }

    const patchSeat = (seat: any) => {
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
            attributes: {
                ...(seat.attributes || {}),
                holdToken: availability.holdToken,
                holdExpiresAt: availability.holdExpiresAt || availability.expiresAt,
            },
        };
    };

    const patchRow = (row: any) => ({
        ...row,
        seats: row?.seats?.map((seat: any) => patchSeat(seat)) || [],
    });

    const patchTable = (table: any) => ({
        ...table,
        seats: table?.seats?.map((seat: any) => patchSeat(seat)) || [],
    });

    return {
        ...seatConfig,
        sections: seatConfig.sections.map((section: any) => ({
            ...section,
            rows: section?.rows?.map((row: any) => patchRow(row)) || [],
            tables: section?.tables?.map((table: any) => patchTable(table)) || [],
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
            seatConfig: cloneSeatConfigWithAvailability(seatMap.seatConfig, availabilityMap),
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