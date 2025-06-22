import React, { useEffect, useState } from "react";
import { Group, Rect, Text } from "react-konva";
import { Position, Seat } from "../../../../models/generated/event-service-models";
import { SeatColors, SeatSizes } from "../../constants/SeatMapContants.ts";
import { useSeatMapContext } from "../../context/SeatMapContext.tsx";
import { handleClick, handleMouseDown, handleMouseUp } from "../../utils/SeatUtils.ts";

export interface SeatMapSeatedSeatProps {
    seat: Seat,
    rowName: string,
    rowPosition?: Position,
}

const SeatMapSeatedSeat: React.FC<SeatMapSeatedSeatProps> = (props) => {
    const [seat, setSeat] = useState<Seat>(props.seat);
    const {selectedSeats, setSelectedSeats, setDraggable} = useSeatMapContext();

    useEffect(() => {
        const data = props.seat;
        if (data) {
            setSeat(data);
        }
    }, [props.seat]);

    const isSelected = selectedSeats.includes(seat.id);
    const seatColor = isSelected ? SeatColors.SELECTED.color : SeatColors[seat.status || "AVAILABLE"].color;
    const seatSize = SeatSizes.MEDIUM.size;
    const positionX = seat.position?.x || 0;
    const positionY = seat.position?.y || 0;

    return (
            <Group x={positionX}
                   y={positionY}
                   onClick={(e) => handleClick(e, seat, isSelected, selectedSeats, setSelectedSeats)}
                   onMouseDown={() => handleMouseDown(setDraggable)}
                   onMouseUp={() => handleMouseUp(setDraggable)}
            >
                <Rect
                        x={0}
                        y={0}
                        width={seatSize}
                        height={seatSize}
                        fill={seatColor}
                        opacity={seat.status === "SOLD" ? 0.5 : 1}
                />
                <Text
                        x={0}
                        y={0}
                        text={seat.name || ""}
                        fontSize={12}
                        fontFamily='Arial'
                        fill='#2C3E50'
                        align='center'
                        verticalAlign='middle'
                        width={seatSize}
                        height={seatSize}
                        listening={false}
                        perfectDrawEnabled={false}
                        fontStyle='bold'
                />
            </Group>
    );
}

export default SeatMapSeatedSeat;