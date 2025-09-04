import { Carousel, Spin } from "antd";
import React, { useEffect } from "react";
import { createErrorNotification } from "../../components/notification/Notification.ts";
import { EventResponseDto } from "../../models/generated/event-service-models";
import { EventService } from "../../services/Event/EventService.ts";
import "antd/dist/reset.css";
import { UploadUtils } from "../../utils/UploadUtils.ts";

const PageIllustration: React.FC = () => {
    const [events, setEvents] = React.useState<EventResponseDto[]>([]);
    const [isLoading, setIsLoading] = React.useState<boolean>(true);

    useEffect(() => {
        const fetchData = async () => {
            try {
                const eventResponse = await EventService.getOnSaleEvents();
                if (eventResponse) {
                    setEvents(eventResponse);
                }
            } catch (Error) {
                console.error("Error fetching events:", Error);
                createErrorNotification("Error", "Failed to fetch data.");
            } finally {
                setIsLoading(false);
            }
        }
        void fetchData();
    }, []);

    const handleEventClick = (eventId: string) => () => {
        console.log(`Event with ID ${eventId} clicked`);
    }

    return (
            <>
                {events && events.length > 0 &&
                    <div className="!h-[220px] !mt-3">
                        {isLoading
                                ? <Spin size="large" className="!mt-20"/>
                                : <div className="h-[220px] w-[1200px]">
                                    <Carousel arrows>
                                        {events.map((event) => (
                                                <div key={event.id}
                                                     className="relative w-full h-[220px] cursor-pointer"
                                                     onClick={handleEventClick(event.id!)}>
                                                    <img
                                                            src={UploadUtils.arrayBufferToBase64(event.logoBufferArray)}
                                                            alt={event.name}
                                                            className="w-full h-[210px] object-cover"
                                                    />
                                                    <h3 className="absolute inset-0 flex justify-center items-center bg-black/40 text-white text-xl font-bold">
                                                        {event.name}
                                                    </h3>
                                                </div>
                                        ))}
                                    </Carousel>
                                </div>
                        }
                    </div>
                }
            </>
    );
}

export default PageIllustration;
