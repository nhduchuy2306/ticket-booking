import { Card, Select } from "antd";
import React, { useEffect, useMemo, useState } from "react";
import SeatMapPreviewCanvasSimple from "../../../components/seat-map/seat-map-editor/SeatMapPreviewCanvasSimple.tsx";
import { SeatMapResponseDto, TicketTypeResponseDto, } from "../../../models/generated/event-service-models";

interface EventTicketTypeFormProps {
    seatMap?: SeatMapResponseDto | null;
    ticketTypes: TicketTypeResponseDto[];
    assignments: Record<string, string>;
    eventSectionMappings: Record<string, string>;
    onAssignmentsChange: (assignments: Record<string, string>) => void;
}

const EventTicketTypeForm: React.FC<EventTicketTypeFormProps> = ({
                                                                     seatMap,
                                                                     ticketTypes,
                                                                     assignments,
                                                                     eventSectionMappings,
                                                                     onAssignmentsChange,
                                                                 }) => {
    const [ticketTypeColors, setTicketTypeColors] = useState<Record<string, string>>({});
    const sections = seatMap?.seatConfig?.sections || [];

    const ticketTypeOptions = useMemo(() => ticketTypes.map((ticketType) => ({
        label: `${ticketType.name} - ${ticketType.price} ${ticketType.currency}`,
        value: ticketType.id
    })), [ticketTypes]);

    const ticketTypesMap: Record<string, TicketTypeResponseDto> = useMemo(() => ticketTypes.reduce((map, ticketType) => {
        map[ticketType.id] = ticketType;
        return map;
    }, {} as Record<string, TicketTypeResponseDto>), [ticketTypes]);

    const totalSeated = useMemo(() => sections
            .filter((section) => section.type === "SEATED")
            .reduce((sum, section) => {
                const rows = section.rows || [];
                const rowCount = rows.reduce((rowSum, row) => rowSum + (row.seats?.length || 0), 0);
                return sum + rowCount;
            }, 0), [sections]);

    const totalStanding = useMemo(() => sections
            .filter((section) => section.type === "STANDING")
            .reduce((sum, section) => sum + (section.capacity || 0), 0), [sections]);

    const totalCapacity = totalSeated + totalStanding;

    useEffect(() => {
        Object.keys(eventSectionMappings).forEach((sectionId) => {
            const ticketTypeId = eventSectionMappings[sectionId];
            const ticketType = ticketTypesMap[ticketTypeId];
            if (ticketType) {
                setTicketTypeColors((prevColors) => ({
                    ...prevColors,
                    [sectionId]: ticketType.color,
                }));
            }
        });
    }, [ticketTypesMap]);

    const updateSectionTicketType = (sectionId: string, ticketTypeId?: string) => {
        onAssignmentsChange({
            ...assignments,
            [sectionId]: ticketTypeId || "",
        });
        const ticketType = ticketTypesMap[ticketTypeId || ""];
        setTicketTypeColors({
            ...ticketTypeColors,
            [sectionId]: ticketType?.color,
        })
    };

    return (
            <div className="grid grid-cols-1 gap-4 xl:grid-cols-2">
                <Card className="!border-[#3a3a3a] !bg-[#252525] text-white p-5!">
                    <div className="mb-4!">
                        <h2 className="text-[18px] font-semibold text-white">
                            Assign Ticket Type for each Section Area
                        </h2>
                        <p className="mt-1! text-sm text-slate-300">
                            Each section in seat map need to be assigned 1 type of ticket respectively.
                        </p>
                    </div>

                    <div className="space-y-3!">
                        {sections.map((section) => {
                            const currentTicketTypeId = eventSectionMappings[section.id] || "";
                            return (
                                    <div key={section.id}
                                         className="flex flex-col gap-3 rounded-2xl border border-white/10 bg-white/5 p-3! md:flex-row md:items-center md:justify-between">
                                        <div className="min-w-0">
                                            <div className="flex items-center gap-2">
                                                <span className="text-base font-semibold text-white">{section.name}</span>
                                                <span className="rounded-full bg-sky-500/20 px-2! py-0.5! text-[11px] font-semibold uppercase tracking-wide text-sky-200">
                                                {section.type}
                                            </span>
                                            </div>
                                            <div className="mt-1 text-xs text-slate-400">
                                                {section.type === "SEATED"
                                                        ? `${section.rows?.length || 0} hàng • ${section.rows?.reduce((sum, row) => sum + (row.seats?.length || 0), 0) || 0} ghế`
                                                        : `${section.capacity || 0} chỗ đứng`}
                                            </div>
                                        </div>

                                        <div className="md:w-[280px]">
                                            <Select
                                                    className="w-full"
                                                    value={currentTicketTypeId || undefined}
                                                    placeholder="Chọn loại vé"
                                                    options={ticketTypeOptions}
                                                    onChange={(value) => updateSectionTicketType(section.id, value)}
                                                    allowClear
                                            />
                                        </div>
                                    </div>
                            );
                        })}
                    </div>
                </Card>

                <div className="space-y-4!">
                    <Card className="!border-[#3a3a3a] !bg-[#252525] text-white p-5!">
                        <h2 className="text-[18px] font-semibold text-white">Preview Seat Map</h2>
                        <div className="mt-4 h-[340px] rounded-2xl bg-[#1f1f1f] p-3">
                            {seatMap?.seatConfig && seatMap?.stageConfig
                                    ? <SeatMapPreviewCanvasSimple
                                            stageConfig={seatMap?.stageConfig || {}}
                                            seatConfig={seatMap?.seatConfig || {}}
                                            ticketTypeColors={ticketTypeColors}
                                    />
                                    : <div className="flex h-full items-center justify-center text-sm text-slate-400">
                                        No seat map data available
                                    </div>
                            }
                        </div>
                    </Card>

                    <Card className="!border-[#3a3a3a] !bg-[#252525] text-white p-5!">
                        <h2 className="text-[18px] font-semibold text-white">Seat Map Summary</h2>
                        <div className="mt-4 space-y-3 text-sm text-slate-200">
                            <div className="flex items-center justify-between border-b border-white/5 pb-2!">
                                <span className="text-slate-400">Total seated capacity</span>
                                <span className="font-semibold text-white">{totalSeated}</span>
                            </div>
                            <div className="flex items-center justify-between border-b border-white/5 pb-2!">
                                <span className="text-slate-400">Total standing capacity</span>
                                <span className="font-semibold text-white">{totalStanding}</span>
                            </div>
                            <div className="flex items-center justify-between">
                                <span className="text-slate-400">Total capacity</span>
                                <span className="font-semibold text-white">{totalCapacity}</span>
                            </div>
                        </div>
                    </Card>
                </div>
            </div>
    );
};

export default EventTicketTypeForm;


