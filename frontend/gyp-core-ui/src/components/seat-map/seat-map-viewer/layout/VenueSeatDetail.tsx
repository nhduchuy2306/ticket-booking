import React from "react";
import ChooseSeatAndOrderComponent from "../../common/ChooseSeatAndOrderComponent.tsx";
import SectionPriceComponent from "../../common/SectionPriceComponent.tsx";
import { useSeatMapViewerContext } from "../context/SeatMapViewerContext.tsx";

interface VenueSeatDetailProps {
    title?: string;
}

const VenueSeatDetail: React.FC<VenueSeatDetailProps> = ({title}) => {
    const {seatTypes} = useSeatMapViewerContext();

    return (
            <div className="flex-1/3 flex flex-col items-start justify-between bg-[#38383d] h-full">
                <div className="flex-1 w-full">
                    <SectionPriceComponent title={title} seatTypes={seatTypes}/>
                </div>
                <div className="w-full">
                    <ChooseSeatAndOrderComponent/>
                </div>
            </div>
    );
}

export default VenueSeatDetail;