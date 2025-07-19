import { ColumnsType } from "antd/es/table";
import React from "react";
import SinglePageTable from "../../components/layout/singlepage/SinglePageTable.tsx";
import { EventModel } from "../../models/EventService/EventModel.ts";
import { SeatMapServiceAdapter } from "../../services/Event/SeatMapService.ts";

export interface SeatMapTableProps {
}

const SeatMapTable: React.FC<SeatMapTableProps> = () => {
    const columns: ColumnsType<EventModel> = [
        {
            title: 'ID',
            dataIndex: 'id',
            width: '20%',
        },
        {
            title: 'Name',
            dataIndex: 'name',
            sorter: true,
            width: '20%',
        },
        {
            title: 'Venue Type',
            dataIndex: 'venueType',
            sorter: true,
            width: '20%',
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