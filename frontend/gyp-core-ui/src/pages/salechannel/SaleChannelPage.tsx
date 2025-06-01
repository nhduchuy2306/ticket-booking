import React from "react";

interface SaleChannelPageProps {
    // Define any props if needed
}

const SaleChannelPage: React.FC<SaleChannelPageProps> = () => {
    return (
        <div className="w-full h-full bg-white p-4">
            <h1 className="text-2xl font-bold mb-4">Sale Channel</h1>
            <p>This is the Sale Channel Page.</p>
        </div>
    );
}

export default SaleChannelPage;