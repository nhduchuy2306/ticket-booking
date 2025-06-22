import React from "react";
import SeatMapRenderer from "../../components/seat-map/SeatMapRenderer.tsx";

interface SeatMapPageProps {
    // Define any props if needed
}

const SeatMapPage: React.FC<SeatMapPageProps> = () => {
    return (
            <div className="w-full bg-white !p-2 !overflow-y-auto !h-[calc(100vh-100px)]">
                <SeatMapRenderer/>
            </div>
    );
}

export default SeatMapPage;