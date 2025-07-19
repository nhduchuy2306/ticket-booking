import { notification } from "antd";
import React, { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import SinglePageLayout from "../../components/layout/singlepage/SinglePageLayout.tsx";
import { Mode } from "../../configs/Constants.ts";
import { SeatMapResponseDto } from "../../models/generated/event-service-models";
import { SeatMapService } from "../../services/Event/SeatMapService.ts";
import SeatMapDetailForm from "./SeatMapDetailForm.tsx";
import { SeatMapFormContext, SeatMapFormContextProps } from "./SeatMapFormContext.tsx";

export interface SeatMapFormProps {
    mode: string;
}

const SeatMapForm: React.FC<SeatMapFormProps> = ({mode}) => {
    const navigate = useNavigate();
    const {id} = useParams();
    const [entity, setEntity] = useState<SeatMapResponseDto | null>(null);

    useEffect(() => {
        const fetchData = async () => {
            try {
                if (id && (mode === Mode.EDIT.key || mode === Mode.READ_ONLY.key)) {
                    const response = await SeatMapService.getSeatMapById(id);
                    if (response) {
                        setEntity(response);
                    }
                } else {
                    setEntity(null);
                }
            } catch (error) {
                console.error("Error fetching data:", error);
                notification.error({message: "Failed to fetch data"});
            }
        };
        void fetchData();
    }, [id, mode]);

    const contextValue: SeatMapFormContextProps = {
        entity,
        setEntity,
    };

    return (
            <SinglePageLayout onNavigate={(path: string, entity?: SeatMapResponseDto) =>
                    SeatMapService.navigate(navigate, path, entity)}
            >
                <SeatMapFormContext.Provider value={contextValue}>
                    <SeatMapDetailForm mode={mode}/>
                </SeatMapFormContext.Provider>
            </SinglePageLayout>
    );
}

export default SeatMapForm;