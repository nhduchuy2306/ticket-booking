import React, { useEffect, useState } from "react";
import { Group, Path, Rect, Text } from "react-konva";
import { StageConfig } from "../../../models/generated/event-service-models";

interface VenueStageContainerProps {
    stageConfig?: StageConfig;
}

const VenueStageContainer: React.FC<VenueStageContainerProps> = ({stageConfig}) => {
    const [stage, setStage] = useState<StageConfig>({});

    useEffect(() => {
        if (stageConfig) {
            setStage(stageConfig);
            console.log("Stage config updated:", stageConfig);
        }
    }, [stageConfig]);

    const shapeProps = {
        fill: "#2C3E50",
        stroke: "#34495E",
        strokeWidth: 2,
    };

    let positionX = stage.position?.x || 0;
    let positionY = stage.position?.y || 0;
    const width = stage.dimensions?.width || 0;
    const height = stage.dimensions?.height || 0;

    return (
            <>
                {stage.svgPath
                        ? <Path x={positionX}
                                y={positionY}
                                data={stage.svgPath}
                                {...shapeProps}
                        />
                        : <Group x={positionX} y={positionY}>
                            <Rect x={0} y={0}
                                  width={width}
                                  height={height}
                                  cornerRadius={stage.borderRadius ? parseInt(stage.borderRadius) : 0}
                                  {...shapeProps}
                            />
                            <Text x={0} y={0}
                                  width={width}
                                  height={height}
                                  text={stage.name || "Stage"}
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
