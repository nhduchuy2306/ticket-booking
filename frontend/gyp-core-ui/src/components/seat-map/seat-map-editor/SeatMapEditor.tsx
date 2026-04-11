import { Splitter } from "antd";
import React, { useEffect, useMemo, useState } from "react";
import type {
    SeatConfig,
    Section,
    StageConfig,
} from "../../../models/generated/event-service-models";
import { DraftErrorMap, SectionDraftState, StageDraftState } from "../models/SeatMapModels.ts";
import SeatMapConfigEditor from "./editors/SeatMapConfigEditor";
import SeatMapPreviewCanvas from "./SeatMapPreviewCanvas";
import {
    buildSectionFromDraft,
    buildStageConfigFromDraft,
    createDefaultSectionDraft,
    createDefaultStageDraft,
} from "./utils/SeatMapEditorUtils.ts";
import { SeatMapEditorContext } from "./context/SeatMapEditorContext.tsx";

export interface SeatMapEditorData {
    stageConfig: StageConfig;
    seatConfig: SeatConfig;
}

export interface SeatMapEditorProps {
    initialData?: SeatMapEditorData;
    onSave?: (data: SeatMapEditorData) => void;
    title?: string;
}

const DEFAULT_STAGE_CONFIG: StageConfig = {
    name: "STAGE",
    position: {x: 0, y: 24},
    dimensions: {width: 800, height: 150},
    borderRadius: 12,
};

const SeatMapEditor: React.FC<SeatMapEditorProps> = ({initialData, onSave, title}) => {
    const [stageDraft, setStageDraft] = useState<StageDraftState>(createDefaultStageDraft(initialData?.stageConfig));
    const [stageConfig, setStageConfig] = useState<StageConfig>(initialData?.stageConfig || DEFAULT_STAGE_CONFIG);
    const [seatConfig, setSeatConfig] = useState<SeatConfig>({sections: initialData?.seatConfig?.sections || []});
    const [draftSection, setDraftSection] = useState<SectionDraftState | null>(null);
    const [editingSectionId, setEditingSectionId] = useState<string | null>(null);
    const [selectedSectionId, setSelectedSectionId] = useState<string | null>(initialData?.seatConfig?.sections?.[0]?.id || null);
    const [stageErrors, setStageErrors] = useState<DraftErrorMap>({});
    const [draftErrors, setDraftErrors] = useState<DraftErrorMap>({});
    const [exportedJson, setExportedJson] = useState<string>("");
    const [showSeatNumbers, setShowSeatNumbers] = useState<boolean>(false);

    useEffect(() => {
        const nextStageDraft = createDefaultStageDraft(initialData?.stageConfig);
        const nextStage = buildStageConfigFromDraft(nextStageDraft);

        setStageDraft(nextStageDraft);
        setStageErrors(nextStage.errors);
        if (nextStage.stageConfig) {
            setStageConfig(nextStage.stageConfig);
        } else {
            setStageConfig(initialData?.stageConfig || DEFAULT_STAGE_CONFIG);
        }

        setSeatConfig({sections: initialData?.seatConfig?.sections || []});
        setDraftSection(null);
        setEditingSectionId(null);
        setSelectedSectionId(initialData?.seatConfig?.sections?.[0]?.id || null);
        setDraftErrors({});
        setExportedJson("");
    }, [initialData]);

    const updateStageDraft = (field: keyof StageDraftState, value: string) => {
        const nextDraft = {...stageDraft, [field]: value};
        setStageDraft(nextDraft);

        const nextStage = buildStageConfigFromDraft(nextDraft);
        setStageErrors(nextStage.errors);
        if (nextStage.stageConfig) {
            setStageConfig(nextStage.stageConfig);
        }
    };

    const previewSection = useMemo(() => {
        if (!draftSection) {
            return null;
        }

        const previewId = editingSectionId || "__draft-section__";
        const parsed = buildSectionFromDraft(draftSection, previewId, false);
        return parsed.section || null;
    }, [draftSection, editingSectionId]);

    const previewSeatConfig = useMemo<SeatConfig>(() => {
        if (!previewSection) {
            return seatConfig;
        }

        const sections = seatConfig.sections || [];
        const existingIndex = editingSectionId
                ? sections.findIndex((section) => section.id === editingSectionId)
                : -1;

        if (existingIndex >= 0) {
            return {
                sections: sections.map((section) => (section.id === editingSectionId ? previewSection : section)),
            };
        }

        return {
            sections: [...sections, previewSection],
        };
    }, [editingSectionId, previewSection, seatConfig]);

    const updateSectionDraft = (field: keyof SectionDraftState, value: string) => {
        if (!draftSection) {
            return;
        }

        const nextDraft = {...draftSection, [field]: value};
        setDraftSection(nextDraft);

        const previewId = editingSectionId || "__draft-section__";
        const parsed = buildSectionFromDraft(nextDraft, previewId, false);
        setDraftErrors(parsed.errors);
    };

    const openNewSectionDraft = () => {
        const nextDraft = createDefaultSectionDraft(seatConfig.sections || []);
        setDraftSection(nextDraft);
        setEditingSectionId(null);
        setSelectedSectionId("__draft-section__");
        const parsed = buildSectionFromDraft(nextDraft, "__draft-section__", false);
        setDraftErrors(parsed.errors);
    };

    const openSectionForEdit = (sectionId: string) => {
        const sections = seatConfig.sections || [];
        const section = sections.find((item) => item.id === sectionId);
        if (!section) {
            return;
        }

        const nextDraft: SectionDraftState = {
            name: section.name || "",
            type: section.type || "SEATED",
            ticketTypeId: section.ticketTypeId || "",
            positionX: String(section.position?.x || 0),
            positionY: String(section.position?.y || 0),
            borderRadius: String(section.borderRadius || 0),
            labelPosition: section.labelPosition || "LEFT",
            rowCount: String(section.type === "SEATED" ? section.rows?.length || 1 : 0),
            seatsPerRow: String(section.type === "SEATED" ? section.rows?.[0]?.seats?.length || 1 : 0),
            rowSpacing: String(section.type === "SEATED" ? section.rowSpacing || 50 : 0),
            seatSpacing: String(section.type === "SEATED" ? section.rows?.[0]?.seatSpacing || 50 : 0),
            startX: String(section.type === "SEATED" ? section.rows?.[0]?.position?.x || 40 : 0),
            capacity: String(section.capacity || 0),
        };

        setDraftSection(nextDraft);
        setEditingSectionId(sectionId);
        setSelectedSectionId(sectionId);

        const parsed = buildSectionFromDraft(nextDraft, sectionId, false);
        setDraftErrors(parsed.errors);
    };

    const cancelDraft = () => {
        setDraftSection(null);
        setEditingSectionId(null);
        setDraftErrors({});
        setSelectedSectionId((seatConfig.sections || [])[0]?.id || null);
    };

    const saveDraftSection = () => {
        if (!draftSection) {
            return;
        }

        const sectionId = editingSectionId || crypto.randomUUID();
        const parsed = buildSectionFromDraft(draftSection, sectionId, true);

        if (!parsed.section) {
            setDraftErrors(parsed.errors);
            return;
        }

        const sections = seatConfig.sections || [];
        const nextSections = editingSectionId
                ? sections.map((section) => (section.id === editingSectionId ? parsed.section as Section : section))
                : [...sections, parsed.section as Section];

        setSeatConfig({sections: nextSections});
        setDraftSection(null);
        setEditingSectionId(null);
        setDraftErrors({});
        setSelectedSectionId(parsed.section.id);
    };

    const deleteSection = (sectionId: string) => {
        const nextSections = (seatConfig.sections || []).filter((section) => section.id !== sectionId);
        setSeatConfig({sections: nextSections});

        if (selectedSectionId === sectionId) {
            setSelectedSectionId(nextSections[0]?.id || null);
        }
        if (editingSectionId === sectionId) {
            cancelDraft();
        }
    };

    const handleSelectSection = (sectionId?: string) => {
        if (!sectionId) {
            setSelectedSectionId(null);
            return;
        }

        setSelectedSectionId(sectionId);
    };

    const exportJson = () => {
        const data = {
            stageConfig,
            seatConfig: previewSeatConfig,
        };
        const json = JSON.stringify(data, null, 2);
        setExportedJson(json);
        console.log(json);
        onSave?.(data);
    };

    return (
            <SeatMapEditorContext.Provider value={{
                showSeatNumbers: showSeatNumbers,
                setShowSeatNumbers: setShowSeatNumbers,
            }}>
            <div className="h-full w-full min-h-0 flex-1 !overflow-auto">
                <Splitter layout="horizontal" style={{height: "100%", width: "100%"}}>
                    <Splitter.Panel size={500} min={340} max={700} style={{height: "100%", minHeight: 0, minWidth: 0}}>
                        <div className="h-[calc(100%-115px)]">
                            <SeatMapConfigEditor
                                    title={title}
                                    stageDraft={stageDraft}
                                    stageErrors={stageErrors}
                                    sections={seatConfig.sections || []}
                                    selectedSectionId={selectedSectionId}
                                    draftSection={draftSection}
                                    draftErrors={draftErrors}
                                    editingSectionId={editingSectionId}
                                    onStageDraftChange={updateStageDraft}
                                    onOpenNewSection={openNewSectionDraft}
                                    onEditSection={openSectionForEdit}
                                    onDeleteSection={deleteSection}
                                    onDraftChange={updateSectionDraft}
                                    onSaveDraft={saveDraftSection}
                                    onCancelDraft={cancelDraft}
                                    onExportJson={exportJson}
                                    onFocusSection={handleSelectSection}
                                    exportedJson={exportedJson}
                            />
                        </div>
                    </Splitter.Panel>
                    <Splitter.Panel style={{height: "100%", minHeight: 0, minWidth: 0}}>
                        <SeatMapPreviewCanvas
                                stageConfig={stageConfig}
                                seatConfig={previewSeatConfig}
                                selectedSectionId={selectedSectionId}
                                onSelectSection={handleSelectSection}
                        />
                    </Splitter.Panel>
                </Splitter>
            </div>
            </SeatMapEditorContext.Provider>
    );
};

export default SeatMapEditor;
