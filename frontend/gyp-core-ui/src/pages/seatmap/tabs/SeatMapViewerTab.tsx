import React from "react";
import SeatMapViewer from "../../../components/seat-map/seat-map-viewer/SeatMapViewer.tsx";
import { useSeatMapFormContext } from "../SeatMapFormContext.tsx";

export interface SeatMapViewerTabProps {
    mode: string;
}

const SeatMapViewerTab: React.FC<SeatMapViewerTabProps> = () => {
    const {entity} = useSeatMapFormContext();

    return (
            <div className="bg-white !h-screen">
                <SeatMapViewer
                        venueMap={{
                            seatConfig: entity?.seatConfig,
                            stageConfig: entity?.stageConfig,
                        }}
                        title={entity?.name}
                />
            </div>
    );
}

export default SeatMapViewerTab;