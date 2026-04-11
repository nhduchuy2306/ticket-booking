import React from "react";
import SeatMapEditor, { type SeatMapEditorData } from "../../../components/seat-map/seat-map-editor/SeatMapEditor";
import { useSeatMapFormContext } from "../SeatMapFormContext.tsx";

export interface SeatMapEditorTabProps {
    mode: string;
}

const SeatMapEditorTab: React.FC<SeatMapEditorTabProps> = () => {
    const {entity} = useSeatMapFormContext();

    const initialData = entity?.seatConfig && entity?.stageConfig
            ? {
                stageConfig: entity.stageConfig as SeatMapEditorData["stageConfig"],
                seatConfig: entity.seatConfig as SeatMapEditorData["seatConfig"],
            }
            : undefined;

    return (
            <div className="bg-white !h-screen">
                <SeatMapEditor initialData={initialData}
                               title={entity?.name}
                               onSave={(data: SeatMapEditorData) => console.log("Seat map payload", data)}
                />
            </div>
    );
}

export default SeatMapEditorTab;