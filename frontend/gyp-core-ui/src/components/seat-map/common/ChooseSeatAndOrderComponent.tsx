import { Button } from "antd";
import React, { useMemo, useState } from "react";
import { BsTicketDetailed } from "react-icons/bs";
import { useNavigate } from "react-router-dom";
import { OrderDetailModel } from "../models/SeatMapModels.ts";
import { useSeatMapViewerContext } from "../seat-map-viewer/context/SeatMapViewerContext.tsx";
import { clearWaitingRoomClearance, createHoldToken, formatCountdown, getHoldCountdownSeconds, saveBookingSession } from "../../../utils/bookingSession.ts";
import { SeatMapService } from "../../../services/Event/SeatMapService.ts";
import { IamService } from "../../../services/Iam/IamService.ts";
import { createErrorNotification } from "../../notification/Notification.ts";

const ChooseSeatAndOrderComponent: React.FC = () => {
    const {selectedSeats, ticketTypes, eventId, seatMapId} = useSeatMapViewerContext();
    const navigate = useNavigate();
    const [isReserving, setIsReserving] = useState(false);

    const getSeatTypeMap = useMemo(() => {
        const map = new Map<string, number>();
        if (ticketTypes) {
            ticketTypes.forEach(st => {
                if (st.id && st.name) {
                    map.set(st.id, st.price || 0);
                }
            });
        }
        return map;
    }, [ticketTypes]);

    const getSelectedSeats = () => {
        if (!selectedSeats || selectedSeats.length === 0) {
            return "No seats selected";
        }
        return selectedSeats.map(s => s.seat.id).join(", ");
    }

    const getTotalAmount = () => {
        if (!selectedSeats || selectedSeats.length === 0) {
            return 0;
        }
        return selectedSeats.reduce((total, seat) => {
            const price = getSeatTypeMap.get(seat.section?.ticketTypeId || "") || 0;
            return total + price;
        }, 0);
    }

    const getSeatKey = (seatId: string, sectionId?: string, rowId?: string) => {
        if (!sectionId || !rowId) {
            return seatId;
        }

        return `${sectionId}-${rowId}-${seatId}`;
    };

    const getCurrentHoldCountdown = () => {
        const holdSession = sessionStorage.getItem("gyp:booking-hold-session");
        if (!holdSession) {
            return null;
        }

        try {
            const parsed = JSON.parse(holdSession) as { holdExpiresAt?: string };
            if (!parsed.holdExpiresAt) {
                return null;
            }

            return getHoldCountdownSeconds(parsed.holdExpiresAt);
        } catch {
            return null;
        }
    }

    const handleOrderTickets = async () => {
        if (!eventId || !selectedSeats || selectedSeats.length === 0) {
            return;
        }

        const seatIds = selectedSeats.map((seat) => String(seat.seat.id));
        const seatKeys = selectedSeats.map((seat) => getSeatKey(String(seat.seat.id), seat.section?.id, seat.row?.id));
        const existingCountdown = getCurrentHoldCountdown();
        if (existingCountdown && existingCountdown > 0) {
            navigate(`/gyp/events/${eventId}/orders`);
            return;
        }

        try {
            setIsReserving(true);
            const userId = IamService.getUserId();
            const reservationResponse = await SeatMapService.reserveSeats({
                eventId,
                seatIds,
                seatKeys,
                holdToken: createHoldToken(),
                userId,
            });

            const holdToken = reservationResponse.holdToken || createHoldToken();
            const holdExpiresAt = reservationResponse.holdExpiresAt || new Date(Date.now() + 15 * 60 * 1000).toISOString();

            saveBookingSession({
                eventId,
                seatMapId,
                holdToken,
                holdExpiresAt,
                seatIds,
                seatKeys,
                selectedSeats: selectedSeats.map((seat) => ({
                    seatId: String(seat.seat.id),
                    seatKey: getSeatKey(String(seat.seat.id), seat.section?.id, seat.row?.id),
                    seatName: seat.seat.name,
                    sectionName: seat.section?.name,
                    rowName: seat.row?.name,
                    ticketTypeId: seat.section?.ticketTypeId,
                    price: getSeatTypeMap.get(seat.section?.ticketTypeId || "") || 0,
                    status: seat.seat.status,
                })),
                totalAmount: getTotalAmount(),
                userId,
            });
            clearWaitingRoomClearance();

            const orderDetails: OrderDetailModel = {
                eventId,
                seatMapId,
                selectedSeats,
                totalAmount: getTotalAmount(),
                ticketTypeMap: getSeatTypeMap,
                holdToken,
                holdExpiresAt,
            };
            navigate(`/gyp/events/${eventId}/orders`, {
                state: { orderDetails }
            });
        } catch {
            createErrorNotification(
                    "Seat reservation failed",
                    "The selected seats could not be reserved. Refresh availability and try again."
            );
        } finally {
            setIsReserving(false);
        }
    };

    const holdCountdown = getCurrentHoldCountdown();

    return (
            <div className="flex flex-col items-start justify-center gap-4 h-full !m-4">
                {selectedSeats && selectedSeats.length > 0 &&
                    <div>
                        <div className="flex items-center justify-start gap-2 text-white font-bold">
                            <BsTicketDetailed/>
                            <span>:</span>
                            <span>{getSelectedSeats()}</span>
                        </div>
                        <div className="flex items-center justify-start gap-2 text-white font-bold">
                            <span>Total: </span>
                            <span>${getTotalAmount()}</span>
                        </div>
                        {holdCountdown !== null && holdCountdown > 0 && (
                                <div className="flex items-center justify-start gap-2 text-amber-300 font-bold">
                                    <span>Hold expires in:</span>
                                    <span>{formatCountdown(holdCountdown)}</span>
                                </div>
                        )}
                    </div>
                }
                <Button className="!bg-[#2dc275] !text-white !font-bold !mb-4 w-full !border-none disabled:!bg-gray-400 hover:!bg-[#25a563]"
                        disabled={!selectedSeats || selectedSeats.length === 0}
                        loading={isReserving}
                        onClick={handleOrderTickets}
                >
                    Reserve Seats & Continue
                </Button>
            </div>
    );
}

export default ChooseSeatAndOrderComponent;