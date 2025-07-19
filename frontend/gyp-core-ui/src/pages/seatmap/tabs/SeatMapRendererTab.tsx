import React from "react";
import SeatMapRenderer from "../../../components/seat-map/SeatMapRenderer.tsx";
import { useSeatMapFormContext } from "../SeatMapFormContext.tsx";

export interface SeatMapRendererTabProps {
    mode: string;
}

const SeatMapRendererTab: React.FC<SeatMapRendererTabProps> = () => {
    const {entity} = useSeatMapFormContext();

    return (
            <div className="bg-white overflow-auto! h-[calc(100vh-100px)]!">
                <SeatMapRenderer
                        venueMap={{
                            seatConfig: entity?.seatConfig,
                            stageConfig: entity?.stageConfig,
                        }}
                        title={entity?.name}
                />
            </div>
    );
}

export default SeatMapRendererTab;