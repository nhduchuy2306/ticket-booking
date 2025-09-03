import React, { useEffect } from "react";
import { Group } from "react-konva";
import { SeatConfig } from "../../../models/generated/event-service-models";
import SectionContainer from "../section/SectionContainer.tsx";

interface VenueSeatContainerProps {
    seatConfig?: SeatConfig;
}

const VenueSeatContainer: React.FC<VenueSeatContainerProps> = (props) => {
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
                                <SectionContainer key={section.id} section={section}/>
                        ))}
                    </Group>
                }
            </Group>
    );
}

export default VenueSeatContainer;