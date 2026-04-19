import { Button, Spin } from "antd";
import React from "react";
import { FaClock, FaMoneyBillWave } from "react-icons/fa";
import { FaLocationDot } from "react-icons/fa6";
import { useNavigate, useParams } from "react-router-dom";
import { useEventData } from "../../hooks/form/useEventData.tsx";
import { UploadUtils } from "../../utils/UploadUtils.ts";
import { useGypPageContext } from "../GypPageContext.tsx";

const EventDetailPage: React.FC = () => {
    const {id} = useParams();
    const {tenantOrganizationId} = useGypPageContext();
    const {event, isLoading, venue} = useEventData({id, tenantOrganizationId});
    const navigate = useNavigate();

    const getMinTicketPrice = () => {
        if (!event || !event.ticketTypes || event.ticketTypes.length === 0) {
            return null;
        }

        const prices = event.ticketTypes
                .map(t => t.price)
                .filter((p): p is number => p !== undefined);

        return prices.length > 0 ? Math.min(...prices) : null;
    }

    if (isLoading) {
        return (
                <div className="flex flex-col items-center justify-center w-full h-full">
                    <Spin size="large" className="!mt-20"/>
                </div>
        );
    }

    if (!event) {
        return <div className="w-full flex items-center justify-center py-20">Event not found.</div>;
    }

    return (
            <div className="w-full flex flex-col flex-grow">
                <div className="!p-4 flex items-start justify-center bg-gray-400 flex-grow">
                    <div className="w-6xl flex items-center justify-center">
                        <div className="flex-1/3 flex flex-col justify-center rounded-l-xl bg-[#38383d] h-[400px] text-white !p-4">
                            <div className="flex-1 flex flex-col gap-5">
                                <p className="text-2xl font-bold">{event?.name}</p>
                                <div className="flex items-center gap-2">
                                    <FaClock/>
                                    <span>{event?.startTime}</span>
                                </div>
                                <div className="flex items-center gap-2">
                                    <span>Open Time:</span>
                                    <span>{event?.doorOpenTime}</span>
                                </div>
                                <div className="flex items-center gap-2">
                                    <FaLocationDot/>
                                    <span>{venue?.address}</span>
                                </div>
                            </div>
                            <div>
                                <div className="flex items-center gap-2">
                                    <FaMoneyBillWave/>
                                    <span>From: $ {getMinTicketPrice()}</span>
                                </div>
                                <Button
                                        type="primary"
                                        className="w-full !mt-2"
                                        onClick={() => navigate(`/gyp/events/${event?.id}/choose-seats`)}
                                >
                                    Buy Ticket
                                </Button>
                                <Button
                                        type="default"
                                        className="w-full !mt-2"
                                        onClick={() => navigate(`/gyp/events/${event?.id}/waiting-room?eventName=${encodeURIComponent(event?.name || "")}`)}
                                >
                                    Join Waiting Room
                                </Button>
                            </div>
                        </div>
                        <div className="flex-2/3">
                            <img
                                    src={UploadUtils.arrayBufferToBase64(event?.logoBufferArray)}
                                    alt={event?.name}
                                    className="w-full h-[400px] object-cover rounded-r-xl"
                            />
                        </div>
                    </div>
                </div>
            </div>
    );
}

export default EventDetailPage;