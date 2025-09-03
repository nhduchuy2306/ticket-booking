import { Position, Seat, TicketTypeResponseDto } from "../../../../models/generated/event-service-models";
import { SeatColors } from "../../constants/SeatMapContants.ts";

export class SeatUtils {
    static calculateRectangleSeats = (seats: Seat[], width: number, height: number) => {
        const seatRadius = 10;
        const padding = seatRadius + 5; // Distance from table edge
        const positions: Position[] = [];

        if (seats.length === 0) {
            return positions;
        }

        // Calculate perimeter points
        const topCount = Math.ceil(seats.length * (width / (2 * width + 2 * height)));
        const rightCount = Math.ceil(seats.length * (height / (2 * width + 2 * height)));
        const bottomCount = Math.ceil(seats.length * (width / (2 * width + 2 * height)));
        const leftCount = seats.length - topCount - rightCount - bottomCount;

        let seatIndex = 0;

        // Top edge
        for (let i = 0; i < topCount && seatIndex < seats.length; i++) {
            const x = (width / (topCount + 1)) * (i + 1);
            const y = -padding;
            positions.push({x, y});
            seatIndex++;
        }

        // Right edge
        for (let i = 0; i < rightCount && seatIndex < seats.length; i++) {
            const x = width + padding;
            const y = (height / (rightCount + 1)) * (i + 1);
            positions.push({x, y});
            seatIndex++;
        }

        // Bottom edge
        for (let i = 0; i < bottomCount && seatIndex < seats.length; i++) {
            const x = width - (width / (bottomCount + 1)) * (i + 1);
            const y = height + padding;
            positions.push({x, y});
            seatIndex++;
        }

        // Left edge
        for (let i = 0; i < leftCount && seatIndex < seats.length; i++) {
            const x = -padding;
            const y = height - (height / (leftCount + 1)) * (i + 1);
            positions.push({x, y});
            seatIndex++;
        }

        return positions;
    };

    static calculateCircleSeats = (seats: Seat[], width: number, height: number) => {
        const radius = Math.min(width, height) / 2;
        const seatDistance = radius + 25; // Distance from center
        const centerX = width / 2;
        const centerY = height / 2;
        const angleStep = seats.length > 0 ? (2 * Math.PI) / seats.length : 0;

        return seats.map((_, index) => {
            const angle = index * angleStep - Math.PI / 2; // Start from top
            const x = centerX + seatDistance * Math.cos(angle);
            const y = centerY + seatDistance * Math.sin(angle);
            return {x, y};
        });
    };

    static getSeatColor = (seat: Seat, isSelected: boolean) => {
        if (isSelected) {
            return SeatColors.RESERVED.color;
        }
        let seatColor;
        switch (seat.status) {
            case "AVAILABLE":
                seatColor = SeatColors.AVAILABLE.color;
                break;
            case "RESERVED":
                seatColor = SeatColors.RESERVED.color;
                break;
            case "SOLD":
                seatColor = SeatColors.SOLD.color;
                break;
            default:
                seatColor = SeatColors.AVAILABLE.color;
        }
        return seatColor;
    }

    static getTicketTypeById = (ticketTypes: TicketTypeResponseDto[], id: string): TicketTypeResponseDto | undefined => {
        return ticketTypes.find(type => type.id === id);
    }
}