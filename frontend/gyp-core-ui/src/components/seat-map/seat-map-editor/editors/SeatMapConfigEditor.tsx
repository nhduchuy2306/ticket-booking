import React from "react";
import { SelectedType } from "../../constants/SeatMapContants.ts";
import { useSeatMapContext } from "../context/SeatMapContext.tsx";

interface SeatMapConfigEditorProps {
    title?: string;
}

const SeatMapConfigEditor: React.FC<SeatMapConfigEditorProps> = ({title}) => {
    const {selectedType, seatTypes} = useSeatMapContext();

    const getEditor = () => {
        switch (selectedType?.type) {
            case SelectedType.VENUE_CONTAINER.key:
                return <div>{SelectedType.VENUE_CONTAINER.value}</div>
            case SelectedType.SECTION_CONTAINER.key:
                return <div>{SelectedType.SECTION_CONTAINER.value}</div>
            case SelectedType.SEATED_ROW_WRAPPER.key:
                return <div>{SelectedType.SEATED_ROW_WRAPPER.value}</div>
            case SelectedType.SEATED_SEAT.key:
                return <div>{SelectedType.SEATED_SEAT.value}</div>
            case SelectedType.TABLE_ROW_WRAPPER.key:
                return <div>{SelectedType.TABLE_ROW_WRAPPER.value}</div>
            case SelectedType.TABLE_SEAT.key:
                return <div>{SelectedType.TABLE_SEAT.value}</div>
            default:
                return <div className="text-white text-center">Select an item to edit</div>
        }
    }

    return (
            <div className="flex-1 flex-col items-center justify-center bg-[#38383d] h-full">
                <p className="text-white text-center text-2xl font-bold !m-2">{title}</p>
                <div className="flex flex-col items-start justify-center gap-2 !m-4">
                    {seatTypes?.map((seatType) => (
                            <div key={seatType.id} className="flex items-center justify-between w-full">
                                <div className="flex items-center gap-2">
                                    <div className="w-6 h-4 rounded border"
                                         style={{backgroundColor: seatType.color}}></div>
                                    <span className="capitalize text-white">{seatType.name}</span>
                                </div>
                                <div className="text-white font-bold">${seatType.price}</div>
                            </div>
                    ))}
                </div>
                <div>{getEditor()}</div>
            </div>
    );
}

export default SeatMapConfigEditor;