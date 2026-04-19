import React from "react";
import { useNavigate } from "react-router-dom";
import { createErrorNotification } from "../../../components/notification/Notification.ts";
import { loadBookingSession, clearBookingSession, clearWaitingRoomClearance } from "../../../utils/bookingSession.ts";
import { SeatMapService } from "../../../services/Event/SeatMapService.ts";

const PaymentFailurePage: React.FC = () => {
    const navigate = useNavigate();
    const bookingSession = loadBookingSession();
    const bookingEventId = bookingSession?.eventId;
    const bookingHoldToken = bookingSession?.holdToken;
    const bookingSeatIds = bookingSession?.seatIds;
    const bookingSeatKeys = bookingSession?.seatKeys;
    const bookingUserId = bookingSession?.userId;
    const getWaitingRoomPath = () => bookingEventId ? `/gyp/events/${bookingEventId}/waiting-room?next=${encodeURIComponent(`/gyp/events/${bookingEventId}/choose-seats`)}` : "/gyp/";

    React.useEffect(() => {
        const releaseSeats = async () => {
            if (!bookingEventId || !bookingHoldToken) {
                return;
            }

            try {
                await SeatMapService.releaseSeats({
                    eventId: bookingEventId,
                    holdToken: bookingHoldToken,
                    seatIds: bookingSeatIds,
                    seatKeys: bookingSeatKeys || bookingSeatIds,
                    userId: bookingUserId,
                });
            } catch (error) {
                createErrorNotification(
                        "Seat release failed",
                        (error instanceof Error ? error.message : "Payment failed and the hold could not be released automatically.")
                );
            } finally {
                clearBookingSession();
                clearWaitingRoomClearance();
            }
        };

        void releaseSeats();
    }, [bookingEventId, bookingHoldToken, bookingSeatIds, bookingSeatKeys, bookingUserId]);

    return (
            <div className="flex flex-col items-center justify-center bg-red-100 !p-4 flex-grow">
                <div className="bg-white shadow-md rounded-lg !p-8 max-w-md w-full text-center">
                    <h1 className="text-3xl font-bold text-red-600 mb-4">Payment Failed</h1>
                    <p className="text-gray-700 mb-6">
                        The payment did not complete. Your seat hold has been released, and you can try again.
                    </p>
                    <button
                            onClick={() => navigate(getWaitingRoomPath())}
                            className="bg-red-600 !text-white !p-2 rounded hover:bg-red-700 transition cursor-pointer"
                    >
                        Retry Payment
                    </button>
                </div>
            </div>
    );
}

export default PaymentFailurePage;