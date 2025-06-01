import React from "react";

interface SeatMapPageProps {
    // Define any props if needed
}

const SeatMapPage: React.FC<SeatMapPageProps> = () => {
    return (
        <div className="w-full h-full bg-white p-4">
            <h1 className="text-2xl font-bold mb-4">Seat Map</h1>
            <p>This is the Seat Map Page.</p>
        </div>
    );
}

export default SeatMapPage;