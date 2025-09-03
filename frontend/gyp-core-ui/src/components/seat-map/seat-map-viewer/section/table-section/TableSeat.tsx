import Konva from "konva";
import React, { useEffect, useState } from "react";
import { Circle, Group, Text } from "react-konva";
import { Dimension, Position, Seat, TableShape } from "../../../../../models/generated/event-service-models";
import { SelectedType } from "../../../constants/SeatMapContants.ts";
import { useSeatMapContext } from "../../context/SeatMapContext.tsx";
import { EventUtils } from "../../utils/EventUtils.ts";
import { SeatUtils } from "../../utils/SeatUtils.ts";

interface TableSeatProps {
    seat: Seat,
    index?: number,
    rowName?: string,
    rowPosition?: Position,
    tableShape?: TableShape,
    tableDimensions?: Dimension,
    calculatedPosition?: Position
}

const TableSeat: React.FC<TableSeatProps> = (props) => {
    const [seat, setSeat] = useState<Seat>(props.seat);
    const {selectedSeats, setSelectedSeats, setDraggable, setSelectedType} = useSeatMapContext();

    useEffect(() => {
        const data = props.seat;
        if (data) {
            setSeat(data);
        }
    }, [props.seat]);

    const isSelected = selectedSeats.includes(seat.id);
    const seatRadius = 10;

    const seatX = props.calculatedPosition?.x || seat.position?.x || 0;
    const seatY = props.calculatedPosition?.y || seat.position?.y || 0;

    const handleTableClick = (e: Konva.KonvaEventObject<MouseEvent>) => {
        e.evt.preventDefault();
        e.evt.stopPropagation();

        if (setSelectedType) {
            setSelectedType({
                type: SelectedType.SEATED_SEAT.key,
                data: seat
            });
        }

        EventUtils.handleClick(e, seat, isSelected, selectedSeats, setSelectedSeats)
    }

    return (
            <Group
                    x={seatX}
                    y={seatY}
                    onClick={handleTableClick}
                    onMouseDown={() => EventUtils.handleMouseDown(setDraggable)}
                    onMouseUp={() => EventUtils.handleMouseUp(setDraggable)}
                    opacity={seat.visualStyle?.opacity || 1}
            >
                <Circle
                        radius={seatRadius}
                        fill={SeatUtils.getSeatColor(seat, isSelected)}
                />
                <Text
                        x={-6}
                        y={-6}
                        width={12}
                        height={12}
                        align="center"
                        verticalAlign="middle"
                        text={seat.name || ""}
                        fontSize={10}
                        fill="#000"
                        fontStyle="bold"
                        listening={false}
                />
            </Group>
    );
}

export default TableSeat;