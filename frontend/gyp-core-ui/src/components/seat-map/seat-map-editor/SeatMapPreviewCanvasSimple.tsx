import Konva from "konva";
import React, { useMemo, useRef, useState } from "react";
import { Group, Layer, Rect, Stage, Text } from "react-konva";
import { SeatConfig, Section, StageConfig, } from "../../../models/generated/event-service-models";

export interface SeatMapPreviewCanvasSimpleProps {
    stageConfig: StageConfig;
    seatConfig: SeatConfig;
    ticketTypeColors?: Record<string, string>;
}

const WIDTH = 1000;
const HEIGHT = 500;

const SeatMapPreviewCanvasSimple: React.FC<SeatMapPreviewCanvasSimpleProps> = ({
                                                                                   stageConfig,
                                                                                   seatConfig,
                                                                                   ticketTypeColors
                                                                               }) => {
    const stageRef = useRef<Konva.Stage>(null);
    const layerRef = useRef<Konva.Layer>(null);
    const [zoomLevel, setZoomLevel] = useState(0.5);

    const handleWheel = (event: Konva.KonvaEventObject<WheelEvent>) => {
        event.evt.preventDefault();
        event.evt.stopPropagation();

        const stage = stageRef.current;
        const layer = layerRef.current;
        if (!stage || !layer) return null;

        const oldScale = layer.scaleX();
        const pointer = stage.getPointerPosition();
        if (!pointer) return null;

        const mousePointTo = {
            x: (pointer.x - layer.x()) / oldScale,
            y: (pointer.y - layer.y()) / oldScale,
        };

        const direction = event.evt.deltaY > 0 ? -1 : 1;
        const factor = 1.1;
        const newScale = direction > 0 ? oldScale * factor : oldScale / factor;

        // Apply zoom
        const clampedScale = Math.max(0.5, Math.min(1, newScale));
        layer.scale({x: clampedScale, y: clampedScale});
        setZoomLevel(clampedScale);

        const newPos = {
            x: pointer.x - mousePointTo.x * clampedScale,
            y: pointer.y - mousePointTo.y * clampedScale,
        };

        layer.position(newPos);
        layer.batchDraw();
    };

    const sectionElements = useMemo(() => seatConfig.sections || [], [seatConfig.sections]);

    const renderStage = () => {
        return (
                <Group x={150} y={0}>
                    <Rect x={0} y={0}
                          width={500}
                          height={100}
                          cornerRadius={10}
                          fill="#2C3E50"
                          stroke="#34495E"
                          strokeWidth={2}
                    />
                    <Text x={0} y={0}
                          width={500}
                          height={100}
                          text={stageConfig?.name || "Stage"}
                          fontSize={40}
                          fontFamily="Arial"
                          fill="white"
                          fontStyle="bold"
                          align="center"
                          verticalAlign="middle"
                    />
                </Group>
        );
    }

    const renderSeatedSeat = (section: Section, index: number) => {
        const width = 800;
        const height = 100;
        const fillColor = ticketTypeColors?.[section?.id] || "rgba(37, 99, 235, 0.18)";
        return (
                <Group x={0} y={150 + 150 * index}>
                    <Rect
                            x={0}
                            y={0}
                            width={width}
                            height={height}
                            fill={fillColor}
                            opacity={0.6}
                            cornerRadius={section.borderRadius ? Number(section.borderRadius) : 0}
                            stroke={fillColor}
                            strokeWidth={2}
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
                            align="center"
                            verticalAlign="middle"
                            padding={10}
                            listening={false}
                            perfectDrawEnabled={false}
                    />
                </Group>
        );
    }

    const renderStandingSeat = (section: Section, index: number) => {
        const width = 800;
        const height = 100;
        const fillColor = ticketTypeColors?.[section?.id] || "rgba(16, 185, 129, 0.18)";
        return (
                <Group x={0} y={150 + 150 * index}>
                    <Rect
                            width={width}
                            height={height}
                            fillLinearGradientStartPoint={{x: 0, y: 0}}
                            fillLinearGradientEndPoint={{x: width, y: height}}
                            fillLinearGradientColorStops={[0, fillColor, 1, "#66BB6A"]}
                            stroke="#2E7D32"
                            strokeWidth={1}
                            cornerRadius={3}
                    />
                    <Text
                            y={height / 2 - 30}
                            width={width}
                            align="center"
                            text={section?.name}
                            fontSize={18}
                            fill="#2C3E50"
                            fontStyle="bold"
                    />
                    <Text
                            y={height / 2}
                            width={width}
                            align="center"
                            text={`Capacity: ${section?.capacity}`}
                            fontSize={14}
                            fill="#2C3E50"
                    />
                </Group>
        );
    }

    return (
            <div className="relative h-full w-full min-w-0 overflow-hidden border border-slate-200 bg-gradient-to-b from-slate-50 to-indigo-50 min-h-0 flex-1">
                <Stage
                        ref={stageRef}
                        width={WIDTH}
                        height={HEIGHT}
                        onWheel={handleWheel}
                        draggable={true}
                        className="h-full w-full cursor-grab bg-transparent"
                >
                    <Layer
                            ref={layerRef}
                            x={WIDTH / 2 - (WIDTH / 2) * zoomLevel}
                            y={HEIGHT / 2 - (HEIGHT / 2) * zoomLevel}
                            scale={{x: zoomLevel, y: zoomLevel}}
                    >
                        {/* Render Stage */}
                        {renderStage()}

                        {/* Render Section */}
                        {sectionElements.map((section, index) => {
                            return section.type === "SEATED" ? renderSeatedSeat(section, index) : renderStandingSeat(section, index);
                        })}
                    </Layer>
                </Stage>
            </div>
    );
};

export default SeatMapPreviewCanvasSimple;
