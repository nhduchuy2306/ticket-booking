import React from "react";

interface VenuePageProps {
    // Define any props if needed
}

const VenuePage: React.FC<VenuePageProps> = () => {
    return (
            <div className="w-full h-full bg-white p-4">
                <h1 className="text-2xl font-bold mb-4">Venue</h1>
                <p>This is the Venue Page.</p>
            </div>
    );
}

export default VenuePage;

