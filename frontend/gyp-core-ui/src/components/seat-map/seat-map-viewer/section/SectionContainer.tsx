import React, { useEffect, useState } from "react";
import { Group, Rect, Text } from "react-konva";
import { Section } from "../../../../models/generated/event-service-models";
import SeatedRowWrapper from "../../common/section/SeatedRowWrapper.tsx";
import { SeatUtils } from "../../utils/SeatUtils.ts";
import { useSeatMapViewerContext } from "../context/SeatMapViewerContext.tsx";
import { EventUtils } from "../utils/EventUtils.ts";
import SeatedSeat from "./seated-section/SeatedSeat.tsx";
import StandingArea from "./standing-section/StandingArea.tsx";

export interface SectionContainerProps {
    section: Section;
}

const SectionContainer: React.FC<SectionContainerProps> = ({section}) => {
    const [sectionData, setSectionData] = useState<Section>(section);
    const {ticketTypes} = useSeatMapViewerContext();

    useEffect(() => {
        if (section) {
            setSectionData(section);
        }
    }, [section]);

    const positionX = sectionData?.position?.x || 0;
    const positionY = sectionData?.position?.y || 0;
    const width = sectionData?.dimensions?.width || 0;
    const height = sectionData?.dimensions?.height || 0;

    switch (section.type) {
        case "SEATED":
            const fillColor = SeatUtils.getTicketTypeById(ticketTypes || [], sectionData?.ticketTypeId)?.color || 'rgba(200, 200, 200, 0.1)';
            return (
                    <Group x={positionX} y={positionY}>
                        <Group onMouseEnter={EventUtils.handleItemMouseEnterEvent}
                               onMouseLeave={EventUtils.handleItemMouseLeaveEvent}>
                            <Rect x={0} y={0}
                                  width={width}
                                  height={height}
                                  fill={fillColor}
                                  cornerRadius={sectionData.borderRadius ? parseInt(sectionData.borderRadius) : 0}
                            />
                            <Text
                                    x={0}
                                    y={0}
                                    width={width}
                                    height={height}
                                    text={sectionData.name}
                                    fontSize={25}
                                    fontFamily="Arial"
                                    fill="#FFF"
                                    fontStyle="bold"
                                    align="left"
                                    verticalAlign="top"
                                    padding={10}
                                    listening={false}
                                    perfectDrawEnabled={false}
                            />
                        </Group>
                        <Group x={0} y={0}>
                            {sectionData?.rows?.map((row) => (
                                    <SeatedRowWrapper
                                            key={row.id} row={row}
                                            sectionPosition={sectionData.position}
                                            labelPosition={sectionData.labelPosition}
                                    >
                                        {(seat, rowName, rowPosition) => <SeatedSeat
                                                key={seat.id}
                                                seat={seat}
                                                rowName={rowName}
                                                rowPosition={rowPosition}
                                        />}
                                    </SeatedRowWrapper>
                            ))}
                        </Group>
                    </Group>
            );
        case "STANDING":
            return <StandingArea standingSection={section}/>;
        default:
            return null;
    }
}

export default SectionContainer;