import React, { useState } from "react";
import { Arc, Group, Text } from "react-konva";
import { ArcProperties, Position, Row } from "../../../../models/generated/event-service-models";
import { useSeatMapContext } from "../../context/SeatMapContext.tsx";
import SeatedSeat from "./SeatedSeat.tsx";

export interface SeatMapSeatedSectionProps {
    row: Row;
    sectionPosition?: Position;
    sectionArcProperties?: ArcProperties;
}

const SeatedRowWrapper: React.FC<SeatMapSeatedSectionProps> = (props) => {
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

    const renderRowBase = () => {
        const isArc = row.isArc;

        if (isArc && row.arcProperties) {
            const arcProperties = row.arcProperties;
            const {
                centerX = 0,
                centerY = 0,
                radius = 0,
                startAngle = 0,
                endAngle = Math.PI * 2,
                thickness = 20
            } = arcProperties;

            // Convert degrees to radians if needed
            const startAngleRad = startAngle * Math.PI / 180;
            const endAngleRad = endAngle * Math.PI / 180;
            const angleSpan = endAngleRad - startAngleRad;

            return (
                    <Arc
                            x={centerX}
                            y={centerY}
                            innerRadius={radius - thickness}
                            outerRadius={radius}
                            angle={angleSpan}
                            rotation={startAngleRad * 180 / Math.PI}
                            fill="rgba(100, 100, 100, 0.1)"
                            stroke='#E0E0E0'
                            strokeWidth={0.5}
                    />
            );
        }

        return null; // No base for regular rows
    };

    const renderRowLabel = () => {
        if (!showSeatNumbers) return null;

        // For arc rows, position the label differently
        if (row.isArc && row.arcProperties) {
            const {centerX = 0, centerY = 0, radius = 0, startAngle = 0} = row.arcProperties;
            const startAngleRad = startAngle * Math.PI / 180;

            // Position label at the start of the arc
            const labelX = centerX + (radius - 30) * Math.cos(startAngleRad);
            const labelY = centerY + (radius - 30) * Math.sin(startAngleRad);

            return (
                    <Text
                            x={labelX}
                            y={labelY}
                            text={row.name}
                            fontSize={13}
                            fontFamily='Arial'
                            fill='#7F8C8D'
                            fontStyle='bold'
                            offsetX={15}
                            offsetY={6}
                    />
            );
        }

        // Regular row label
        return (
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
        );
    };

    const renderSeats = () => {
        if (!row?.seats || row.seats.length === 0) return null;

        return row.seats.map((seat) => (
                <SeatedSeat
                        key={seat.id}
                        seat={seat}
                        rowName={row.name}
                        rowPosition={row.position}
                        isArcRow={row.isArc}
                        rowArcProperties={row.arcProperties}
                />
        ));
    };

    return (
            <Group x={positionX} y={positionY}>
                {renderRowBase()}
                {renderRowLabel()}
                {renderSeats()}
            </Group>
    );
}

export default SeatedRowWrapper;