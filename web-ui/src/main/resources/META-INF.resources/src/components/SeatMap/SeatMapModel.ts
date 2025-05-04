export interface SeatMapModel {
    venueName: string;
    seats: SeatModel[];
}

export interface SeatModel {
    id: string;
    type: string;
    price: number;
    x: number;
    y: number;
    width: number;
    height: number;
    status: string;
}

export const SAMPLE_DATA: SeatMapModel = {
    "venueName": "Hội trường A",
    "seats": [
        {
            "id": "A1",
            "type": "VIP",
            "price": 500000,
            "x": 100,
            "y": 50,
            "width": 30,
            "height": 30,
            "status": "available"
        },
        {
            "id": "A2",
            "type": "Standard",
            "price": 200000,
            "x": 140,
            "y": 50,
            "width": 30,
            "height": 30,
            "status": "available"
        },
        {
            "id": "A3",
            "type": "Standard",
            "price": 200000,
            "x": 180,
            "y": 50,
            "width": 30,
            "height": 30,
            "status": "available"
        }
    ]
}