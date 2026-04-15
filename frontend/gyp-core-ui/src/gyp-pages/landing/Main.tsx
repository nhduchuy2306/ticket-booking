import { Spin } from "antd";
import React, { useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { createErrorNotification } from "../../components/notification/Notification.ts";
import MultiItemSlider from "../../components/slider/MultiItemSlider.tsx";
import { EventResponseDto } from "../../models/generated/event-service-models";
import { EventService } from "../../services/Event/EventService.ts";
import { UploadUtils } from "../../utils/UploadUtils.ts";

const Main: React.FC = () => {
    const navigate = useNavigate();
    const [onSaleEvents, setOnSaleEvents] = React.useState<EventResponseDto[]>([]);
    const [onComingEvents, setOnComingEvents] = React.useState<EventResponseDto[]>([]);
    const [isLoading, setIsLoading] = React.useState<boolean>(true);

    useEffect(() => {
        (async () => {
            try {
                const onSaleEventResponse = await EventService.getOnSaleEvents();
                if (onSaleEventResponse) {
                    setOnSaleEvents(onSaleEventResponse);
                }
            } catch (Error) {
                console.error("Error fetching events:", Error);
                createErrorNotification("Error", "Failed to fetch data.");
            } finally {
                setIsLoading(false);
            }
        })();

        (async () => {
            try {
                const comingEventResponse = await EventService.getComingEvents();
                if (comingEventResponse) {
                    setOnComingEvents(comingEventResponse);
                }
            } catch (Error) {
                console.error("Error fetching events:", Error);
                createErrorNotification("Error", "Failed to fetch data.");
            } finally {
                setIsLoading(false);
            }
        })();
    }, []);

    return (
            <main className="flex flex-col items-center justify-center w-full max-w-6xl mx-auto px-4 py-16">
                {/* Hero Section */}
                <div className="flex flex-col items-center justify-center !mb-16 !mt-8 w-full h-full">
                    <h2 className="text-3xl font-bold text-center mb-12 text-gray-800">
                        Special Events
                    </h2>
                    {isLoading
                            ? <Spin size="large" className="!mt-20"/>
                            : <MultiItemSlider
                                    itemsPerView={3}
                                    items={onSaleEvents.map((event) => (
                                            <>
                                                <div className="h-48 bg-gradient-to-br flex items-center justify-center">
                                                    <img
                                                            src={UploadUtils.arrayBufferToBase64(event.logoBufferArray)}
                                                            alt={event.name}
                                                            className="w-full h-[210px] object-cover"
                                                    />
                                                </div>
                                                <div className="text-center !mt-5">
                                                    <h4 className="font-semibold text-gray-800">
                                                        {event.name}
                                                    </h4>
                                                </div>
                                            </>
                                    ))}
                                    onItemClick={(index: number) => {
                                        const event = onSaleEvents[index];
                                        if (event && event.id) {
                                            navigate(`/gyp/events/${event.id}`);
                                        }
                                    }}
                            />
                    }
                </div>

                {/* Coming Event */}
                <div className="flex flex-col items-center justify-center !mb-16 !mt-8 w-full h-full">
                    <h2 className="text-3xl font-bold text-center mb-12 text-gray-800">
                        Coming Events
                    </h2>
                    {isLoading
                            ? <Spin size="large" className="!mt-20"/>
                            : <MultiItemSlider
                                    itemsPerView={3}
                                    items={onComingEvents.map((event) => (
                                            <>
                                                <div className="h-48 bg-gradient-to-br flex items-center justify-center">
                                                    <img
                                                            src={UploadUtils.arrayBufferToBase64(event.logoBufferArray)}
                                                            alt={event.name}
                                                            className="w-full h-[210px] object-cover"
                                                    />
                                                </div>
                                                <div className="text-center !mt-5">
                                                    <h4 className="font-semibold text-gray-800">
                                                        {event.name}
                                                    </h4>
                                                </div>
                                            </>
                                    ))}
                                    onItemClick={(index: number) => {
                                        const event = onComingEvents[index];
                                        if (event && event.id) {
                                            navigate(`/gyp/events/${event.id}`);
                                        }
                                    }}
                            />
                    }
                </div>

                {/* Features Grid */}
                <div className="w-full">
                    <h2 className="text-3xl font-bold text-center mb-12 text-gray-800">
                        Explore What Matters to You
                    </h2>

                    <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-8">
                        {/* Football */}
                        <div className="group bg-white rounded-xl shadow-lg overflow-hidden hover:shadow-xl transition-all duration-300 transform hover:-translate-y-2">
                            <div className="h-48 bg-gradient-to-br from-green-400 to-green-600 flex items-center justify-center">
                                <div className="text-6xl">⚽</div>
                            </div>
                            <div className="!p-6">
                                <h3 className="text-xl font-semibold mb-2 text-gray-800">Sports Events</h3>
                                <p className="text-gray-600">Join thrilling football matches and sports competitions
                                    in your area.</p>
                            </div>
                        </div>

                        {/* Concert */}
                        <div className="group bg-white rounded-xl shadow-lg overflow-hidden hover:shadow-xl transition-all duration-300 transform hover:-translate-y-2">
                            <div className="h-48 bg-gradient-to-br from-purple-400 to-pink-600 flex items-center justify-center">
                                <div className="text-6xl">🎵</div>
                            </div>
                            <div className="!p-6">
                                <h3 className="text-xl font-semibold mb-2 text-gray-800">Live Concerts</h3>
                                <p className="text-gray-600">Experience amazing live music performances and concerts
                                    near you.</p>
                            </div>
                        </div>

                        {/* Museum */}
                        <div className="group bg-white rounded-xl shadow-lg overflow-hidden hover:shadow-xl transition-all duration-300 transform hover:-translate-y-2">
                            <div className="h-48 bg-gradient-to-br from-yellow-400 to-orange-600 flex items-center justify-center">
                                <div className="text-6xl">🏛️</div>
                            </div>
                            <div className="!p-6">
                                <h3 className="text-xl font-semibold mb-2 text-gray-800">Museums & Art</h3>
                                <p className="text-gray-600">Discover cultural treasures and exhibitions at local
                                    museums.</p>
                            </div>
                        </div>

                        {/* Stadium */}
                        <div className="group bg-white rounded-xl shadow-lg overflow-hidden hover:shadow-xl transition-all duration-300 transform hover:-translate-y-2">
                            <div className="h-48 bg-gradient-to-br from-red-400 to-red-600 flex items-center justify-center">
                                <div className="text-6xl">🏟️</div>
                            </div>
                            <div className="!p-6">
                                <h3 className="text-xl font-semibold mb-2 text-gray-800">Venues & Events</h3>
                                <p className="text-gray-600">Find the best stadiums and event venues for
                                    unforgettable
                                    experiences.</p>
                            </div>
                        </div>
                    </div>
                </div>

                {/* Bottom CTA Section */}
                <div className="!mt-16 !mb-16 flex flex-col items-center justify-center bg-gradient-to-r from-blue-50 to-purple-50 rounded-2xl !p-12 w-full">
                    <h3 className="text-3xl font-bold mb-4 text-gray-800">
                        Ready to Get Started?
                    </h3>
                    <p className="text-lg text-gray-600 mb-8 max-w-2xl mx-auto text-center">
                        Join thousands of users who have already discovered their perfect experiences.
                        Sign up today and unlock a world of possibilities.
                    </p>
                    <div className="flex flex-col sm:flex-row gap-4 justify-center">
                        <button className="!p-2 bg-gradient-to-r from-blue-600 to-purple-600 hover:from-blue-700 hover:to-purple-700 text-white font-semibold py-4 px-10 rounded-lg shadow-lg transform hover:scale-105 transition-all duration-200">
                            Get Started Free
                        </button>
                        <button className="!p-2 bg-white hover:bg-gray-50 text-gray-700 font-semibold py-4 px-10 rounded-lg border border-gray-300 shadow-lg transform hover:scale-105 transition-all duration-200">
                            Learn More
                        </button>
                    </div>
                </div>
            </main>
    );
}

export default Main;