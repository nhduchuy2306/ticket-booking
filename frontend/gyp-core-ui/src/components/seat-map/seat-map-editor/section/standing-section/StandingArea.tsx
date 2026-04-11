import React from "react";
import { Group, Rect, Text } from "react-konva";
import { Section } from "../../../../../models/generated/event-service-models";

interface StandingAreaProps {
    standingSection: Section;
    isSelected?: boolean;
    fillColor?: string;
        onSelect?: (section: Section) => void;
}

const StandingArea: React.FC<StandingAreaProps> = ({standingSection, isSelected, fillColor, onSelect}) => {
    const positionX = standingSection?.position?.x || 0;
    const positionY = standingSection?.position?.y || 0;
    const dimensionsWidth = standingSection?.dimensions?.width || 0;
    const dimensionsHeight = standingSection?.dimensions?.height || 0;
    const capacity = standingSection?.capacity || 0;
    const sectionName = standingSection?.name || "Standing Section";

    const backgroundColor = fillColor || "#4CAF50";
    const borderColor = isSelected ? "#111827" : "#2E7D32";
    const textColor = "#2C3E50";

    return (
            <Group x={positionX} y={positionY} onClick={() => onSelect?.(standingSection)} onTap={() => onSelect?.(standingSection)}>
                <Rect
                        width={dimensionsWidth}
                        height={dimensionsHeight}
                        fillLinearGradientStartPoint={{x: 0, y: 0}}
                        fillLinearGradientEndPoint={{x: dimensionsWidth, y: dimensionsHeight}}
                        fillLinearGradientColorStops={[0, backgroundColor, 1, "#66BB6A"]}
                        stroke={borderColor}
                        strokeWidth={isSelected ? 3 : 1}
                        cornerRadius={3}
                />
                <Text
                        y={dimensionsHeight / 2 - 30}
                        width={dimensionsWidth}
                        align="center"
                        text={sectionName}
                        fontSize={18}
                        fill={textColor}
                        fontStyle="bold"
                />
                <Text
                        y={dimensionsHeight / 2}
                        width={dimensionsWidth}
                        align="center"
                        text={`Capacity: ${capacity}`}
                        fontSize={14}
                        fill={textColor}
                />
            </Group>
    );
}

export default StandingArea;