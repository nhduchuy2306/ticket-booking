import Konva from "konva";
import React, { useEffect, useRef, useState } from "react";
import { Layer, Stage } from 'react-konva';
import { SeatConfig, StageConfig, VenueMap } from "../../models/generated/event-service-models";
import { SeatMapContext } from "./context/SeatMapContext.tsx";
import SeatMapHeader from "./layout/SeatMapHeader.tsx";
import SeatMapSeatConfig from "./layout/SeatMapSeatConfig.tsx";
import SeatMapStageConfig from "./layout/SeatMapStageConfig.tsx";

export interface SeatMapRendererProps {
    venueMap?: VenueMap;
    title?: string;
}

const SeatMapRenderer: React.FC<SeatMapRendererProps> = ({venueMap, title}) => {
    const [venueData, setVenueData] = useState<VenueMap>({});
    const [seatTypeColors, setSeatTypeColors] = useState<SeatConfig['seatTypeColors']>({});
    const [stageConfig, setStageConfig] = useState<StageConfig>({});
    const [seatConfig, setSeatConfig] = useState<SeatConfig>({});
    const [selectedSeats, setSelectedSeats] = useState<string[]>([]);
    const [zoomLevel, setZoomLevel] = useState(0.8);
    const [showSeatNumbers, setShowSeatNumbers] = useState(true);
    const [draggable, setDraggable] = useState(true);
    const stageRef = useRef<Konva.Stage>(null);
    const layerRef = useRef<Konva.Layer>(null);

    useEffect(() => {
        if (venueMap) {
            setVenueData(venueMap);
        }
    }, [venueMap]);

    useEffect(() => {
        if (venueData && Object.keys(venueData).length > 0) {
            setSeatTypeColors(venueData.seatConfig?.seatTypeColors || {});
            setStageConfig(venueData.stageConfig || {});
            setSeatConfig(venueData.seatConfig || {});
        }
    }, [venueData]);

    const handleResetView = () => {
        if (stageRef.current) {
            stageRef.current.position({x: 0, y: 0});
            stageRef.current.scale({x: 1, y: 1});
            stageRef.current.batchDraw();
            setZoomLevel(1);
            setShowSeatNumbers(true);
        }
    };

    const handleWheel = (e: Konva.KonvaEventObject<WheelEvent>) => {
        e.evt.preventDefault();
        e.evt.stopPropagation();

        if (!stageRef.current) {
            console.warn("Stage reference is not set.");
            return;
        }
        const stage = stageRef.current;

        const oldScale = stage.scaleX();
        const pointer = stage.getPointerPosition();

        if (!pointer) return;

        const mousePointTo = {
            x: (pointer.x - stage.x()) / oldScale,
            y: (pointer.y - stage.y()) / oldScale,
        };

        const direction = e.evt.deltaY > 0 ? -1 : 1;
        const factor = 1.1;
        const newScale = direction > 0 ? oldScale * factor : oldScale / factor;

        const clampedScale = Math.max(0.5, Math.min(3, newScale));

        stage.scale({x: clampedScale, y: clampedScale});
        setZoomLevel(clampedScale);
        setShowSeatNumbers(clampedScale > 0.8);

        const newPos = {
            x: pointer.x - mousePointTo.x * clampedScale,
            y: pointer.y - mousePointTo.y * clampedScale,
        };
        stage.position(newPos);
        stage.batchDraw();
    }

    return (
            <SeatMapContext.Provider value={{
                venueData: venueData,
                stageConfig: stageConfig,
                seatConfig: seatConfig,
                seatTypeColors: seatTypeColors,
                showSeatNumbers: showSeatNumbers,
                selectedSeats: selectedSeats,
                setSelectedSeats: setSelectedSeats,
                draggable: draggable,
                setDraggable: setDraggable,
            }}>
                <div className="w-full flex flex-col items-center justify-center mb-5!">
                    {/* SeatMap Header */}
                    <SeatMapHeader
                            selectedSeats={selectedSeats}
                            seatTypeColors={seatTypeColors}
                            zoomLevel={zoomLevel}
                            resetView={handleResetView}
                            clearSelection={() => setSelectedSeats([])}
                            title={title}
                    ></SeatMapHeader>

                    {/* SeatMap Container */}
                    <div className="border border-gray-200 rounded flex items-center justify-center">
                        <Stage ref={stageRef}
                               width={1100}
                               height={800}
                               scale={{x: zoomLevel, y: zoomLevel}}
                               onWheel={handleWheel}
                               draggable={draggable}>
                            <Layer ref={layerRef}>
                                <SeatMapStageConfig stageConfig={stageConfig}/>
                                <SeatMapSeatConfig seatConfig={seatConfig}/>
                            </Layer>
                        </Stage>
                    </div>
                </div>
            </SeatMapContext.Provider>
    );
};

export default SeatMapRenderer;