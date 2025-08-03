import { TableProps } from "antd";
import React from "react";
import { DoublePageTable } from "../../components/layout/doulepage/DoublePageTable.tsx";
import { VenueMapServiceAdapter } from "../../services/Event/VenueMapService.ts";

interface VenueMapTableProps {

}

const VenueMapTable: React.FC<VenueMapTableProps> = () => {
    const columns: TableProps['columns'] = [
        {
            title: 'ID',
            dataIndex: 'id',
            key: 'id',
            width: '10%',
        },
        {
            title: 'Name',
            dataIndex: 'name',
            key: 'name',
            width: '40%',
        },
        {
            title: 'Seat Map',
            dataIndex: 'seatMapName',
            key: 'seatMapName',
            width: '40%',
        },
        {
            title: 'Venue',
            dataIndex: 'venueName',
            key: 'venueName',
            width: '40%',
        },
    ];

    return (
            <DoublePageTable
                    columns={columns}
                    service={VenueMapServiceAdapter}
                    createButtonTooltip="Create New Venue Map"
                    pagination={{
                        pageSize: 5,
                        showSizeChanger: false,
                        showTotal: (total, range) => `${range[0]}-${range[1]} of ${total} categories`
                    }}
            />
    );
}

export default VenueMapTable;