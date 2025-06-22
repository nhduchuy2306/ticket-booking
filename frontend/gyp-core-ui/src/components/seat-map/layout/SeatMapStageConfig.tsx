import React, { useEffect, useState } from "react";
import { Arc, Circle, Group, Line, Rect, Shape, Text } from "react-konva";
import { StageConfig } from "../../../models/generated/event-service-models";

export interface SeatMapStageConfigProps {
    stageConfig?: StageConfig;
}

const SeatMapStageConfig: React.FC<SeatMapStageConfigProps> = ({stageConfig}) => {
    const [stage, setStage] = useState<StageConfig>({});

    useEffect(() => {
        if (stageConfig) {
            setStage(stageConfig);
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

    let centerX = positionX;
    let centerY = positionY;

    const renderShape = () => {
        switch (stage.shape) {
            case "RECTANGLE":
                centerX += width / 2;
                centerY += height / 2;
                return <Rect x={positionX} y={positionY} width={width} height={height}
                             cornerRadius={5} {...shapeProps} />;
            case "CIRCULAR":
            case "ARENA":
                return <Circle x={positionX} y={positionY} radius={width / 2} {...shapeProps} />;
            case "SEMICIRCLE":
                positionX += 5;
                positionY += 5;
                return <Arc x={positionX} y={positionY} innerRadius={0} outerRadius={width / 2} angle={180}
                            rotation={360} {...shapeProps} />;
            case "THRUST":
                centerX += width / 2;
                centerY += height / 2;
                return (
                        <Line
                                points={[
                                    positionX, positionY,
                                    positionX + width, positionY,
                                    positionX + width * 0.8, positionY + height,
                                    positionX + width * 0.2, positionY + height,
                                ]}
                                closed
                                {...shapeProps}
                        />
                );

            case "CUSTOM":
                centerX += 50;
                centerY += 30;
                return (
                        <Shape
                                sceneFunc={(context, shape) => {
                                    context.beginPath();
                                    context.moveTo(positionX, positionY);
                                    context.lineTo(positionX + 100, positionY + 30);
                                    context.lineTo(positionX + 80, positionY + 60);
                                    context.closePath();
                                    context.fillStrokeShape(shape);
                                }}
                                {...shapeProps}
                        />
                );

            default:
                return null;
        }
    };

    const shapeElement = renderShape();
    if (!shapeElement) {
        return null;
    }

    return (
            <Group>
                {shapeElement}
                <Text
                        x={centerX}
                        y={centerY}
                        text={stage.name || "Stage"}
                        fontSize={16}
                        fontFamily="Arial"
                        fill="white"
                        fontStyle="bold"
                        offsetX={(stage.name?.length || 5) * 4.5}
                        offsetY={8}
                />
            </Group>
    );
};

export default SeatMapStageConfig;
