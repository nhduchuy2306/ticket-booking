import React, { useState } from "react";
import { Group, Text } from "react-konva";
import { Position, Row } from "../../../../models/generated/event-service-models";
import { useSeatMapContext } from "../../context/SeatMapContext.tsx";
import SeatMapSeatedSeat from "./SeatMapSeatedSeat.tsx";

export interface SeatMapRowProps {
    row: Row;
    sectionPosition?: Position;
}

const SeatMapSeatedSection: React.FC<SeatMapRowProps> = (props) => {
    const [row, setRow] = useState<Row>(props.row);
    const {showSeatNumbers} = useSeatMapContext();

    React.useEffect(() => {
        const data = props.row;
        if (data) {
            setRow(data);
        }
    }, [props.row]);

    const positionX = row?.position?.x || 0;
    const positionY = row?.position?.y || 0;

    return (
            <Group x={positionX} y={positionY}>
                {showSeatNumbers &&
                    <Text
                        x={0}
                        y={0}
                        offsetX={positionX + 45}
                        offsetY={-30 / 2}
                        text={row.name}
                        fontSize={13}
                        fontFamily='Arial'
                        fill='#7F8C8D'
                        fontStyle='bold'
                    />
                }
                {row?.seats && row?.seats.length > 0 && row?.seats.map((seat) =>
                        <SeatMapSeatedSeat
                                key={seat.id}
                                seat={seat}
                                rowName={row.name}
                                rowPosition={row.position}
                        />
                )}
            </Group>
    );
}

export default SeatMapSeatedSection;