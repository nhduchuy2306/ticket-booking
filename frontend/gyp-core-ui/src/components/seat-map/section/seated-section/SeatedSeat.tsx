import Konva from "konva";
import React, { useEffect, useState } from "react";
import { Circle, Group, Text } from "react-konva";
import { ArcProperties, Position, Seat } from "../../../../models/generated/event-service-models";
import { SeatSizes, SelectedType } from "../../constants/SeatMapContants.ts";
import { useSeatMapContext } from "../../context/SeatMapContext.tsx";
import { EventUtils } from "../../utils/EventUtils.ts";
import { SeatUtils } from "../../utils/SeatUtils.ts";

export interface SeatMapSeatedSeatProps {
    seat: Seat,
    rowName: string,
    rowPosition?: Position,
    isArcRow?: boolean,
    rowArcProperties?: ArcProperties,
}

const SeatedSeat: React.FC<SeatMapSeatedSeatProps> = (props) => {
    const [seat, setSeat] = useState<Seat>(props.seat);
    const {selectedSeats, setSelectedSeats, setDraggable, showSeatNumbers, setSelectedType} = useSeatMapContext();

    useEffect(() => {
        const data = props.seat;
        if (data) {
            setSeat(data);
        }
    }, [props.seat]);

    const isSelected = selectedSeats.includes(seat.id);

    // Use the seat's predefined position for arc layouts
    const positionX = seat.position?.x || 0;
    const positionY = seat.position?.y || 0;

    const handleClick = (e: Konva.KonvaEventObject<MouseEvent>) => {
        e.evt.preventDefault();
        e.evt.stopPropagation();

        if (setSelectedType) {
            setSelectedType({
                type: SelectedType.SEATED_SEAT.key,
                data: seat
            });
        }
        EventUtils.handleClick(e, seat, isSelected, selectedSeats, setSelectedSeats);
    }

    return (
            <>
                {showSeatNumbers &&
                    <Group x={positionX} y={positionY}
                           onClick={handleClick}
                           onMouseDown={() => EventUtils.handleMouseDown(setDraggable)}
                           onMouseUp={() => EventUtils.handleMouseUp(setDraggable)}
                           onMouseEnter={EventUtils.handleMouseEnterEvent}
                           onMouseLeave={EventUtils.handleMouseLeaveEvent}>
                        <Circle
                            x={0}
                            y={0}
                            offsetX={-20}
                            offsetY={-20}
                            radius={SeatSizes.MEDIUM.size / 4}
                            fill={SeatUtils.getSeatColor(seat, isSelected)}
                        />
                        <Text
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
                        />
                    </Group>}
            </>
    );
}

export default SeatedSeat;