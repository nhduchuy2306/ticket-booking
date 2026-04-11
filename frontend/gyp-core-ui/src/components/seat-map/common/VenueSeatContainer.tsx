import React, { useEffect, useState } from "react";
import { Group } from "react-konva";
import { SeatConfig, Section } from "../../../models/generated/event-service-models";

interface VenueSeatContainerProps {
    seatConfig?: SeatConfig;
    children?: (section: Section) => React.ReactNode;
}

const VenueSeatContainer: React.FC<VenueSeatContainerProps> = ({seatConfig, children}) => {
    const [seatConfigData, setSeatConfigData] = useState<SeatConfig | undefined>(seatConfig);

    useEffect(() => {
        const data = seatConfig;
        if (data) {
            setSeatConfigData(data);
        }
    }, [seatConfig]);

    return (
            <Group>
                {seatConfigData?.sections && seatConfigData?.sections.length > 0 &&
                    <Group>
                        {seatConfigData.sections.map((section) => (
                                <React.Fragment key={section.id}>
                                    {children ? children(section) : null}
                                </React.Fragment>
                        ))}
                    </Group>
                }
            </Group>
    );
}

export default VenueSeatContainer;