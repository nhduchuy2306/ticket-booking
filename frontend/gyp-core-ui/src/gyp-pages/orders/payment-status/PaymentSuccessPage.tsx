import React from "react";
import { loadBookingSession } from "../../../utils/bookingSession.ts";

const PaymentSuccessPage: React.FC = () => {
    const {orderId, message} = Object.fromEntries(new URLSearchParams(window.location.search));
    const bookingSession = loadBookingSession();
    const ticketRows = bookingSession?.selectedSeats || [];

    return (
            <div className="flex flex-col items-center justify-center bg-green-100 !p-4 flex-grow">
                <div className="bg-white shadow-md rounded-lg !p-8 max-w-2xl w-full text-center">
                    <h1 className="text-3xl font-bold text-green-600 mb-4">Payment {message} with order {orderId}</h1>
                    <p className="text-gray-700 mb-6">
                        Your seats have been confirmed and tickets are ready.
                    </p>
                    {ticketRows.length > 0 && (
                            <div className="mb-6 text-left">
                                <h2 className="text-lg font-semibold text-gray-800 mb-3">Tickets</h2>
                                <div className="space-y-2 max-h-72 overflow-auto">
                                    {ticketRows.map((ticket) => (
                                            <div key={ticket.seatId} className="flex items-center justify-between rounded-lg border border-green-200 bg-green-50 px-4 py-3">
                                                <div>
                                                    <p className="font-semibold text-gray-900">Seat {ticket.seatName || ticket.seatId}</p>
                                                    <p className="text-sm text-gray-600">{ticket.sectionName || "General admission"}</p>
                                                </div>
                                                <div className="text-right">
                                                    <p className="font-semibold text-green-700">${ticket.price}</p>
                                                    <p className="text-xs text-gray-500">Confirmed</p>
                                                </div>
                                            </div>
                                    ))}
                                </div>
                            </div>
                    )}
                    <button
                            onClick={() => window.location.href = '/gyp/'}
                            className="bg-green-600 !text-white !p-2 rounded hover:bg-green-700 transition cursor-pointer"
                    >
                        Back to Home
                    </button>
                </div>
            </div>
    );
};

export default PaymentSuccessPage;