import { Button } from "antd";
import Konva from "konva";
import React, { useEffect, useRef, useState } from "react";
import { BiMinus, BiPlus, BiReset } from "react-icons/bi";
import { Layer, Stage } from "react-konva";
import { SeatConfig, StageConfig, TicketTypeResponseDto, VenueMap, } from "../../../models/generated/event-service-models";
import { TicketTypeService } from "../../../services/Event/TicketTypeService.ts";
import { createErrorNotification } from "../../notification/Notification.ts";
import { SeatMapContext } from "./context/SeatMapContext.tsx";
import SeatMapConfigEditor from "./editors/SeatMapConfigEditor.tsx";
import VenueSeatContainer from "./layout/VenueSeatContainer.tsx";
import VenueSeatMapHeader from "./layout/VenueSeatMapHeader.tsx";
import VenueStageContainer from "./layout/VenueStageContainer.tsx";
import { SelectedTypeModel } from "./models/SeatMapModels.ts";

export interface SeatMapEditorProps {
    venueMap?: VenueMap;
    title?: string;
}

const SeatMapEditor: React.FC<SeatMapEditorProps> = ({venueMap, title}) => {
    const [venueData, setVenueData] = useState<VenueMap>({});
    const [seatTypes, setSeatTypes] = useState<TicketTypeResponseDto[]>([]);
    const [stageConfig, setStageConfig] = useState<StageConfig>({});
    const [seatConfig, setSeatConfig] = useState<SeatConfig>({});
    const [selectedSeats, setSelectedSeats] = useState<string[]>([]);
    const [zoomLevel, setZoomLevel] = useState(0.8);
    const [showSeatNumbers, setShowSeatNumbers] = useState(false);
    const [draggable, setDraggable] = useState(true);
    const [selectedType, setSelectedType] = useState<SelectedTypeModel>();

    const stageRef = useRef<Konva.Stage>(null);
    const layerRef = useRef<Konva.Layer>(null);
    const stageWidth = 800;
    const stageHeight = 650;

    useEffect(() => {
        if (venueMap) {
            setVenueData(venueMap);
        }
    }, [venueMap]);

    useEffect(() => {
        if (venueData && Object.keys(venueData).length > 0) {
            setStageConfig(venueData.stageConfig || {});
            setSeatConfig(venueData.seatConfig || {});
            void getTicketTypes(venueData.seatConfig?.seatTypes || []);
        }
    }, [venueData]);

    const getTicketTypes = async (ids: string[]) => {
        try {
            if (ids.length === 0) return;
            const types = await TicketTypeService.getTicketTypesByIds(ids);
            if (types && types.length > 0) {
                setSeatTypes(types);
            } else {
                setSeatTypes([]);
            }
        } catch (error) {
            createErrorNotification("Failed to fetch ticket types");
        }
    };

    const handleResetView = () => {
        if (!stageRef.current || !layerRef.current) return;

        const stage = stageRef.current;
        const layer = layerRef.current;

        const resetScale = 0.8;

        // Set scale first
        layer.scale({x: resetScale, y: resetScale});

        // Calculate centered position
        const newX = stageWidth / 2 - (stageWidth / 2) * resetScale;
        const newY = stageHeight / 2 - (stageHeight / 2) * resetScale;

        layer.position({x: newX, y: newY});
        layer.batchDraw();

        stage.position({x: 0, y: 0});
        stage.batchDraw();

        setZoomLevel(resetScale);
        setShowSeatNumbers(false);
    };

    const getZoomContext = () => {
        if (!stageRef.current || !layerRef.current) return null;

        const stage = stageRef.current;
        const layer = layerRef.current;
        const oldScale = layer.scaleX();
        const pointer = stage.getPointerPosition();
        if (!pointer) return null;

        const mousePointTo = {
            x: (pointer.x - layer.x()) / oldScale,
            y: (pointer.y - layer.y()) / oldScale,
        };

        return {oldScale, pointer, mousePointTo};
    };

    const applyZoom = (
            scale: number,
            pointer: { x: number; y: number },
            mousePointTo: { x: number; y: number }
    ) => {
        if (!layerRef.current) return;

        const clampedScale = Math.max(0.5, Math.min(3, scale));
        layerRef.current.scale({x: clampedScale, y: clampedScale});
        setZoomLevel(clampedScale);
        setShowSeatNumbers(clampedScale > 1.7);

        const newPos = {
            x: pointer.x - mousePointTo.x * clampedScale,
            y: pointer.y - mousePointTo.y * clampedScale,
        };

        layerRef.current.position(newPos);
        layerRef.current.batchDraw();
    };

    const handleWheel = (e: Konva.KonvaEventObject<WheelEvent>) => {
        e.evt.preventDefault();
        e.evt.stopPropagation();

        const ctx = getZoomContext();
        if (!ctx) return;
        const {oldScale, pointer, mousePointTo} = ctx;

        const direction = e.evt.deltaY > 0 ? -1 : 1;
        const factor = 1.1;
        const newScale = direction > 0 ? oldScale * factor : oldScale / factor;

        applyZoom(newScale, pointer, mousePointTo);
    };

    const handleZoomInOut = (isZoomIn: boolean) => {
        const ctx = getZoomContext();
        if (!ctx) return;
        const {oldScale, pointer, mousePointTo} = ctx;

        const factor = 1.1;
        const newScale = isZoomIn ? oldScale * factor : oldScale / factor;

        applyZoom(newScale, pointer, mousePointTo);
    };

    return (
            <SeatMapContext.Provider value={{
                venueData: venueData,
                stageConfig: stageConfig,
                seatConfig: seatConfig,
                seatTypes: seatTypes,
                showSeatNumbers: showSeatNumbers,
                selectedSeats: selectedSeats,
                setSelectedSeats: setSelectedSeats,
                draggable: draggable,
                setDraggable: setDraggable,
                selectedType: selectedType,
                setSelectedType: setSelectedType,
            }}>
                <div className="w-full flex items-start gap-3 !m-2 h-full">
                    <div className="absolute z-10 flex flex-col items-center justify-center gap-3 top-8">
                        <Button onClick={() => handleZoomInOut(true)} type="default" icon={<BiPlus/>}
                                className="!rounded-2xl mb-2"/>
                        <Button onClick={handleResetView} type="default" icon={<BiReset/>} className="!rounded-2xl"/>
                        <Button onClick={() => handleZoomInOut(false)} type="default" icon={<BiMinus/>}
                                className="!rounded-2xl mb-2"/>
                    </div>
                    <div className="rounded flex flex-col flex-1 items-center justify-center">
                        <VenueSeatMapHeader/>
                        <Stage x={0} y={0}
                               ref={stageRef}
                               width={stageWidth}
                               height={stageHeight}
                               onWheel={handleWheel}
                               draggable={draggable}>
                            <Layer ref={layerRef}
                                   x={stageWidth / 2 - (stageWidth / 2) * zoomLevel}
                                   y={stageHeight / 2 - (stageHeight / 2) * zoomLevel}
                                   scale={{x: zoomLevel, y: zoomLevel}}>
                                <VenueStageContainer stageConfig={stageConfig}/>
                                <VenueSeatContainer seatConfig={seatConfig}/>
                            </Layer>
                        </Stage>
                    </div>
                    <SeatMapConfigEditor title={title}/>
                </div>
            </SeatMapContext.Provider>
    );
};

export default SeatMapEditor;