import React from "react";

type SeatMapUploaderProps = {
    eventId: string;
};

const SeatMapUploader: React.FC<SeatMapUploaderProps> = ({eventId}) => {
    // @ts-ignore
    const handleFileUpload = (e) => {
        const file = e.target.files[0];
        const formData = new FormData();
        formData.append("file", file);
        console.log(eventId)
    };

    return <input type="file" accept=".json" onChange={handleFileUpload}/>;
}

export default SeatMapUploader;