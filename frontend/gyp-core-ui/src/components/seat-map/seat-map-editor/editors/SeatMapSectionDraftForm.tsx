import React from "react";
import type { DraftErrorMap, SectionDraftState } from "../../models/SeatMapModels.ts";
import SeatMapEditorField from "./SeatMapEditorField.tsx";

export interface SeatMapSectionDraftFormProps {
    draftSection: SectionDraftState;
    draftErrors: DraftErrorMap;
    onDraftChange: (field: keyof SectionDraftState, value: string) => void;
    onSaveDraft: () => void;
    onCancelDraft: () => void;
}

const baseButton = "border border-slate-300 bg-white px-3! py-2! text-sm text-slate-900 transition hover:border-slate-400 rounded-xl cursor-pointer";
const activeButton = "border border-slate-900 bg-slate-900 px-3! py-2! text-sm text-white! transition rounded-xl cursor-pointer";

const SeatMapSectionDraftForm: React.FC<SeatMapSectionDraftFormProps> = ({
                                                                             draftSection,
                                                                             draftErrors,
                                                                             onDraftChange,
                                                                             onSaveDraft,
                                                                             onCancelDraft,
                                                                         }) => {
    return (
            <div className="border border-slate-300 bg-white p-4! shadow-sm rounded-2xl"
                 onClick={(event) => event.stopPropagation()}>
                <div className="mb-5! flex items-center justify-between gap-3">
                    <strong className="text-sm text-slate-900">Section editor</strong>
                    <button type="button"
                            className="border border-slate-300 bg-white px-3! py-2! text-sm text-slate-900 transition hover:border-slate-400 rounded-xl cursor-pointer"
                            onClick={onCancelDraft}>
                        Cancel
                    </button>
                </div>

                <div className="!space-y-3">
                    <SeatMapEditorField
                            label="Name"
                            value={draftSection.name}
                            onChange={(value) => onDraftChange("name", value)}
                            error={draftErrors.name}
                    />

                    <div className="flex flex-col gap-2 text-sm text-slate-900">
                        <span>Type</span>
                        <div className="flex gap-2">
                            <button
                                    type="button"
                                    className={draftSection.type === "SEATED" ? activeButton : baseButton}
                                    onClick={() => onDraftChange("type", "SEATED")}
                            >
                                SEATED
                            </button>
                            <button
                                    type="button"
                                    className={draftSection.type === "STANDING" ? activeButton : baseButton}
                                    onClick={() => onDraftChange("type", "STANDING")}
                            >
                                STANDING
                            </button>
                        </div>
                    </div>

                    <div className="grid grid-cols-2 gap-3">
                        <SeatMapEditorField
                                label="Position X"
                                value={draftSection.positionX}
                                onChange={(value) => onDraftChange("positionX", value)}
                                error={draftErrors.positionX}
                                type="number"
                        />
                        <SeatMapEditorField
                                label="Position Y"
                                value={draftSection.positionY}
                                onChange={(value) => onDraftChange("positionY", value)}
                                error={draftErrors.positionY}
                                type="number"
                        />
                    </div>

                    <SeatMapEditorField
                            label="Border radius"
                            value={draftSection.borderRadius}
                            onChange={(value) => onDraftChange("borderRadius", value)}
                            error={draftErrors.borderRadius}
                            type="number"
                    />

                    <div className="flex flex-col gap-2 text-sm text-slate-900">
                        <span>Label position</span>
                        <div className="flex gap-2">
                            <button
                                    type="button"
                                    className={draftSection.labelPosition === "LEFT" ? activeButton : baseButton}
                                    onClick={() => onDraftChange("labelPosition", "LEFT")}
                            >
                                LEFT
                            </button>
                            <button
                                    type="button"
                                    className={draftSection.labelPosition === "RIGHT" ? activeButton : baseButton}
                                    onClick={() => onDraftChange("labelPosition", "RIGHT")}
                            >
                                RIGHT
                            </button>
                        </div>
                    </div>

                    {draftSection.type === "SEATED" ? (
                            <>
                                <div className="grid grid-cols-2 gap-3">
                                    <SeatMapEditorField
                                            label="Rows"
                                            value={draftSection.rowCount}
                                            onChange={(value) => onDraftChange("rowCount", value)}
                                            error={draftErrors.rowCount}
                                            type="number"
                                    />
                                    <SeatMapEditorField
                                            label="Seats / row"
                                            value={draftSection.seatsPerRow}
                                            onChange={(value) => onDraftChange("seatsPerRow", value)}
                                            error={draftErrors.seatsPerRow}
                                            type="number"
                                    />
                                </div>
                                <div className="grid grid-cols-2 gap-3">
                                    <SeatMapEditorField
                                            label="Row spacing"
                                            value={draftSection.rowSpacing}
                                            onChange={(value) => onDraftChange("rowSpacing", value)}
                                            error={draftErrors.rowSpacing}
                                            type="number"
                                    />
                                    <SeatMapEditorField
                                            label="Seat spacing"
                                            value={draftSection.seatSpacing}
                                            onChange={(value) => onDraftChange("seatSpacing", value)}
                                            error={draftErrors.seatSpacing}
                                            type="number"
                                    />
                                </div>
                                <SeatMapEditorField
                                        label="Start X"
                                        value={draftSection.startX}
                                        onChange={(value) => onDraftChange("startX", value)}
                                        error={draftErrors.startX}
                                        type="number"
                                />
                            </>
                    ) : (
                            <SeatMapEditorField
                                    label="Capacity"
                                    value={draftSection.capacity}
                                    onChange={(value) => onDraftChange("capacity", value)}
                                    error={draftErrors.capacity}
                                    type="number"
                            />
                    )}

                    <div className="flex justify-end gap-2 pt-1">
                        <button type="button" className={baseButton} onClick={onCancelDraft}>
                            Cancel
                        </button>
                        <button type="button" className={activeButton} onClick={onSaveDraft}>
                            Confirm Section
                        </button>
                    </div>
                </div>
            </div>
    );
};

export default SeatMapSectionDraftForm;
