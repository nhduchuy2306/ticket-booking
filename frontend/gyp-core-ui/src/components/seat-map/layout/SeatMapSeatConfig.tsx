import React, { useEffect } from "react";
import { Group } from "react-konva";
import { SeatConfig } from "../../../models/generated/event-service-models";
import SeatMapSection from "../section/SeatMapSection.tsx";

export interface SeatMapSeatConfigProps {
    seatConfig?: SeatConfig;
}

const SeatMapSeatConfig: React.FC<SeatMapSeatConfigProps> = (props) => {
    const [seatConfig, setSeatConfig] = React.useState<SeatConfig | undefined>(props.seatConfig);

    useEffect(() => {
        const data = props.seatConfig;
        if (data) {
            setSeatConfig(data);
        }
    }, [props.seatConfig]);

    return (
            <Group>
                {seatConfig?.sections && seatConfig?.sections.length > 0 &&
                    <Group>
                        {seatConfig.sections.map((section) => (
                                <SeatMapSection key={section.id} section={section}/>
                        ))}
                    </Group>
                }
            </Group>
    );
}

export default SeatMapSeatConfig;