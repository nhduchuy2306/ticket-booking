import Konva from "konva";
import { Dispatch, SetStateAction } from "react";
import { Seat, Section } from "../../../../models/generated/event-service-models";
import { SelectedSeatModel } from "../../models/SeatMapModels.ts";

export class EventUtils {
    static handleSeatMouseEnterEvent(evt: Konva.KonvaEventObject<MouseEvent>, seat: Seat) {
        evt.evt.preventDefault();
        evt.evt.stopPropagation();
        const container = evt.target.getStage()?.container();
        if (container) {
            if (seat.status === "AVAILABLE") {
                container.style.cursor = 'pointer';
            } else {
                container.style.cursor = 'not-allowed';
            }
        }
    }

    static handleSeatMouseLeaveEvent(evt: Konva.KonvaEventObject<MouseEvent>, seat: Seat) {
        evt.evt.preventDefault();
        evt.evt.stopPropagation();
        const container = evt.target.getStage()?.container();
        if (container) {
            if (seat.status === "AVAILABLE") {
                container.style.cursor = 'default';
            } else {
                container.style.cursor = 'not-allowed';
            }
        }
    }

    static handleItemMouseEnterEvent(evt: Konva.KonvaEventObject<MouseEvent>) {
        evt.evt.preventDefault();
        evt.evt.stopPropagation();
        const container = evt.target.getStage()?.container();
        if (container) {
            container.style.cursor = 'pointer';
        }
    }

    static handleItemMouseLeaveEvent(evt: Konva.KonvaEventObject<MouseEvent>) {
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
            seat: Seat,
            isSelected: boolean,
            selectedSeats: SelectedSeatModel[],
            setSelectedSeats: Dispatch<SetStateAction<SelectedSeatModel[]>>,
            section?: Section,
    ) => {
        e.evt.preventDefault();
        e.evt.stopPropagation();

        if (seat.status === "AVAILABLE") {
            if (isSelected) {
                setSelectedSeats(selectedSeats.filter((s) => s.seat.id !== seat.id));
            } else {
                setSelectedSeats([...selectedSeats, {
                    section: section,
                    seat: seat
                }]);
            }
        }
    }
}