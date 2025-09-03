import Konva from "konva";
import React, { useEffect, useState } from "react";
import { Arc, Group, Rect, Text } from "react-konva";
import { Section } from "../../../../models/generated/event-service-models";
import { SelectedType } from "../../constants/SeatMapContants.ts";
import { useSeatMapContext } from "../context/SeatMapContext.tsx";
import { EventUtils } from "../utils/EventUtils.ts";
import { SeatUtils } from "../utils/SeatUtils.ts";
import SeatedRowWrapper from "./seated-section/SeatedRowWrapper.tsx";
import StandingArea from "./standing-section/StandingArea.tsx";
import TableRowWrapper from "./table-section/TableRowWrapper.tsx";

export interface SectionContainerProps {
    section: Section;
}

const SectionContainer: React.FC<SectionContainerProps> = (props) => {
    const [section, setSection] = useState<Section>(props.section);
    const {seatTypes, showSeatNumbers, setDraggable, setSelectedType} = useSeatMapContext();

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

        const fillColor = SeatUtils.getTicketTypeById(seatTypes || [], section?.ticketTypeId)?.color || 'rgba(200, 200, 200, 0.1)';

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

            return (
                    <Group>
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
                        {!showSeatNumbers && <Text
                            x={centerX}
                            y={centerY - radius + 10}
                            text={section.name}
                            fontSize={14}
                            fontFamily="Arial"
                            fill="black"
                            fontStyle="bold"
                            offsetX={(section.name?.length || 6) * 4.5}
                            align="center"
                        />}
                    </Group>
            );
        }

        // Regular rectangular section
        return (
                <Group>
                    <Rect x={0} y={0}
                          width={width}
                          height={height}
                          fill={fillColor}
                          cornerRadius={3}
                    />
                    {!showSeatNumbers && <Text
                        x={0}
                        y={0}
                        width={width}
                        height={height}
                        text={section.name}
                        fontSize={25}
                        fontFamily="Arial"
                        fill="#FFF"
                        fontStyle="bold"
                        align="center"
                        verticalAlign="middle"
                    />}
                </Group>
        );
    }

    const renderSeatedRows = () => {
        if (!section?.rows || section.rows.length === 0) return null;

        return section.rows.map((row) => (
                <SeatedRowWrapper
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
                <TableRowWrapper key={table.id} table={table}/>
        ));
    };

    const handleSectionClick = (evt: Konva.KonvaEventObject<MouseEvent>) => {
        evt.evt.preventDefault();
        evt.evt.stopPropagation();

        if (setSelectedType) {
            setSelectedType({
                type: SelectedType.SEATED_SEAT.key,
                data: section
            });
        }
    }

    switch (section.type) {
        case "SEATED":
            return (
                    <Group x={positionX} y={positionY}
                           onMouseDown={() => EventUtils.handleMouseDown(setDraggable)}
                           onMouseUp={() => EventUtils.handleMouseUp(setDraggable)}
                           onClick={handleSectionClick}>
                        {renderSectionBase()}
                        {renderSeatedRows()}
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
            return <StandingArea standingSection={section}/>;
        default:
            return null;
    }
}

export default SectionContainer;