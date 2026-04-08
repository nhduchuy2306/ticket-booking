import { Button, Form, Input, Modal } from "antd";
import React, { useEffect, useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import { OrderDetailModel } from "../../components/seat-map/models/SeatMapModels.ts";
import { useEventData } from "../../hooks/form/useEventData.tsx";
import { OrderRequestDto } from "../../models/generated/order-service-models";
import { OrderService } from "../../services/Order/OrderService.ts";
import { PaymentService } from "../../services/Order/PaymentService.ts";
import { DateUtils } from "../../utils/DateUtils.ts";
import { clearBookingSession, getHoldCountdownSeconds, isHoldExpired, loadBookingSession, saveBookingSession } from "../../utils/bookingSession.ts";
import { SeatMapService } from "../../services/Event/SeatMapService.ts";
import { createErrorNotification } from "../../components/notification/Notification.ts";

const EventOrderPage: React.FC = () => {
    const location = useLocation();
    const bookingSession = loadBookingSession();
    const orderDetailsFromState: OrderDetailModel = location.state?.orderDetails;
    const {eventId, selectedSeats, totalAmount, ticketTypeMap, holdToken, holdExpiresAt}: OrderDetailModel = orderDetailsFromState || {
        eventId: bookingSession?.eventId,
        selectedSeats: bookingSession?.selectedSeats.map((seat) => ({
            seat: {id: seat.seatId, name: seat.seatName, status: seat.status},
            section: {name: seat.sectionName, ticketTypeId: seat.ticketTypeId},
        })),
        totalAmount: bookingSession?.totalAmount,
        holdToken: bookingSession?.holdToken,
        holdExpiresAt: bookingSession?.holdExpiresAt,
    };
    const {event, venue} = useEventData({id: eventId});
    const navigate = useNavigate();
    const [isLoading, setIsLoading] = useState(false);
    const [form] = Form.useForm();
    const [timeLeft, setTimeLeft] = useState(() => getHoldCountdownSeconds(holdExpiresAt || bookingSession?.holdExpiresAt));
    const bookingSeatPriceMap = new Map<string, number>(bookingSession?.selectedSeats?.map((seat) => [seat.seatId, seat.price]) || []);
    const resolvedTotalAmount = totalAmount || bookingSession?.totalAmount || 0;

    const resolvedSeatRows = (selectedSeats && selectedSeats.length > 0)
            ? selectedSeats.map((seat) => ({
                seatId: String(seat.seat.id),
                seatName: seat.seat.name || String(seat.seat.id),
                sectionName: seat.section?.name || "Seat",
                ticketTypeId: seat.section?.ticketTypeId,
                price: ticketTypeMap?.get(seat.section?.ticketTypeId || "") || bookingSeatPriceMap.get(String(seat.seat.id)) || 0,
            }))
            : (bookingSession?.selectedSeats || []);

    const showSessionExpiredModal = () => {
        Modal.confirm({
            title: "Session Expired",
            content: "Your seat hold expired. Please reserve the seats again.",
            okText: "Start New Order",
            cancelText: "Go Home",
            onOk: async () => {
                clearBookingSession();
                navigate(`/gyp/events/${eventId}/choose-seats`, {replace: true});
            },
            onCancel: () => {
                clearBookingSession();
                navigate("/gyp/", {replace: true});
            },
            maskClosable: false,
            closable: false,
        });
    };

    useEffect(() => {
        if (!eventId || !selectedSeats || selectedSeats.length === 0) {
            navigate("/gyp/", {replace: true});
        }
    }, [eventId, navigate, selectedSeats]);

    useEffect(() => {
        if (!holdExpiresAt && !bookingSession?.holdExpiresAt) {
            return;
        }

        const timer = setInterval(() => {
            setTimeLeft(getHoldCountdownSeconds(holdExpiresAt || bookingSession?.holdExpiresAt));
        }, 1000);

        return () => clearInterval(timer);
    }, [bookingSession?.holdExpiresAt, holdExpiresAt]);

    useEffect(() => {
        if (timeLeft === 0 && (holdToken || bookingSession?.holdToken)) {
            showSessionExpiredModal();
        }
    }, [bookingSession?.holdToken, eventId, holdToken, navigate, showSessionExpiredModal, timeLeft]);

    // Format time as MM:SS
    const formatTime = (seconds: number) => {
        const minutes = Math.floor(seconds / 60);
        const remainingSeconds = seconds % 60;
        return `${minutes.toString().padStart(2, '0')}:${remainingSeconds.toString().padStart(2, '0')}`;
    };

    const handleCompleteOrder = async () => {
        if (timeLeft <= 0 || isHoldExpired(holdExpiresAt || bookingSession?.holdExpiresAt)) {
            showSessionExpiredModal();
        } else {
            showInputEmailAndPhoneModal();
        }
    }

    const showInputEmailAndPhoneModal = () => {
        Modal.confirm({
            title: "Enter Email and Phone",
            content: (
                    <Form
                            form={form}
                            layout="vertical"
                            className="mt-4"
                    >
                        <Form.Item
                                name="email"
                                label="Email"
                                rules={[
                                    {required: true, message: 'Please enter your email!'},
                                    {type: 'email', message: 'Please enter a valid email!'}
                                ]}
                        >
                            <Input type="email" placeholder="Enter your email"/>
                        </Form.Item>

                        <Form.Item
                                name="phone"
                                label="Phone Number"
                                rules={[
                                    {required: true, message: 'Please enter your phone number!'},
                                    {pattern: /^[+]?[\d\s-()]+$/, message: 'Please enter a valid phone number!'}
                                ]}
                        >
                            <Input type="tel" placeholder="Enter your phone number"/>
                        </Form.Item>
                    </Form>
            ),
            okText: "Ok",
            cancelText: "Cancel",
            okButtonProps: {
                loading: isLoading,
            },
            onOk: async () => {
                try {
                    setIsLoading(true);
                    const values = await form.validateFields();

                    if (resolvedTotalAmount) {
                        const orderRequest: OrderRequestDto = {
                            eventId: eventId,
                            customerEmail: values.email,
                            totalAmount: resolvedTotalAmount,
                            orderDetails: resolvedSeatRows.map((seat) => ({
                                seatId: seat.seatId,
                                quantity: 1,
                                price: seat.price,
                            })),
                        }
                        const pendingOrder = await OrderService.createOrder(orderRequest);
                        if (!pendingOrder || !pendingOrder.id) {
                            alert("Failed to create order. Please try again.");
                            setIsLoading(false);
                            return Promise.resolve(); // Close modal
                        }

                        const vndPrice = resolvedTotalAmount * 23000; // Convert to VND
                        const paymentResponse = await PaymentService.createPaymentEndpoint(vndPrice, pendingOrder.id);
                        if (paymentResponse) {
                            const sessionSelectedSeats = (resolvedSeatRows.length > 0)
                                    ? resolvedSeatRows.map((seat) => ({
                                        seatId: seat.seatId,
                                        seatName: seat.seatName,
                                        sectionName: seat.sectionName,
                                        ticketTypeId: seat.ticketTypeId,
                                        price: seat.price,
                                        holdToken: holdToken || bookingSession?.holdToken,
                                        holdExpiresAt: holdExpiresAt || bookingSession?.holdExpiresAt,
                                    }))
                                    : (bookingSession?.selectedSeats || []);

                            saveBookingSession({
                                ...(bookingSession || {
                                    eventId: eventId || "",
                                    holdToken: holdToken || bookingSession?.holdToken || "",
                                    holdExpiresAt: holdExpiresAt || bookingSession?.holdExpiresAt || new Date(Date.now() + 5 * 60 * 1000).toISOString(),
                                    seatIds: resolvedSeatRows.map((seat) => seat.seatId),
                                    selectedSeats: sessionSelectedSeats,
                                    totalAmount: resolvedTotalAmount,
                                }),
                                eventId: eventId || bookingSession?.eventId || "",
                                orderId: pendingOrder.id,
                                customerEmail: values.email,
                            });
                            window.location.href = paymentResponse.payUrl;
                        }
                    }
                } catch (error) {
                    createErrorNotification(
                            "Checkout failed",
                            (error as any)?.response?.data?.message || (error as Error)?.message || "Unable to create the order. Please try again."
                    );
                    setIsLoading(false);
                    return Promise.reject();
                } finally {
                    setIsLoading(false);
                }
            },
            onCancel: () => {
                if (bookingSession?.eventId && (holdToken || bookingSession?.holdToken)) {
                    void SeatMapService.releaseSeats({
                        eventId: bookingSession.eventId,
                        holdToken: holdToken || bookingSession.holdToken,
                        seatIds: bookingSession.seatIds,
                        seatKeys: bookingSession.seatIds,
                        userId: bookingSession.userId,
                    }).finally(() => clearBookingSession());
                }
            },
            maskClosable: false,
        });
    };

    return (
            <div className="bg-black !p-6 flex flex-grow">
                <div className="w-full">
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
                                            {resolvedSeatRows.map((selectedSeat, index) => (
                                                <div key={index}
                                                     className="flex justify-between items-center py-3 px-4 bg-gray-50 rounded-lg">
                                                    <span className="font-medium text-gray-800">
                                                            Seat {selectedSeat.seatName || selectedSeat.seatId}
                                                    </span>
                                                        <span className="font-semibold text-gray-800">${selectedSeat.price}</span>
                                                </div>
                                        ))}
                                    </div>
                                </div>

                                {/* Total Price */}
                                <div className="border-t border-gray-200 !pt-6">
                                    <div className="flex justify-between items-center">
                                        <span className="text-xl font-semibold text-gray-700">Total Price:</span>
                                        <span className="text-2xl font-bold text-green-600">${totalAmount}</span>
                                    </div>
                                </div>

                                {/* Action Buttons */}
                                <div className="!mt-8 flex gap-4">
                                    <Button type="primary" size="large" className="flex-3/4"
                                            onClick={handleCompleteOrder}
                                    >Complete Order</Button>
                                    <Button type="default" size="large" className="flex-1/4"
                                            onClick={() => {
                                                Modal.confirm({
                                                    title: "Cancel Order",
                                                    content: "Are you sure you want to cancel your order? All selected seats will be released.",
                                                    okText: "Yes, Cancel",
                                                    cancelText: "No, Go Back",
                                                    onOk: () => {
                                                        if (bookingSession?.eventId && (holdToken || bookingSession?.holdToken)) {
                                                            void SeatMapService.releaseSeats({
                                                                eventId: bookingSession.eventId,
                                                                holdToken: holdToken || bookingSession.holdToken,
                                                                seatIds: bookingSession.seatIds,
                                                                seatKeys: bookingSession.seatIds,
                                                                userId: bookingSession.userId,
                                                            }).finally(() => clearBookingSession());
                                                        } else {
                                                            clearBookingSession();
                                                        }
                                                        navigate("/gyp/", {replace: true});
                                                    },
                                                    onCancel: () => {
                                                        console.log("User chose to continue order");
                                                    },
                                                    maskClosable: false,
                                                });
                                            }}
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
                                        <span>{selectedSeats?.length || bookingSession?.selectedSeats?.length || 0}</span>
                                    </div>
                                    <div className="flex justify-between opacity-75">
                                        <span>Total Amount:</span>
                                        <span>${resolvedTotalAmount}</span>
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