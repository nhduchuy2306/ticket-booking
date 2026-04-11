import React, { useEffect, useState } from "react";
import { Group, Text } from "react-konva";
import { Position, Row, RowLabelPosition } from "../../../../../models/generated/event-service-models";
import SeatedSeat from "./SeatedSeat.tsx";

export interface SeatMapSeatedSectionProps {
    row: Row;
    sectionPosition?: Position;
    labelPosition?: RowLabelPosition,
}

const SeatedRowWrapper: React.FC<SeatMapSeatedSectionProps> = ({row, sectionPosition}) => {
    const [rowData, setRowData] = useState<Row>(row);

    useEffect(() => {
        if (row) {
            setRowData(row);
        }
    }, [row]);

    const positionX = rowData?.position?.x || 0 + (sectionPosition?.x || 0);
    const positionY = rowData?.position?.y || 0 + (sectionPosition?.y || 0);

    return (
            <Group x={positionX} y={positionY}>
                <Text
                        x={0}
                        y={0}
                        offsetX={positionX + 45}
                        offsetY={-10}
                        text={rowData.name}
                        fontSize={25}
                        fontFamily='Arial'
                        fill='#7F8C8D'
                        fontStyle='bold'
                />
                {rowData?.seats?.map((seat) => (
                        <SeatedSeat
                                key={seat.id}
                                seat={seat}
                                rowName={rowData.name}
                                rowPosition={rowData.position}
                        />
                ))}
            </Group>
    );
}

export default SeatedRowWrapper;