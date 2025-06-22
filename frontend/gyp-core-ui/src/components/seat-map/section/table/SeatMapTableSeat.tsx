import Konva from "konva";
import React, { useEffect, useState } from "react";
import { Circle, Group, Text } from "react-konva";
import { Dimension, Position, Seat, TableShape } from "../../../../models/generated/event-service-models";
import { SeatColors } from "../../constants/SeatMapContants.ts";
import { useSeatMapContext } from "../../context/SeatMapContext.tsx";
import { handleClick, handleMouseDown, handleMouseUp } from "../../utils/SeatUtils.ts";

export interface SeatMapTableSeatProps {
    seat: Seat,
    index?: number,
    rowName?: string,
    rowPosition?: Position,
    tableShape?: TableShape,
    tableDimensions?: Dimension,
    calculatedPosition?: Position
}

const SeatMapTableSeat: React.FC<SeatMapTableSeatProps> = (props) => {
    const [seatData, setSeatData] = useState<Seat>(props.seat);
    const {selectedSeats, setSelectedSeats, setDraggable} = useSeatMapContext();

    useEffect(() => {
        const data = props.seat;
        if (data) {
            setSeatData(data);
        }
    }, [props.seat]);

    const isSelected = selectedSeats.includes(seatData.id);
    const seatColor = isSelected
            ? SeatColors.SELECTED.color
            : SeatColors[seatData.status || "AVAILABLE"].color;
    const seatRadius = 10;
    const isClickable = seatData.status !== 'BLOCKED' && seatData.status !== 'DISABLED';

    const seatX = props.calculatedPosition?.x || seatData.position?.x || 0;
    const seatY = props.calculatedPosition?.y || seatData.position?.y || 0;

    const handleSeatClick = (e: Konva.KonvaEventObject<MouseEvent>) => {
        if (!isClickable) return;
        handleClick(e, seatData, isSelected, selectedSeats, setSelectedSeats);
    };

    return (
            <Group
                    x={seatX}
                    y={seatY}
                    onClick={handleSeatClick}
                    onMouseDown={() => handleMouseDown(setDraggable)}
                    onMouseUp={() => handleMouseUp(setDraggable)}
                    opacity={seatData.visualStyle?.opacity || 1}
            >
                <Circle
                        radius={seatRadius}
                        fill={seatColor}
                        stroke={isSelected ? "#000" : "#666"}
                        strokeWidth={isSelected ? 2 : 1}
                />
                <Text
                        x={-6}
                        y={-6}
                        width={12}
                        height={12}
                        align="center"
                        verticalAlign="middle"
                        text={seatData.name || ""}
                        fontSize={10}
                        fill="#000"
                        fontStyle="bold"
                        listening={false}
                />
            </Group>
    );
}

export default SeatMapTableSeat;