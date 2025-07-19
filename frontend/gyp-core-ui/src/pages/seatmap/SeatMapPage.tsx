import React from "react";
import { useNavigate } from "react-router-dom";
import SinglePageLayout from "../../components/layout/singlepage/SinglePageLayout.tsx";
import { SeatMapResponseDto } from "../../models/generated/event-service-models";
import { SeatMapService } from "../../services/Event/SeatMapService.ts";
import SeatMapTable from "./SeatMapTable.tsx";

interface SeatMapPageProps {
}

const SeatMapPage: React.FC<SeatMapPageProps> = () => {
    const navigate = useNavigate();

    return (
            <div className="bg-white">
                <SinglePageLayout onNavigate={(path: string, entity?: SeatMapResponseDto) =>
                        SeatMapService.navigate(navigate, path, entity)}>
                    <SeatMapTable/>
                </SinglePageLayout>
            </div>
    );
}

export default SeatMapPage;