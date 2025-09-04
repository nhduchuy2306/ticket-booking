import Konva from "konva";
import React, { useEffect, useState } from "react";
import { Group, Rect, Text } from "react-konva";
import { Section } from "../../../../../models/generated/event-service-models";
import { SelectedType } from "../../../constants/SeatMapContants.ts";
import { useSeatMapEditorContext } from "../../context/SeatMapEditorContext.tsx";

interface StandingAreaProps {
    standingSection: Section
}

const StandingArea: React.FC<StandingAreaProps> = (props) => {
    const [standingSection, setStandingSection] = useState<Section>(props.standingSection);
    const {setSelectedType} = useSeatMapEditorContext();

    useEffect(() => {
        const data = props.standingSection;
        if (data) {
            setStandingSection(data);
        }
    }, [props.standingSection]);

    const positionX = standingSection?.position?.x || 0;
    const positionY = standingSection?.position?.y || 0;
    const rotation = standingSection?.rotation || 0;
    const dimensionsWidth = standingSection?.dimensions?.width || 0;
    const dimensionsHeight = standingSection?.dimensions?.height || 0;
    const capacity = standingSection?.capacity || 0;
    const sectionName = standingSection?.name || "Standing Section";

    const fillColor = "#4CAF50";
    const borderColor = "#2E7D32";
    const textColor = "#2C3E50";

    const handleStandingAreaClick = (evt: Konva.KonvaEventObject<MouseEvent>) => {
        evt.evt.preventDefault();
        evt.evt.stopPropagation();

        if (setSelectedType) {
            setSelectedType({
                type: SelectedType.SEATED_SEAT.key,
                data: standingSection
            });
        }
    }

    return (
            <Group x={positionX} y={positionY} onClick={handleStandingAreaClick}>
                <Rect
                        width={dimensionsWidth}
                        height={dimensionsHeight}
                        fillLinearGradientStartPoint={{x: 0, y: 0}}
                        fillLinearGradientEndPoint={{x: dimensionsWidth, y: dimensionsHeight}}
                        fillLinearGradientColorStops={[0, fillColor, 1, "#66BB6A"]}
                        rotation={rotation}
                        stroke={borderColor}
                        strokeWidth={1}
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