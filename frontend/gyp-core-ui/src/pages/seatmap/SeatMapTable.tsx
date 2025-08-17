import { ColumnsType } from "antd/es/table";
import React from "react";
import SinglePageTable from "../../components/layout/singlepage/SinglePageTable.tsx";
import { EventResponseDto } from "../../models/generated/event-service-models";
import { SeatMapServiceAdapter } from "../../services/Event/SeatMapService.ts";

export interface SeatMapTableProps {
}

const SeatMapTable: React.FC<SeatMapTableProps> = () => {
    const columns: ColumnsType<EventResponseDto> = [
        {
            title: 'Name',
            dataIndex: 'name',
            sorter: true,
        },
        {
            title: 'Venue Type',
            dataIndex: 'venueType',
            sorter: true,
        },
    ];

    return (
            <SinglePageTable
                    columns={columns}
                    createButtonTooltip="Create New Seat Map"
                    service={SeatMapServiceAdapter}
            />
    );
}

export default SeatMapTable;