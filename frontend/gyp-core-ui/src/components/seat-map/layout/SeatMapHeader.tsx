import { Button, Flex, Typography } from "antd";
import React from "react";
import { PiPen } from "react-icons/pi";
import { SeatConfig } from "../../../models/generated/event-service-models";
import { SeatColors } from "../constants/SeatMapContants.ts";
import { useSeatMapContext } from "../context/SeatMapContext.tsx";

export interface SeatMapHeaderProps {
    seatTypeColors: SeatConfig['seatTypeColors'],
    selectedSeats: string[],
    zoomLevel: number,
    resetView: () => void,
    clearSelection: () => void,
}

const SeatMapHeader: React.FC<SeatMapHeaderProps> = (props) => {
    const {draggable, setDraggable} = useSeatMapContext();

    const handlePenToolToggle = () => {
        setDraggable(!draggable);
    }

    return (
            <div className="!w-full !mb-4 flex flex-col items-center justify-center">
                <Typography.Title level={2} className="text-2xl font-bold text-gray-800">
                    Interactive Seat Map
                </Typography.Title>

                <Flex justify="flex-start" align="flex-start" gap={30} className="!mb-1">
                    <Flex vertical={true} justify="flex-start" align="center" gap={10} className="!mb-1">
                        <Flex justify="flex-start" align="center" gap={10} className="!w-full">
                            <Button icon={<PiPen/>} color="primary" variant="solid" className="!w-15"
                                    onClick={handlePenToolToggle}/>
                            <span className="text-sm text-gray-600 font-bold">
                                Zoom: {(props.zoomLevel * 100).toFixed(0)}%
                            </span>
                        </Flex>
                        <Button onClick={props.resetView} color="primary" variant="solid" className="w-45">
                            Reset View
                        </Button>
                        <Button onClick={props.clearSelection} color="red" variant="solid" className="w-45">
                            Clear Selection ({props?.selectedSeats?.length || 0})
                        </Button>
                    </Flex>

                    <div>
                        <span className="text-gray-600 font-bold">Seat Colors:</span>
                        {Object.entries(SeatColors).map(([status, color]) => (
                                <div key={status} className="flex items-center gap-2">
                                    <div
                                            className="w-4 h-4 rounded-full border border-gray-300"
                                            style={{backgroundColor: color.color}}
                                    ></div>
                                    <span className="capitalize">{status.toLowerCase()}</span>
                                </div>
                        ))}
                    </div>

                    <div>
                        <span className="text-gray-600 font-bold">Seat Type Colors:</span>
                        {Object.entries(props.seatTypeColors || []).map(([status, color]) => (
                                <div key={status} className="flex items-center gap-2">
                                    <div
                                            className="w-4 h-4 rounded-full border border-gray-300"
                                            style={{backgroundColor: color}}
                                    ></div>
                                    <span className="capitalize">{status.toLowerCase()}</span>
                                </div>
                        ))}
                    </div>

                    <div className="bg-gray-50 rounded-lg text-sm !p-2">
                        <span className="text-gray-600 font-bold">Instructions:</span>
                        <ul className="list-disc list-inside space-y-1">
                            <li>Scroll to zoom in/out</li>
                            <li>Drag to pan around the venue</li>
                            <li>Click available seats to select them</li>
                            <li>Hover over seats for details</li>
                            <li>Golden border indicates premium seats</li>
                        </ul>
                    </div>
                </Flex>
            </div>
    );
}

export default SeatMapHeader;