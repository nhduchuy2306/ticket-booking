import React, { useEffect, useState } from "react";
import { Circle, Group, Text } from "react-konva";
import { Position, Seat, Section } from "../../../../../models/generated/event-service-models";
import { formatCountdown, getHoldCountdownSeconds } from "../../../../../utils/bookingSession.ts";
import { SeatSizes } from "../../../constants/SeatMapContants.ts";
import { SeatUtils } from "../../../utils/SeatUtils.ts";
import { useSeatMapViewerContext } from "../../context/SeatMapViewerContext.tsx";
import { EventUtils } from "../../utils/EventUtils.ts";

export interface SeatMapSeatedSeatProps {
    seat: Seat,
    rowName: string,
    rowPosition?: Position,
    isArcRow?: boolean,
    section?: Section
}

const SeatedSeat: React.FC<SeatMapSeatedSeatProps> = (props) => {
    const [seat, setSeat] = useState<Seat>(props.seat);
    const {selectedSeats, setSelectedSeats, setDraggable, showSeatNumbers} = useSeatMapViewerContext();

    useEffect(() => {
        const data = props.seat;
        if (data) {
            setSeat(data);
        }
    }, [props.seat]);

    const isSelected = selectedSeats.find((s) => s.seat.id === seat.id) !== undefined;
    const holdExpiresAt = (seat as Seat & { holdExpiresAt?: string }).holdExpiresAt;
    const holdCountdown = getHoldCountdownSeconds(holdExpiresAt);

    // Use the seat's predefined position for arc layouts
    const positionX = seat.position?.x || 0;
    const positionY = seat.position?.y || 0;

    return (
            <>
                <Group x={positionX} y={positionY}
                       onClick={(e) => EventUtils.handleClick(e, seat, isSelected, selectedSeats, setSelectedSeats, props?.section)}
                       onMouseDown={() => EventUtils.handleMouseDown(setDraggable)}
                       onMouseUp={() => EventUtils.handleMouseUp(setDraggable)}
                       onMouseEnter={(e) => EventUtils.handleSeatMouseEnterEvent(e, seat)}
                       onMouseLeave={(e) => EventUtils.handleSeatMouseLeaveEvent(e, seat)}>
                    <Circle
                            x={0}
                            y={0}
                            offsetX={-20}
                            offsetY={-20}
                            radius={SeatSizes.MEDIUM.size / 4}
                            fill={SeatUtils.getSeatColor(seat, isSelected)}
                            stroke="#676767"
                            strokeWidth={1}
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
                    {seat.status === "RESERVED" && holdCountdown > 0 && (
                            <Text
                                    x={0}
                                    y={SeatSizes.MEDIUM.size / 2}
                                    text={formatCountdown(holdCountdown)}
                                    fontSize={10}
                                    fill="#9A3412"
                                    align="center"
                                    width={SeatSizes.MEDIUM.size}
                            />
                    )}
                </Group>
            </>
    );
}

export default SeatedSeat;