import React from "react";
import SeatMapEditor from "../../../components/seat-map/SeatMapEditor.tsx";
import { useSeatMapFormContext } from "../SeatMapFormContext.tsx";

export interface SeatMapEditorTabTabProps {
    mode: string;
}

const SeatMapEditorTab: React.FC<SeatMapEditorTabTabProps> = () => {
    const {entity} = useSeatMapFormContext();

    return (
            <div className="bg-white !h-screen">
                <SeatMapEditor
                        venueMap={{
                            seatConfig: entity?.seatConfig,
                            stageConfig: entity?.stageConfig,
                        }}
                        title={entity?.name}
                />
            </div>
    );
}

export default SeatMapEditorTab;