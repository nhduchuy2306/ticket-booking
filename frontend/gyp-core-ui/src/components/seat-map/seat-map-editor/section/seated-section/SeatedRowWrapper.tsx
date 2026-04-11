import React from "react";
import { Group, Text } from "react-konva";
import { Row, RowLabelPosition, Section } from "../../../../../models/generated/event-service-models";
import SeatedSeat from "./SeatedSeat.tsx";

export interface SeatMapSeatedSectionProps {
    row: Row;
    labelPosition?: RowLabelPosition;
    section?: Section;
}

const SeatedRowWrapper: React.FC<SeatMapSeatedSectionProps> = ({row, labelPosition, section}) => {
    const positionX = row?.position?.x || 0;
    const positionY = row?.position?.y || 0;
    const sectionPositionX = section?.position?.x || 0;
    const sectionDimensionsWidth = section?.dimensions?.width || 0;
    const labelOffsetX = labelPosition === "RIGHT" ? sectionPositionX - (sectionDimensionsWidth + 10) : sectionPositionX + 40;
    const labelAlign = labelPosition === "RIGHT" ? "left" : "right";

    return (
            <Group x={positionX} y={positionY}>
                <Text
                        x={0}
                        y={0}
                        offsetX={labelOffsetX}
                        offsetY={-10}
                        text={row.name}
                        fontSize={25}
                        fontFamily='Arial'
                        fill='#7F8C8D'
                        fontStyle='bold'
                        align={labelAlign}
                />
                {row?.seats?.map((seat, seatIndex) => (
                        <SeatedSeat
                                key={seat.id}
                                seat={seat}
                                seatIndex={seatIndex}
                                seatSpacing={row.seatSpacing || 0}
                                isSelected={false}
                        />
                ))}
            </Group>
    );
}

export default SeatedRowWrapper;