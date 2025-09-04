import React from "react";
import { SeatColors } from "../../constants/SeatMapContants.ts";

const VenueSeatMapHeader: React.FC = () => {
    return (
            <div className="flex items-center justify-center gap-4 !m-2 bg-black">
                {Object.entries(SeatColors).map(([status, color]) => (
                        <div key={status} className="flex items-center justify-center gap-2">
                            <div className="w-4 h-4 rounded-full"
                                 style={{backgroundColor: color.color}}></div>
                            <span className="text-white">{status.toLowerCase()}</span>
                        </div>
                ))}
            </div>
    );
}

export default VenueSeatMapHeader;