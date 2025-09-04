import { Button } from "antd";
import React, { useEffect, useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import { OrderDetailModel } from "../../components/seat-map/models/SeatMapModels.ts";
import { useEventData } from "../../hooks/form/useEventData.tsx";
import { OrderService } from "../../services/Order/OrderService.ts";
import { DateUtils } from "../../utils/DateUtils.ts";

const EventOrderPage: React.FC = () => {
    const location = useLocation();
    const {eventId, selectedSeats, totalPrice, ticketTypeMap}: OrderDetailModel = location.state?.orderDetails;
    const {event, venue} = useEventData({id: eventId});
    const navigate = useNavigate();

    if (!eventId || !selectedSeats) {
        navigate("/gyp/", {replace: true});
    }

    // Countdown timer state (15 minutes)
    const [timeLeft, setTimeLeft] = useState(15 * 60);

    useEffect(() => {
        const eventSource = OrderService.getCountDownTime();

        eventSource.onmessage = (event: MessageEvent) => {
            setTimeLeft(() => {
                const newTime = parseInt(event.data, 10);
                console.log("Received SSE time event:", newTime);
                if (isNaN(newTime) || newTime < 0) {
                    return 0;
                }
                return newTime;
            });
        };

        eventSource.addEventListener("time", (event: MessageEvent) => {
            setTimeLeft(() => {
                const newTime = parseInt(event.data, 10);
                console.log("Received SSE time event:", newTime);
                if (isNaN(newTime) || newTime < 0) {
                    return 0;
                }
                return newTime;
            });
        });

        eventSource.onerror = (error) => {
            console.error("SSE error:", error);
            eventSource.close();
        };

        return () => {
            if (eventSource) {
                eventSource.close();
            }
        }
    }, []);

    // Format time as MM:SS
    const formatTime = (seconds: number) => {
        const minutes = Math.floor(seconds / 60);
        const remainingSeconds = seconds % 60;
        return `${minutes.toString().padStart(2, '0')}:${remainingSeconds.toString().padStart(2, '0')}`;
    };

    const handleCompleteOrder = async () => {
        if (timeLeft <= 0) {
            alert("Time has expired. Please start over.");
        }
        if (totalPrice) {
            const vndPrice = totalPrice * 23000; // Convert to VND
            const paymentResponse = await OrderService.createPaymentEndpoint(vndPrice);
            console.log("Redirecting to payment URL:", paymentResponse);
            if(paymentResponse) {
                window.location.href = paymentResponse.payUrl;
            }
        }
    }

    return (
            <div className="bg-black h-full !p-6">
                <div className="mx-auto">
                    <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">

                        {/* Order Details Block - Left Side */}
                        <div className="lg:col-span-2">
                            <div className="bg-white rounded-2xl shadow-lg !p-8 border border-gray-200">
                                <div className="!mb-6">
                                    <h1 className="text-3xl font-bold text-gray-800 mb-2">Order Summary</h1>
                                    <div className="w-16 h-1 bg-blue-500 rounded"></div>
                                </div>

                                {/* Event Information */}
                                <div className="!mb-8">
                                    <h2 className="text-xl font-semibold text-gray-700 !mb-4">Event Details</h2>
                                    <div className="space-y-3">
                                        <div className="flex justify-between items-center py-2 border-b border-gray-100">
                                            <span className="text-gray-600">Event ID:</span>
                                            <span className="font-medium text-gray-800">{eventId}</span>
                                        </div>
                                        {event?.name && (
                                                <div className="flex justify-between items-center py-2 border-b border-gray-100">
                                                    <span className="text-gray-600">Event Name:</span>
                                                    <span className="font-medium text-gray-800">{event?.name}</span>
                                                </div>
                                        )}
                                        {event?.startTime && (
                                                <div className="flex justify-between items-center py-2 border-b border-gray-100">
                                                    <span className="text-gray-600">Date & Time:</span>
                                                    <span className="font-medium text-gray-800">{DateUtils.formatToDateTime(event?.startTime)}</span>
                                                </div>
                                        )}
                                        {venue?.name && (
                                                <div className="flex justify-between items-center py-2 border-b border-gray-100">
                                                    <span className="text-gray-600">Venue:</span>
                                                    <span className="font-medium text-gray-800">{venue?.name}</span>
                                                </div>
                                        )}
                                    </div>
                                </div>

                                {/* Selected Seats */}
                                <div className="mb-8">
                                    <h2 className="text-xl font-semibold text-gray-700 !mb-4">Selected Seats</h2>
                                    <div className="space-y-3">
                                        {selectedSeats?.map((selectedSeat, index) => (
                                                <div key={index}
                                                     className="flex justify-between items-center py-3 px-4 bg-gray-50 rounded-lg">
                                                    <span className="font-medium text-gray-800">
                                                        Seat {selectedSeat.seat.id}
                                                    </span>
                                                    <span className="font-semibold text-gray-800">${ticketTypeMap?.get(selectedSeat.section?.ticketTypeId)}</span>
                                                </div>
                                        ))}
                                    </div>
                                </div>

                                {/* Total Price */}
                                <div className="border-t border-gray-200 !pt-6">
                                    <div className="flex justify-between items-center">
                                        <span className="text-xl font-semibold text-gray-700">Total Price:</span>
                                        <span className="text-2xl font-bold text-green-600">${totalPrice}</span>
                                    </div>
                                </div>

                                {/* Action Buttons */}
                                <div className="!mt-8 flex gap-4">
                                    <Button type="primary" size="large" className="flex-3/4"
                                            onClick={handleCompleteOrder}
                                    >Complete Order</Button>
                                    <Button type="default" size="large" className="flex-1/4"
                                            onClick={() => navigate("/gyp/", {replace: true})}
                                    >Cancel</Button>
                                </div>
                            </div>
                        </div>

                        {/* Countdown Timer Block - Right Side */}
                        <div className="lg:col-span-1">
                            <div className={`${
                                    timeLeft <= 60 ? 'from-red-600 to-red-700' : 'from-red-500 to-red-600'
                            } bg-gradient-to-br text-white rounded-2xl shadow-lg !p-8 text-center sticky top-6`}>
                                <div className="mb-4">
                                    <h2 className="text-lg font-semibold mb-2">Time Remaining</h2>
                                    <p className="text-sm opacity-90">Complete your order before time expires</p>
                                </div>

                                <div className="mb-6">
                                    <div className={`text-6xl font-bold mb-2 font-mono tracking-wider ${
                                            timeLeft <= 60 && timeLeft > 0 ? 'animate-pulse' : ''
                                    }`}>
                                        {formatTime(timeLeft)}
                                    </div>
                                    <div className="text-sm opacity-75">
                                        {timeLeft > 60 ? 'Minutes : Seconds' : timeLeft > 0 ? 'Seconds Remaining' : 'Expired'}
                                    </div>
                                </div>

                                {timeLeft <= 60 && timeLeft > 0 && (
                                        <div className="mb-4 p-3 bg-red-700 bg-opacity-50 rounded-lg animate-pulse">
                                            <p className="text-sm font-medium">⚠️ Hurry up!</p>
                                            <p className="text-xs opacity-90">Less than a minute left</p>
                                        </div>
                                )}

                                {timeLeft <= 0 && (
                                        <div className="mb-4 p-3 bg-red-800 rounded-lg">
                                            <p className="text-sm font-medium">⏰ Time Expired</p>
                                            <p className="text-xs opacity-90">Please start over</p>
                                        </div>
                                )}

                                <div className="space-y-3 text-sm border-t border-red-400 border-opacity-30 pt-4">
                                    <div className="flex justify-between opacity-75">
                                        <span>Seats Reserved:</span>
                                        <span>{selectedSeats?.length || 0}</span>
                                    </div>
                                    <div className="flex justify-between opacity-75">
                                        <span>Total Amount:</span>
                                        <span>${totalPrice}</span>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
    );
};

export default EventOrderPage;