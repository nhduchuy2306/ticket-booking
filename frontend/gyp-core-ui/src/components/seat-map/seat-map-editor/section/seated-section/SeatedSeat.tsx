import React, { useEffect, useState } from "react";
import { Circle, Group, Text } from "react-konva";
import { Seat } from "../../../../../models/generated/event-service-models";
import { SeatColors, SeatSizes } from "../../../constants/SeatMapContants.ts";
import { useSeatMapEditorContext } from "../../context/SeatMapEditorContext.tsx";
import { EventUtils } from "../../utils/EventUtils.ts";

export interface SeatMapSeatedSeatProps {
    seat: Seat,
}

const SeatedSeat: React.FC<SeatMapSeatedSeatProps> = ({seat}) => {
    const [seatData, setSeatData] = useState<Seat>(seat);
    const {setDraggable, showSeatNumbers} = useSeatMapEditorContext();

    useEffect(() => {
        if (seat) {
            setSeatData(seat);
        }
    }, [seat]);

    const positionX = seatData.position?.x || 0;
    const positionY = seatData.position?.y || 0;

    return (
            <Group x={positionX} y={positionY}
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
                        fill={SeatColors.AVAILABLE.color}
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
            </Group>
    );
}

export default SeatedSeat;