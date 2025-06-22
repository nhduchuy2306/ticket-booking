import React, { useEffect, useState } from "react";
import { Group, Rect, Text } from "react-konva";
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
        return <>
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
        </>;
    }

    switch (section.type) {
        case "SEATED":
            return (
                    <Group x={positionX} y={positionY}>
                        {renderSectionBase()}
                        {section?.rows && section?.rows.length > 0 && section.rows.map((row) =>
                                <SeatMapSeatedSection key={row.id} row={row} sectionPosition={section.position}/>
                        )}
                    </Group>
            );
        case "TABLE":
            return (
                    <Group x={positionX} y={positionY}>
                        {renderSectionBase()}
                        {section?.tables && section?.tables.length > 0 && section.tables.map((table) =>
                                <SeatMapTableSection key={table.id} table={table}/>
                        )}
                    </Group>
            );
        case "STANDING":
            return <SeatMapStandingSection standingSection={section}/>;
        default:
            return null;
    }
}

export default SeatMapSection;