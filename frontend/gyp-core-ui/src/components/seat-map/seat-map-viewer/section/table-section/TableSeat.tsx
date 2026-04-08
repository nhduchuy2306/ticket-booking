import React, { useEffect, useState } from "react";
import { Circle, Group, Text } from "react-konva";
import { Dimension, Position, Seat, Section, TableShape } from "../../../../../models/generated/event-service-models";
import { useSeatMapViewerContext } from "../../context/SeatMapViewerContext.tsx";
import { EventUtils } from "../../utils/EventUtils.ts";
import { SeatUtils } from "../../utils/SeatUtils.ts";
import { formatCountdown, getHoldCountdownSeconds } from "../../../../../utils/bookingSession.ts";

interface TableSeatProps {
    seat: Seat,
    index?: number,
    rowName?: string,
    rowPosition?: Position,
    tableShape?: TableShape,
    tableDimensions?: Dimension,
    calculatedPosition?: Position,
    section?: Section,
}

const TableSeat: React.FC<TableSeatProps> = (props) => {
    const [seat, setSeat] = useState<Seat>(props.seat);
    const {selectedSeats, setSelectedSeats, setDraggable} = useSeatMapViewerContext();

    useEffect(() => {
        const data = props.seat;
        if (data) {
            setSeat(data);
        }
    }, [props.seat]);

    const isSelected = selectedSeats.find((s) => s.seat.id === seat.id) !== undefined;
    const seatRadius = 10;
    const holdExpiresAt = seat.attributes?.holdExpiresAt as string | undefined;
    const holdCountdown = getHoldCountdownSeconds(holdExpiresAt);

    const seatX = props.calculatedPosition?.x || seat.position?.x || 0;
    const seatY = props.calculatedPosition?.y || seat.position?.y || 0;

    return (
            <Group
                    x={seatX}
                    y={seatY}
                    onClick={(e) => EventUtils.handleClick(e, seat, isSelected, selectedSeats, setSelectedSeats, props?.section)}
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
                {seat.status === "RESERVED" && holdCountdown > 0 && (
                    <Text
                        x={-18}
                        y={12}
                        width={36}
                        align="center"
                        text={formatCountdown(holdCountdown)}
                        fontSize={9}
                        fill="#9A3412"
                        listening={false}
                    />
                )}
            </Group>
    );
}

export default TableSeat;