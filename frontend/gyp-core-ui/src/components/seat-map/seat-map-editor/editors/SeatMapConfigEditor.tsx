import React from "react";
import SectionPriceComponent from "../../common/SectionPriceComponent.tsx";
import { SelectedType } from "../../constants/SeatMapContants.ts";
import { useSeatMapEditorContext } from "../context/SeatMapEditorContext.tsx";

interface SeatMapConfigEditorProps {
    title?: string;
}

const SeatMapConfigEditor: React.FC<SeatMapConfigEditorProps> = ({title}) => {
    const {seatTypes, selectedType} = useSeatMapEditorContext();

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
                <SectionPriceComponent title={title} seatTypes={seatTypes}/>
                <div>{getEditor()}</div>
            </div>
    );
}

export default SeatMapConfigEditor;