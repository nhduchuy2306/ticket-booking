import { Spin } from "antd";
import React from "react";
import { useNavigate, useParams } from "react-router-dom";
import SeatMapViewer from "../../components/seat-map/seat-map-viewer/SeatMapViewer.tsx";
import { useEventData } from "../../hooks/form/useEventData.tsx";

const EventSeatMap: React.FC = () => {
    const {id} = useParams();
    const {event, seatMap, isLoading} = useEventData({id});
    const navigate = useNavigate();

    if (isLoading) {
        return (
                <div className="flex flex-col items-center justify-center w-full h-full">
                    <Spin size="large" className="!mt-20"/>
                </div>
        );
    }

    return (
            <div className="w-full h-screen overflow-hidden">
                <SeatMapViewer
                        title={event?.name}
                        venueMap={{
                            seatConfig: seatMap?.seatConfig,
                            stageConfig: seatMap?.stageConfig,
                        }}
                        onBack={() => navigate(-1)}
                        eventId={event?.id}
                        seatMapId={seatMap?.id}
                />
            </div>
    );
}

export default EventSeatMap;