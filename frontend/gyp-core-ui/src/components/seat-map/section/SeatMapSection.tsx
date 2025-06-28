import React, { useEffect, useState } from "react";
import { Arc, Group, Rect, Text } from "react-konva";
import { Section } from "../../../models/generated/event-service-models";
import { useSeatMapContext } from "../context/SeatMapContext.tsx";
import SeatMapSeatedSection from "./seated/SeatMapSeatedSection.tsx";
import SeatMapStandingSection from "./standing/SeatMapStandingSection.tsx";
import SeatMapTableSection from "./table/SeatMapTableSection.tsx";

export interface SeatMapSectionProps {
    section: Section;
}

const SeatMapSection: React.FC<SeatMapSectionProps> = (props) => {
    const [section, setSection] = useState<Section>(props.section);
    const {seatTypeColors} = useSeatMapContext();

    useEffect(() => {
        const data = props.section;
        if (data) {
            setSection(data);
        }
    }, [props.section]);

    const positionX = section?.position?.x || 0;
    const positionY = section?.position?.y || 0;
    const width = section?.dimensions?.width || 0;
    const height = section?.dimensions?.height || 0;

    const renderSectionBase = () => {
        const isArc = section.isArc;

        if (isArc && section.arcProperties) {
            const arcProperties = section.arcProperties;
            const {
                centerX = 0,
                centerY = 0,
                radius = 0,
                startAngle = 0,
                endAngle = Math.PI * 2,
                thickness = 20
            } = arcProperties;

            const fillColor = seatTypeColors?.[section.ticketTypeId] || 'rgba(200, 200, 200, 0.1)';

            return (
                    <>
                        <Arc
                                x={centerX}
                                y={centerY}
                                innerRadius={radius - thickness}
                                outerRadius={radius}
                                angle={endAngle - startAngle}
                                rotation={startAngle}
                                fill={fillColor}
                                stroke='#BDC3C7'
                                strokeWidth={1}
                        />
                        <Text
                                x={centerX}
                                y={centerY - radius + 10}
                                text={section.name}
                                fontSize={14}
                                fontFamily="Arial"
                                fill="black"
                                fontStyle="bold"
                                offsetX={(section.name?.length || 6) * 4.5}
                                align="center"
                        />
                    </>
            );
        }

        // Regular rectangular section
        return (
                <>
                    <Rect
                            x={0}
                            y={0}
                            width={width}
                            height={height}
                            fill={seatTypeColors?.[section.ticketTypeId] || 'rgba(200, 200, 200, 0.1)'}
                            stroke='#BDC3C7'
                            strokeWidth={1}
                            cornerRadius={3}
                    />
                    <Text
                            x={10}
                            y={10}
                            text={section.name}
                            fontSize={14}
                            fontFamily='Arial'
                            fill='#2C3E50'
                            fontStyle='bold'
                    />
                </>
        );
    }

    const renderRows = () => {
        if (!section?.rows || section.rows.length === 0) return null;

        return section.rows.map((row) => (
                <SeatMapSeatedSection
                        key={row.id}
                        row={row}
                        sectionPosition={section.position}
                        sectionArcProperties={section.isArc ? section.arcProperties : undefined}
                />
        ));
    };

    const renderTables = () => {
        if (!section?.tables || section.tables.length === 0) return null;

        return section.tables.map((table) => (
                <SeatMapTableSection key={table.id} table={table}/>
        ));
    };

    switch (section.type) {
        case "SEATED":
            return (
                    <Group x={positionX} y={positionY}>
                        {renderSectionBase()}
                        {renderRows()}
                    </Group>
            );
        case "TABLE":
            return (
                    <Group x={positionX} y={positionY}>
                        {renderSectionBase()}
                        {renderTables()}
                    </Group>
            );
        case "STANDING":
            return <SeatMapStandingSection standingSection={section}/>;
        default:
            return null;
    }
}

export default SeatMapSection;