import React, { useEffect, useRef, useState } from 'react';
import { SeatModel } from "./SeatMapModel.ts";

const SeatGridEditor: React.FC = () => {
    const [gridSize, setGridSize] = useState({rows: 5, cols: 10});
    const [seats, setSeats] = useState<SeatModel[]>([]);
    const canvasRef: React.Ref<HTMLCanvasElement> = useRef(null);

    useEffect(() => {
        generateGrid();
    }, [gridSize]);

    const generateGrid = () => {
        const {rows, cols} = gridSize;
        const newSeats = [];
        const cellSize = 40;

        for (let row = 0; row < rows; row++) {
            for (let col = 0; col < cols; col++) {
                newSeats.push({
                    id: `R${row}C${col}`,
                    row,
                    col,
                    x: col * cellSize,
                    y: row * cellSize,
                    width: cellSize,
                    height: cellSize,
                    type: "standard",
                    price: 200000,
                    status: "available"
                });
            }
        }
        setSeats(newSeats);
        drawCanvas(newSeats);
    };

    const drawCanvas = (seats: SeatModel[]) => {
        const canvas = canvasRef.current;
        if (!canvas) {
            return;
        }
        const ctx = canvas.getContext('2d');
        if (!ctx) {
            return;
        }
        ctx.clearRect(0, 0, canvas.width, canvas.height);

        seats.forEach(seat => {
            ctx.fillStyle =
                    seat.status === "booked" ? "red" :
                            seat.type === "VIP" ? "gold" : "green";
            ctx.fillRect(seat.x, seat.y, seat.width, seat.height);
            ctx.strokeRect(seat.x, seat.y, seat.width, seat.height);
        });
    };

    const handleCanvasClick = (e: any) => {
        const canvas = canvasRef.current;
        if (!canvas) {
            return;
        }
        const rect = canvas.getBoundingClientRect();
        const x = e.clientX - rect.left;
        const y = e.clientY - rect.top;

        const clickedSeat = seats.find(seat =>
                x >= seat.x && x <= seat.x + seat.width &&
                y >= seat.y && y <= seat.y + seat.height
        );

        if (clickedSeat) {
            const updatedSeats = seats.map(seat =>
                    seat.id === clickedSeat.id
                            ? {...seat, type: seat.type === "VIP" ? "standard" : "VIP"}
                            : seat
            );
            setSeats(updatedSeats);
            drawCanvas(updatedSeats);
        }
    };

    const exportToJSON = () => {
        const json = {seats};
        console.log(json);
    };

    return (
            <div>
                <div>
                    <label>Rows: </label>
                    <input
                            type="number"
                            value={gridSize.rows}
                            onChange={(e) => setGridSize({...gridSize, rows: +e.target.value})}
                    />
                    <label>Columns: </label>
                    <input
                            type="number"
                            value={gridSize.cols}
                            onChange={(e) => setGridSize({...gridSize, cols: +e.target.value})}
                    />
                    <button onClick={generateGrid}>Tạo Grid</button>
                </div>

                <canvas
                        ref={canvasRef}
                        width={gridSize.cols * 40}
                        height={gridSize.rows * 40}
                        onClick={handleCanvasClick}
                        style={{border: "1px solid black"}}
                />

                <button onClick={exportToJSON}>Lưu Sơ Đồ</button>
            </div>
    );
};

export default SeatGridEditor;