import React from "react";
import { Group, Rect, Text } from "react-konva";
import {
    Section,
} from "../../../../models/generated/event-service-models";
import SeatedRowWrapper from "./seated-section/SeatedRowWrapper.tsx";
import StandingArea from "./standing-section/StandingArea.tsx";

export interface SectionContainerProps {
    section: Section;
    isSelected?: boolean;
    onSelect?: (section: Section) => void;
}

const SectionContainer: React.FC<SectionContainerProps> = ({section, isSelected, onSelect}) => {
    const positionX = section?.position?.x || 0;
    const positionY = section?.position?.y || 0;
    const width = section?.dimensions?.width || 0;
    const height = section?.dimensions?.height || 0;
        const fillColor = section.type === "SEATED" ? "rgba(37, 99, 235, 0.18)" : "rgba(16, 185, 129, 0.18)";

    if (section.type === "SEATED") {
        return (
                <Group x={positionX} y={positionY} onClick={() => onSelect?.(section)} onTap={() => onSelect?.(section)}>
                    <Rect
                            x={0}
                            y={0}
                            width={width}
                            height={height}
                            fill={fillColor}
                            opacity={0.6}
                            cornerRadius={section.borderRadius ? Number(section.borderRadius) : 0}
                            stroke={isSelected ? "#111827" : fillColor}
                            strokeWidth={isSelected ? 4 : 2}
                    />
                    <Text
                            x={0}
                            y={0}
                            width={width}
                            height={height}
                            text={section.name || ""}
                            fontSize={30}
                            fontFamily="Arial"
                            fill="#FFF"
                            fontStyle="bold"
                            align="left"
                            verticalAlign="top"
                            padding={10}
                            listening={false}
                            perfectDrawEnabled={false}
                    />
                    {section.rows?.map((row) => (
                            <SeatedRowWrapper
                                    key={row.id}
                                    row={row}
                                    labelPosition={section.labelPosition}
                                    section={section}
                            />
                    ))}
                </Group>
        );
    }

    return (
            <StandingArea
                    standingSection={section}
                    isSelected={isSelected}
                    fillColor={fillColor}
                    onSelect={onSelect}
            />
    );
};

export default SectionContainer;
