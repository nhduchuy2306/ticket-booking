import React from "react";
import { Circle, Group, Text } from "react-konva";
import { Seat } from "../../../../../models/generated/event-service-models";
import { SeatColors, SeatSizes } from "../../../constants/SeatMapContants.ts";
import { useSeatMapEditorContext } from "../../context/SeatMapEditorContext.tsx";

export interface SeatMapSeatedSeatProps {
    seat: Seat;
    seatIndex: number;
    seatSpacing: number;
    isSelected?: boolean;
}

const SeatedSeat: React.FC<SeatMapSeatedSeatProps> = ({seat, seatIndex, seatSpacing, isSelected}) => {
    const {showSeatNumbers} = useSeatMapEditorContext();

    return (
            <Group x={seatIndex * seatSpacing} y={0}>
                <Circle
                        x={0}
                        y={0}
                        offsetX={-20}
                        offsetY={-20}
                        radius={SeatSizes.MEDIUM.size / 4}
                        fill={isSelected ? SeatColors.SELECTED.color : SeatColors.AVAILABLE.color}
                        stroke="#676767"
                        strokeWidth={1.5}
                />
                {showSeatNumbers && <Text
                    x={0}
                    y={0}
                    text={seat.name || ""}
                    fontSize={12}
                    fill='#2C3E50'
                    align='center'
                    verticalAlign='middle'
                    width={SeatSizes.MEDIUM.size}
                    height={SeatSizes.MEDIUM.size}
                    listening={false}
                    perfectDrawEnabled={false}
                    fontStyle='bold'
                />}
            </Group>
    );
}

export default SeatedSeat;