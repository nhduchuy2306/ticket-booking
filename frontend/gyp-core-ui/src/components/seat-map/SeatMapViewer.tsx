import React, { useEffect, useState } from "react";
import { SAMPLE_DATA, SeatModel } from "./SeatMapModel.ts";

type SeatMapViewerProps = {
    eventId: string;
};

const SeatMapViewer: React.FC<SeatMapViewerProps> = ({eventId}) => {
    const [seats, setSeats] = useState<SeatModel[]>([]);
    // const [selectedSeats, setSelectedSeats] = useState([]);

    useEffect(() => {
        setSeats(SAMPLE_DATA.seats)
    }, [eventId]);

    const handleSeatClick = (seatId: string) => {
        const updatedSeats = seats.map(seat =>
                seat.id === seatId
                        ? {...seat, status: seat.status === "selected" ? "available" : "selected"}
                        : seat
        );
        setSeats(updatedSeats);
    };

    return (
            <div className={"seat-map-container"}>
                {seats.map(seat => (
                        <div
                                key={seat.id}
                                onClick={() => handleSeatClick(seat.id)}
                                style={{
                                    position: 'absolute',
                                    left: `${seat.x}px`,
                                    top: `${seat.y}px`,
                                    width: `${seat.width}px`,
                                    height: `${seat.height}px`,
                                    backgroundColor:
                                            seat.status === "available" ? "green" :
                                                    seat.status === "booked" ? "red" : "blue",
                                    border: '1px solid black',
                                    cursor: seat.status === "booked" ? "not-allowed" : "pointer"
                                }}>{seat.id}</div>
                ))}
            </div>
    );
}

export default SeatMapViewer;