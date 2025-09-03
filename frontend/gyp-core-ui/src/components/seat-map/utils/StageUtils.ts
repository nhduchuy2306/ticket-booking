import { StageOrientation } from "../../../models/generated/event-service-models";

export class StageUtils {
    static getRotationOfStage = (stageOrientation: StageOrientation) => {
        switch (stageOrientation) {
            case "SOUTH":
                return 0;
            case "SOUTHWEST":
                return 45;
            case "WEST":
                return 90;
            case "NORTHWEST":
                return 135;
            case "NORTH":
                return 180;
            case "NORTHEAST":
                return 225;
            case "EAST":
                return 270;
            case "SOUTHEAST":
                return 315;
            default:
                console.warn(`Unknown stage orientation: ${stageOrientation}`);
                return 0;
        }
    }
}

