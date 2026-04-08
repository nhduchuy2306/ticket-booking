import React from "react";
import { useNavigate } from "react-router-dom";
import { createErrorNotification } from "../../../components/notification/Notification.ts";
import { loadBookingSession, clearBookingSession } from "../../../utils/bookingSession.ts";
import { SeatMapService } from "../../../services/Event/SeatMapService.ts";

const PaymentFailurePage: React.FC = () => {
    const navigate = useNavigate();
    const bookingSession = loadBookingSession();

    React.useEffect(() => {
        const releaseSeats = async () => {
            if (!bookingSession?.eventId || !bookingSession?.holdToken) {
                return;
            }

            try {
                await SeatMapService.releaseSeats({
                    eventId: bookingSession.eventId,
                    holdToken: bookingSession.holdToken,
                    seatIds: bookingSession.seatIds,
                    seatKeys: bookingSession.seatIds,
                    userId: bookingSession.userId,
                });
            } catch (error) {
                createErrorNotification(
                        "Seat release failed",
                        (error as any)?.response?.data?.message || "Payment failed and the hold could not be released automatically."
                );
            } finally {
                clearBookingSession();
            }
        };

        void releaseSeats();
    }, [bookingSession?.eventId, bookingSession?.holdToken]);

    return (
            <div className="flex flex-col items-center justify-center bg-red-100 !p-4 flex-grow">
                <div className="bg-white shadow-md rounded-lg !p-8 max-w-md w-full text-center">
                    <h1 className="text-3xl font-bold text-red-600 mb-4">Payment Failed</h1>
                    <p className="text-gray-700 mb-6">
                        The payment did not complete. Your seat hold has been released, and you can try again.
                    </p>
                    <button
                            onClick={() => navigate(bookingSession?.eventId ? `/gyp/events/${bookingSession.eventId}/choose-seats` : "/gyp/")}
                            className="bg-red-600 !text-white !p-2 rounded hover:bg-red-700 transition cursor-pointer"
                    >
                        Retry Payment
                    </button>
                </div>
            </div>
    );
}

export default PaymentFailurePage;