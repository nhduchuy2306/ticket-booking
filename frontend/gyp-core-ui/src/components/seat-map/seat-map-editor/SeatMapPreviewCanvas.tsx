import Konva from "konva";
import React, { useEffect, useMemo, useRef, useState } from "react";
import { Layer, Stage } from "react-konva";
import { SeatConfig, StageConfig, } from "../../../models/generated/event-service-models";
import VenueStageContainer from "../common/VenueStageContainer.tsx";
import SectionContainer from "./section/SectionContainer.tsx";

export interface SeatMapPreviewCanvasProps {
    stageConfig: StageConfig;
    seatConfig: SeatConfig;
    selectedSectionId?: string | null;
    onSelectSection?: (sectionId?: string) => void;
}

const MIN_ZOOM = 0.55;
const MAX_ZOOM = 2.5;

const SeatMapPreviewCanvas: React.FC<SeatMapPreviewCanvasProps> = ({
                                                                       stageConfig,
                                                                       seatConfig,
                                                                       selectedSectionId,
                                                                       onSelectSection,
                                                                   }) => {
    const stageRef = useRef<Konva.Stage>(null);
    const layerRef = useRef<Konva.Layer>(null);
    const containerRef = useRef<HTMLDivElement>(null);
    const [viewportSize, setViewportSize] = useState({width: 0, height: 0});
    const [zoomLevel, setZoomLevel] = useState(1);
    const [pan, setPan] = useState({x: 0, y: 0});
    const [isPanning, setIsPanning] = useState(false);
    const [panOrigin, setPanOrigin] = useState<{ x: number; y: number } | null>(null);
    const isViewportReady = viewportSize.width > 0 && viewportSize.height > 0;

    useEffect(() => {
        const element = containerRef.current;

        if (!element) {
            return;
        }

        const updateViewportSize = () => {
            setViewportSize({
                width: element.clientWidth,
                height: element.clientHeight,
            });
        };

        updateViewportSize();

        const observer = new ResizeObserver(updateViewportSize);
        observer.observe(element);

        return () => observer.disconnect();
    }, []);

    const getZoomContext = () => {
        if (!stageRef.current || !layerRef.current) {
            return null;
        }

        const stage = stageRef.current;
        const layer = layerRef.current;
        const pointer = stage.getPointerPosition();

        if (!pointer) {
            return null;
        }

        const oldScale = layer.scaleX();
        const mousePointTo = {
            x: (pointer.x - layer.x()) / oldScale,
            y: (pointer.y - layer.y()) / oldScale,
        };

        return {oldScale, pointer, mousePointTo};
    };

    const applyZoom = (nextZoom: number, pointer: { x: number; y: number }, mousePointTo: { x: number; y: number }) => {
        const clampedZoom = Math.max(MIN_ZOOM, Math.min(MAX_ZOOM, nextZoom));
        setZoomLevel(clampedZoom);
        setPan({
            x: pointer.x - mousePointTo.x * clampedZoom,
            y: pointer.y - mousePointTo.y * clampedZoom,
        });
    };

    const handleWheel = (event: Konva.KonvaEventObject<WheelEvent>) => {
        event.evt.preventDefault();
        event.evt.stopPropagation();

        const zoomContext = getZoomContext();
        if (!zoomContext) {
            return;
        }

        const direction = event.evt.deltaY > 0 ? -1 : 1;
        const factor = 1.1;
        const nextZoom = direction > 0 ? zoomContext.oldScale * factor : zoomContext.oldScale / factor;

        applyZoom(nextZoom, zoomContext.pointer, zoomContext.mousePointTo);
    };

    const handleMouseDown = (event: Konva.KonvaEventObject<MouseEvent>) => {
        if (event.target !== event.currentTarget) {
            return;
        }

        const stage = stageRef.current;
        if (!stage) {
            return;
        }

        const pointer = stage.getPointerPosition();
        if (!pointer) {
            return;
        }

        setIsPanning(true);
        setPanOrigin({x: pointer.x - pan.x, y: pointer.y - pan.y});
        onSelectSection?.(undefined);
    };

    const handleMouseMove = () => {
        if (!isPanning || !panOrigin || !stageRef.current) {
            return;
        }

        const pointer = stageRef.current.getPointerPosition();
        if (!pointer) {
            return;
        }

        setPan({x: pointer.x - panOrigin.x, y: pointer.y - panOrigin.y});
    };

    const stopPanning = () => {
        setIsPanning(false);
        setPanOrigin(null);
    };

    const sectionElements = useMemo(() => seatConfig.sections || [], [seatConfig.sections]);

    if (!isViewportReady) {
        return (
                <div ref={containerRef}
                     className="relative h-full w-full min-w-0 overflow-hidden border border-slate-200 bg-gradient-to-b from-slate-50 to-indigo-50 min-h-0 flex-1">
                    <div className="flex h-full w-full items-center justify-center text-sm text-slate-500">
                        Seat map is loading...
                    </div>
                </div>
        );
    }

    return (
            <div ref={containerRef}
                 className="relative h-full w-full min-w-0 overflow-hidden border border-slate-200 bg-gradient-to-b from-slate-50 to-indigo-50 min-h-0 flex-1">
                <div className="absolute left-4 top-4 z-20 flex items-center gap-2 border border-slate-200 bg-white px-3! py-2! text-xs text-slate-600 shadow-sm rounded-2xl">
                    <span>Wheel to zoom, drag empty space to pan</span>
                </div>
                <Stage
                        ref={stageRef}
                        width={viewportSize.width}
                        height={viewportSize.height}
                        onWheel={handleWheel}
                        onMouseDown={handleMouseDown}
                        onMouseMove={handleMouseMove}
                        onMouseUp={stopPanning}
                        onMouseLeave={stopPanning}
                        className="h-full w-full"
                        style={{background: "transparent", cursor: isPanning ? "grabbing" : "grab"}}
                >
                    <Layer
                            ref={layerRef}
                            x={pan.x}
                            y={pan.y}
                            scaleX={zoomLevel}
                            scaleY={zoomLevel}
                    >
                        <VenueStageContainer stageConfig={stageConfig}/>
                        {sectionElements.map((section) => (
                                <SectionContainer
                                        key={section.id}
                                        section={section}
                                        isSelected={selectedSectionId === section.id}
                                        onSelect={(selectedSection) => onSelectSection?.(selectedSection.id)}
                                />
                        ))}
                    </Layer>
                </Stage>
            </div>
    );
};

export default SeatMapPreviewCanvas;
