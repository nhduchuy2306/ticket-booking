import React from "react";
import type { Section } from "../../../../models/generated/event-service-models";

export interface SeatMapSectionCardProps {
    section: Section;
    isSelected: boolean;
    onSelect: (sectionId: string) => void;
    onEdit: (sectionId: string) => void;
    onDelete: (sectionId: string) => void;
    children?: React.ReactNode;
}

const SeatMapSectionCard: React.FC<SeatMapSectionCardProps> = ({
                                                                   section,
                                                                   isSelected,
                                                                   onSelect,
                                                                   onEdit,
                                                                   onDelete,
                                                                   children
                                                               }) => {
    return (
            <div
                    className={`border bg-white p-4! shadow-sm transition rounded-2xl ${isSelected ? "border-slate-900" : "border-slate-200"}`}
                    onClick={() => onSelect(section.id)}
            >
                <div className="flex items-start justify-between gap-3">
                    <div className="flex flex-col gap-1.5">
                        <div className="text-sm font-semibold text-slate-900">{section.name}</div>
                        <div className="inline-flex w-fit border border-slate-300 px-2.5! py-1! text-xs text-slate-700 rounded-xl">
                            {section.type}
                        </div>
                    </div>

                    <div className="flex flex-wrap justify-end gap-2">
                        <button
                                type="button"
                                className="border border-slate-300 bg-white px-3! py-2! text-sm text-slate-900 transition hover:border-slate-400 rounded-xl cursor-pointer"
                                onClick={(event) => {
                                    event.stopPropagation();
                                    onEdit(section.id);
                                }}
                        >
                            Edit
                        </button>
                        <button
                                type="button"
                                className="border border-red-200 bg-red-50 px-3! py-2! text-sm text-red-700 transition hover:border-red-300 rounded-xl cursor-pointer"
                                onClick={(event) => {
                                    event.stopPropagation();
                                    onDelete(section.id);
                                }}
                        >
                            Delete
                        </button>
                    </div>
                </div>

                <div className="mt-2! text-xs text-slate-500">
                    {section.type === "SEATED"
                            ? `${section.rows?.length || 0} rows`
                            : `Capacity: ${section.capacity || 0}`}
                </div>
                {children}
            </div>
    );
};

export default SeatMapSectionCard;
