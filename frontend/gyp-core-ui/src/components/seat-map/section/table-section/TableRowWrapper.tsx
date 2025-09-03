import React, { useEffect, useState } from "react";
import { Circle, Group, Rect, Text } from "react-konva";
import { Position, Table } from "../../../../models/generated/event-service-models";
import { TableColors } from "../../constants/SeatMapContants.ts";
import { SeatUtils } from "../../utils/SeatUtils.ts";
import TableSeat from "./TableSeat.tsx";

interface TableRowWrapperProps {
    table: Table
}

const TableRowWrapper: React.FC<TableRowWrapperProps> = (props) => {
    const [table, setTable] = useState<Table>(props.table);

    useEffect(() => {
        const data = props.table;
        if (data) {
            setTable(data);
        }
    }, [props.table]);

    const {position, dimensions, shape, capacity, seats = []} = table;
    const positionX = position?.x || 0;
    const positionY = position?.y || 0;
    const width = dimensions?.width || 0;
    const height = dimensions?.height || 0;

    const renderTable = () => {
        switch (shape) {
            case "RECTANGLE":
                return (
                        <Rect
                                width={width}
                                height={height}
                                fill={TableColors.RECTANGLE.color}
                                opacity={0.7}
                                cornerRadius={3}
                        />
                );
            case "ROUND":
                const radius = Math.min(width, height) / 2;
                return (
                        <Circle
                                x={width / 2}
                                y={height / 2}
                                radius={radius}
                                fill={TableColors.ROUND.color}
                                opacity={0.7}
                        />
                );
            default:
                return null;
        }
    };

    const renderCapacityText = () => (
            <Text
                    x={width / 2}
                    y={height / 2}
                    width={width}
                    align="center"
                    text={`Cap: ${capacity}`}
                    verticalAlign="middle"
                    offsetX={width / 2}
                    fontSize={12}
                    fill="#000"
                    fontStyle="bold"
            />
    );

    const renderSeats = () => {
        if (!seats || seats.length === 0) return null;

        let seatPositions: Position[] = [];

        if (shape === "RECTANGLE") {
            seatPositions = SeatUtils.calculateRectangleSeats(seats, width, height);
        } else if (shape === "ROUND") {
            seatPositions = SeatUtils.calculateCircleSeats(seats, width, height);
        } else {
            return renderCustomShapeWarning();
        }

        return seats.map((seat, index) => (
                <TableSeat
                        key={seat.id}
                        index={index}
                        seat={seat}
                        rowName={table.name}
                        rowPosition={table.position}
                        tableShape={shape}
                        tableDimensions={{width, height}}
                        calculatedPosition={seatPositions[index]}
                />
        ));
    };

    const renderCustomShapeWarning = () => (
            <Group x={positionX} y={positionY}>
                <Text
                        x={width / 2}
                        y={height / 2}
                        width={width}
                        align="center"
                        text="Does not support custom shape"
                        verticalAlign="middle"
                        offsetX={width / 2}
                        fontSize={12}
                        fill="#FF0000"
                />
            </Group>
    );

    return (
            <Group x={positionX} y={positionY}>
                {renderTable()}
                {renderCapacityText()}
                {renderSeats()}
            </Group>
    );
}

export default TableRowWrapper;