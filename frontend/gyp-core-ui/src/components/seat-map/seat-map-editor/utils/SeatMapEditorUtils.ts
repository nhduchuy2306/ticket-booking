import {
    Position,
    Row,
    Seat,
    SeatInventoryStatus,
    Section,
    StageConfig,
} from "../../../../models/generated/event-service-models";
import { DraftErrorMap, SectionDraftState, StageDraftState } from "../../models/SeatMapModels.ts";


const DEFAULT_STAGE_LABEL = "STAGE";
const DEFAULT_SEAT_STATUS: SeatInventoryStatus = "AVAILABLE";

const createRandomId = () => crypto.randomUUID();

const isIntegerLike = (value: string): boolean => /^-?\d+$/.test(value.trim());

const parseInteger = (value: string): number | null => {
    if (!isIntegerLike(value)) {
        return null;
    }

    return Number.parseInt(value, 10);
};

const getAlphaLabel = (index: number): string => {
    let label = "";
    let current = index + 1;

    while (current > 0) {
        const remainder = (current - 1) % 26;
        label = String.fromCharCode(65 + remainder) + label;
        current = Math.floor((current - 1) / 26);
    }

    return label;
};

export const createDefaultStageDraft = (stageConfig?: StageConfig): StageDraftState => ({
    label: (stageConfig as any)?.label || stageConfig?.name || DEFAULT_STAGE_LABEL,
    width: String(stageConfig?.dimensions?.width || 800),
    height: String(stageConfig?.dimensions?.height || 150),
    borderRadius: String(stageConfig?.borderRadius || 12),
    positionX: String(stageConfig?.position?.x || 0),
    positionY: String(stageConfig?.position?.y || 0),
});

export const createDefaultSectionDraft = (sections: Section[] = []): SectionDraftState => {
    const maxBottom = sections.reduce((accumulator, section) => {
        const sectionBottom = (section.position?.y || 0) + (section.dimensions?.height || 0);
        return Math.max(accumulator, sectionBottom);
    }, 0);

    return {
        name: "",
        type: "SEATED",
        ticketTypeId: "",
        positionX: "0",
        positionY: String(Math.max(250, maxBottom + 80)),
        borderRadius: "10",
        labelPosition: "LEFT",
        rowCount: "4",
        seatsPerRow: "12",
        rowSpacing: "50",
        seatSpacing: "50",
        startX: "40",
        capacity: "100",
    };
};

export const buildStageConfigFromDraft = (draft: StageDraftState): {
    stageConfig?: StageConfig;
    errors: DraftErrorMap
} => {
    const errors: DraftErrorMap = {};
    const width = parseInteger(draft.width);
    const height = parseInteger(draft.height);
    const borderRadius = parseInteger(draft.borderRadius);
    const positionX = parseInteger(draft.positionX);
    const positionY = parseInteger(draft.positionY);

    if (!draft.label.trim()) {
        errors.label = "Stage label is required";
    }
    if (width === null || width <= 0) {
        errors.width = "Stage width must be a positive number";
    }
    if (height === null || height <= 0) {
        errors.height = "Stage height must be a positive number";
    }
    if (borderRadius === null || borderRadius < 0) {
        errors.borderRadius = "Stage border radius must be zero or greater";
    }
    if (positionX === null || positionX < 0) {
        errors.positionX = "Stage position X must be zero or greater";
    }
    if (positionY === null || positionY < 0) {
        errors.positionY = "Stage position Y must be zero or greater";
    }

    if (Object.keys(errors).length > 0 || width === null || height === null || borderRadius === null || positionX === null || positionY === null) {
        return {errors};
    }

    return {
        stageConfig: {
            name: draft.label.trim(),
            position: {
                x: positionX,
                y: positionY,
            },
            dimensions: {
                width,
                height,
            },
            borderRadius,
        },
        errors,
    };
};

export const buildSectionFromDraft = (
        draft: SectionDraftState,
        sectionId: string,
        persistIds: boolean,
): { section?: Section; errors: DraftErrorMap } => {
    const errors: DraftErrorMap = {};
    const name = draft.name.trim();

    const positionX = parseInteger(draft.positionX);
    const positionY = parseInteger(draft.positionY);
    const borderRadius = parseInteger(draft.borderRadius);

    if (!name) {
        errors.name = "Section name is required";
    }
    if (positionX === null) {
        errors.positionX = "Position X must be a number";
    }
    if (positionY === null) {
        errors.positionY = "Position Y must be a number";
    }
    if (borderRadius === null || borderRadius < 0) {
        errors.borderRadius = "Border radius must be zero or greater";
    }

    if (draft.type === "SEATED") {
        const rowCount = parseInteger(draft.rowCount);
        const seatsPerRow = parseInteger(draft.seatsPerRow);
        const rowSpacing = parseInteger(draft.rowSpacing);
        const seatSpacing = parseInteger(draft.seatSpacing);
        const startX = parseInteger(draft.startX);

        if (rowCount === null || rowCount <= 0) {
            errors.rowCount = "Row count must be a positive number";
        }
        if (seatsPerRow === null || seatsPerRow <= 0) {
            errors.seatsPerRow = "Seats per row must be a positive number";
        }
        if (rowSpacing === null || rowSpacing <= 0) {
            errors.rowSpacing = "Row spacing must be a positive number";
        }
        if (seatSpacing === null || seatSpacing <= 0) {
            errors.seatSpacing = "Seat spacing must be a positive number";
        }
        if (startX === null || startX < 0) {
            errors.startX = "Start X must be zero or greater";
        }

        if (
                Object.keys(errors).length > 0 ||
                rowCount === null ||
                seatsPerRow === null ||
                rowSpacing === null ||
                seatSpacing === null ||
                startX === null ||
                positionX === null ||
                positionY === null ||
                borderRadius === null
        ) {
            return {errors};
        }

        const rowOriginY = 50;
        const rows: Row[] = Array.from({length: rowCount}, (_, rowIndex) => {
            const rowPosition: Position = {x: startX, y: rowOriginY + rowIndex * rowSpacing};
            const seats: Seat[] = Array.from({length: seatsPerRow}, (_, seatIndex) => ({
                id: persistIds ? createRandomId() : `${sectionId}-row-${rowIndex}-seat-${seatIndex}`,
                name: String(seatIndex + 1),
                status: DEFAULT_SEAT_STATUS,
            }));

            return {
                id: persistIds ? createRandomId() : `${sectionId}-row-${rowIndex}`,
                name: getAlphaLabel(rowIndex),
                position: rowPosition,
                seats,
                borderRadius,
                seatSpacing,
            };
        });

        const sectionWidth = Math.max(240, startX * 2 + (seatsPerRow - 1) * seatSpacing + 80);
        const sectionHeight = Math.max(140, rowOriginY + (rowCount - 1) * rowSpacing + 70);

        return {
            section: {
                id: persistIds ? createRandomId() : sectionId,
                name,
                type: "SEATED",
                ticketTypeId: draft.ticketTypeId || "",
                position: {x: positionX, y: positionY},
                dimensions: {width: sectionWidth, height: sectionHeight},
                borderRadius,
                rowSpacing,
                capacity: 0,
                labelPosition: draft.labelPosition,
                rows,
            },
            errors,
        };
    }

    const capacity = parseInteger(draft.capacity);
    if (capacity === null || capacity <= 0) {
        errors.capacity = "Capacity must be a positive number";
    }

    if (Object.keys(errors).length > 0 || capacity === null || positionX === null || positionY === null || borderRadius === null) {
        return {errors};
    }

    return {
        section: {
            id: persistIds ? createRandomId() : sectionId,
            name,
            type: "STANDING",
            ticketTypeId: draft.ticketTypeId || "",
            position: {x: positionX, y: positionY},
            dimensions: {width: 280, height: Math.max(160, 120 + Math.ceil(capacity / 10) * 4)},
            borderRadius,
            rowSpacing: 0,
            capacity,
            labelPosition: draft.labelPosition,
            rows: [],
        },
        errors,
    };
};

export const getPreviewSectionId = (editingSectionId: string | null): string => {
    return editingSectionId || "__draft-section__";
};
