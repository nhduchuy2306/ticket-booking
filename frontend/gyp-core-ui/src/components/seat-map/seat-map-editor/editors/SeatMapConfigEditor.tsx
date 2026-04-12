import React, { useState } from "react";
import { BiCopy } from "react-icons/bi";
import { Section } from "../../../../models/generated/event-service-models";
import type { DraftErrorMap } from "../../models/SeatMapModels.ts";
import { SectionDraftState, StageDraftState } from "../../models/SeatMapModels.ts";
import SeatMapEditorField from "./SeatMapEditorField.tsx";
import SeatMapSectionCard from "./SeatMapSectionCard.tsx";
import SeatMapSectionDraftForm from "./SeatMapSectionDraftForm.tsx";

export interface SeatMapConfigEditorProps {
    title?: string;
    stageDraft: StageDraftState;
    stageErrors: DraftErrorMap;
    sections: Section[];
    selectedSectionId: string | null;
    draftSection: SectionDraftState | null;
    draftErrors: DraftErrorMap;
    editingSectionId: string | null;
    onStageDraftChange: (field: keyof StageDraftState, value: string) => void;
    onOpenNewSection: () => void;
    onEditSection: (sectionId: string) => void;
    onDeleteSection: (sectionId: string) => void;
    onDraftChange: (field: keyof SectionDraftState, value: string) => void;
    onSaveDraft: () => void;
    onCancelDraft: () => void;
    onExportJson: () => void;
    onFocusSection?: (sectionId: string) => void;
    exportedJson: string;
    setShowSeatNumbersCb: ((show: boolean) => void) | null;
}

const SeatMapConfigEditor: React.FC<SeatMapConfigEditorProps> = ({
                                                                     title,
                                                                     stageDraft,
                                                                     stageErrors,
                                                                     sections,
                                                                     selectedSectionId,
                                                                     draftSection,
                                                                     draftErrors,
                                                                     editingSectionId,
                                                                     onStageDraftChange,
                                                                     onOpenNewSection,
                                                                     onEditSection,
                                                                     onDeleteSection,
                                                                     onDraftChange,
                                                                     onSaveDraft,
                                                                     onCancelDraft,
                                                                     onExportJson,
                                                                     onFocusSection,
                                                                     exportedJson,
                                                                     setShowSeatNumbersCb
                                                                 }) => {
    const isDraftNewSection = draftSection && !editingSectionId;
    const [showSeatNumbers, setShowSeatNumbers] = useState<boolean>();

    return (
            <div className="flex flex-col !h-full !min-h-0 w-full min-w-0 border border-slate-200 bg-white">
                <div className="shrink-0 border-b border-slate-200 !px-4 !py-4">
                    <h2 className="text-xl font-semibold text-slate-900">{title || "Seat Map Editor"}</h2>
                    <p className="!mt-1 text-sm text-slate-500">Form-based seat-map editor with live Konva preview.</p>
                </div>

                <div className="flex-1 min-h-0 overflow-auto">
                    <div className="!space-y-4 !px-4 !py-4">
                        <section className="border border-slate-200 bg-slate-50 p-4! rounded-2xl">
                            <div className="mb-3! flex items-center justify-between gap-3!">
                                <h3 className="text-sm font-semibold text-slate-900">Stage config</h3>
                            </div>
                            <div className="space-y-3!">
                                <SeatMapEditorField
                                        label="Label"
                                        value={stageDraft.label}
                                        onChange={(value) => onStageDraftChange("label", value)}
                                        error={stageErrors.label}
                                />
                                <div className="grid grid-cols-2 gap-3">
                                    <SeatMapEditorField
                                            label="Width"
                                            value={stageDraft.width}
                                            onChange={(value) => onStageDraftChange("width", value)}
                                            error={stageErrors.width}
                                            type="number"
                                    />
                                    <SeatMapEditorField
                                            label="Height"
                                            value={stageDraft.height}
                                            onChange={(value) => onStageDraftChange("height", value)}
                                            error={stageErrors.height}
                                            type="number"
                                    />
                                </div>
                                <div className="grid grid-cols-2 gap-3">
                                    <SeatMapEditorField
                                            label="Position X"
                                            value={stageDraft.positionX}
                                            onChange={(value) => onStageDraftChange("positionX", value)}
                                            error={stageErrors.positionX}
                                            type="number"
                                    />
                                    <SeatMapEditorField
                                            label="Position Y"
                                            value={stageDraft.positionY}
                                            onChange={(value) => onStageDraftChange("positionY", value)}
                                            error={stageErrors.positionY}
                                            type="number"
                                    />
                                </div>
                                <SeatMapEditorField
                                        label="Border radius"
                                        value={stageDraft.borderRadius}
                                        onChange={(value) => onStageDraftChange("borderRadius", value)}
                                        error={stageErrors.borderRadius}
                                        type="number"
                                />
                            </div>
                        </section>

                        <section className="border border-slate-200 bg-slate-50 p-4! rounded-2xl">
                            <div className="mb-3! flex items-center justify-between gap-3">
                                <h3 className="text-sm font-semibold text-slate-900">Sections</h3>
                                <button
                                        type="button"
                                        className="border border-slate-900 px-3! py-2! text-sm hover:text-white! transition hover:bg-slate-800 rounded-xl cursor-pointer"
                                        onClick={onOpenNewSection}
                                >
                                    Add Section
                                </button>
                            </div>

                            <div className="mb-3! flex items-center justify-between gap-3">
                                <SeatMapEditorField
                                        label="Show Seat Number"
                                        value={showSeatNumbers ? "true" : "false"}
                                        type="switch"
                                        onChange={(value) => {
                                            if (setShowSeatNumbersCb) {
                                                setShowSeatNumbers(value === "true");
                                                setShowSeatNumbersCb(value === "true");
                                            }
                                        }}
                                        error={draftErrors.name}
                                />
                            </div>

                            {isDraftNewSection && draftSection ? (
                                    <div className="mb-3!">
                                        <SeatMapSectionDraftForm
                                                draftSection={draftSection}
                                                draftErrors={draftErrors}
                                                onDraftChange={onDraftChange}
                                                onSaveDraft={onSaveDraft}
                                                onCancelDraft={onCancelDraft}
                                        />
                                    </div>
                            ) : null}

                            <div className="space-y-3!">
                                {sections.length === 0 ? (
                                        <div className="text-sm text-slate-500">No sections yet.</div>
                                ) : sections.map((section) => {
                                    const isSelected = selectedSectionId === section.id;
                                    const isEditing = editingSectionId === section.id && draftSection;

                                    return (
                                            <SeatMapSectionCard
                                                    key={section.id}
                                                    section={section}
                                                    isSelected={isSelected}
                                                    onSelect={(sectionId) => onFocusSection?.(sectionId)}
                                                    onEdit={onEditSection}
                                                    onDelete={onDeleteSection}
                                            >
                                                {isEditing && draftSection ? (
                                                        <div className="mt-3!">
                                                            <SeatMapSectionDraftForm
                                                                    draftSection={draftSection}
                                                                    draftErrors={draftErrors}
                                                                    onDraftChange={onDraftChange}
                                                                    onSaveDraft={onSaveDraft}
                                                                    onCancelDraft={onCancelDraft}
                                                            />
                                                        </div>
                                                ) : null}
                                            </SeatMapSectionCard>
                                    );
                                })}
                            </div>
                        </section>

                        <section className="border border-slate-200 bg-slate-50 p-4! rounded-2xl">
                            <div className="!mb-3 flex items-center justify-between gap-3">
                                <h3 className="text-sm font-semibold text-slate-900">Export</h3>
                                <div className="flex flex-wrap justify-end gap-2">
                                    <button type="button"
                                            className="border border-slate-300 px-3! py-2! text-sm hover:text-white! transition hover:bg-slate-800 rounded-xl cursor-pointer"
                                            onClick={onExportJson}
                                    >
                                        Export JSON
                                    </button>
                                    <button type="button"
                                            className="border border-slate-300 bg-white px-3! py-2! text-sm text-slate-900 transition hover:border-slate-400 rounded-xl cursor-pointer"
                                            onClick={() => navigator.clipboard.writeText(exportedJson)}
                                    ><BiCopy/></button>
                                </div>
                            </div>
                            <textarea
                                    readOnly
                                    value={exportedJson}
                                    placeholder="Exported seat-map JSON will appear here."
                                    className="min-h-48 w-full border border-slate-300 bg-white !p-3 font-mono text-xs text-slate-900 outline-none resize-y rounded-2xl"
                            />
                        </section>
                    </div>
                </div>
            </div>
    );
};

export default SeatMapConfigEditor;