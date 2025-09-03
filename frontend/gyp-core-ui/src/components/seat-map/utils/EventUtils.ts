import Konva from "konva";
import { Dispatch, SetStateAction } from "react";
import { Seat } from "../../../models/generated/event-service-models";

export class EventUtils {
    static handleMouseEnterEvent(evt: Konva.KonvaEventObject<MouseEvent>) {
        evt.evt.preventDefault();
        evt.evt.stopPropagation();
        const container = evt.target.getStage()?.container();
        if (container) {
            container.style.cursor = 'pointer';
        }
    }

    static handleMouseLeaveEvent(evt: Konva.KonvaEventObject<MouseEvent>) {
        evt.evt.preventDefault();
        evt.evt.stopPropagation();
        const container = evt.target.getStage()?.container();
        if (container) {
            container.style.cursor = 'default';
        }
    }

    static handleMouseDown = (setDraggable?: Dispatch<SetStateAction<boolean>>) => {
        if (!setDraggable) return;
        setDraggable(false);
    };

    static handleMouseUp = (setDraggable?: Dispatch<SetStateAction<boolean>>) => {
        if (!setDraggable) return;
        setTimeout(() => setDraggable(true), 50);
    };

    static handleClick = (
            e: Konva.KonvaEventObject<MouseEvent>,
            seat: Seat, isSelected: boolean,
            selectedSeats: string[],
            setSelectedSeats: Dispatch<SetStateAction<string[]>>,
    ) => {
        e.evt.preventDefault();
        e.evt.stopPropagation();

        if (seat.status === "AVAILABLE" || seat.status === "RESERVED") {
            if (isSelected) {
                setSelectedSeats(selectedSeats.filter(id => id !== seat.id));
            } else {
                setSelectedSeats([...selectedSeats, seat.id]);
            }
        }
    }
}