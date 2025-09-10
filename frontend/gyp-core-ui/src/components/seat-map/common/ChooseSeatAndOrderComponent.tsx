import { Button } from "antd";
import React, { useMemo } from "react";
import { BsTicketDetailed } from "react-icons/bs";
import { useNavigate } from "react-router-dom";
import { OrderDetailModel } from "../models/SeatMapModels.ts";
import { useSeatMapViewerContext } from "../seat-map-viewer/context/SeatMapViewerContext.tsx";

interface ChooseSeatAndOrderComponentProps {
}

const ChooseSeatAndOrderComponent: React.FC<ChooseSeatAndOrderComponentProps> = () => {
    const {selectedSeats, seatTypes, eventId, seatMapId} = useSeatMapViewerContext();
    const navigate = useNavigate();

    const getSeatTypeMap = useMemo(() => {
        const map = new Map<string, number>();
        if (seatTypes) {
            seatTypes.forEach(st => {
                if (st.id && st.name) {
                    map.set(st.id, st.price || 0);
                }
            });
        }
        return map;
    }, [seatTypes]);

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

    const handleOrderTickets = () => {
        const orderDetails: OrderDetailModel = {
            eventId,
            seatMapId,
            selectedSeats,
            totalAmount: getTotalAmount(),
            ticketTypeMap: getSeatTypeMap,
        };
        navigate(`/gyp/events/${eventId}/orders`, {
            state: { orderDetails }
        });
    };

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
                    </div>
                }
                <Button className="!bg-[#2dc275] !text-white !font-bold !mb-4 w-full !border-none disabled:!bg-gray-400 hover:!bg-[#25a563]"
                        disabled={!selectedSeats || selectedSeats.length === 0}
                        onClick={handleOrderTickets}
                >
                    Order Tickets
                </Button>
            </div>
    );
}

export default ChooseSeatAndOrderComponent;