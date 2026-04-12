import React from "react";
import { Group, Path, Rect, Text } from "react-konva";
import { StageConfig } from "../../../models/generated/event-service-models";

interface VenueStageContainerProps {
    stageConfig?: StageConfig;
}

const VenueStageContainer: React.FC<VenueStageContainerProps> = ({stageConfig}) => {
    const shapeProps = {
        fill: "#2C3E50",
        stroke: "#34495E",
        strokeWidth: 2,
    };

    let positionX = stageConfig?.position?.x || 0;
    let positionY = stageConfig?.position?.y || 0;
    const width = stageConfig?.dimensions?.width || 0;
    const height = stageConfig?.dimensions?.height || 0;

    return (
            <>
                {stageConfig?.svgPath
                        ? <Path x={positionX}
                                y={positionY}
                                data={stageConfig.svgPath}
                                {...shapeProps}
                        />
                        : <Group x={positionX} y={positionY}>
                            <Rect x={0} y={0}
                                  width={width}
                                  height={height}
                                  cornerRadius={Number(stageConfig?.borderRadius || 0)}
                                  {...shapeProps}
                            />
                            <Text x={0} y={0}
                                  width={width}
                                  height={height}
                                  text={stageConfig?.name || "Stage"}
                                  fontSize={40}
                                  fontFamily="Arial"
                                  fill="white"
                                  fontStyle="bold"
                                  align="center"
                                  verticalAlign="middle"
                            />
                        </Group>
                }
            </>
    )
};

export default VenueStageContainer;
