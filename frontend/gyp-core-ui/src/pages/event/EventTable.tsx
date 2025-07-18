import { Button, Tooltip } from "antd";
import React from "react";
import { RiLoopLeftFill } from "react-icons/ri";
import SinglePageTable from "../../components/layout/singlepage/SinglePageTable.tsx";
import { EventModel } from "../../models/EventService/EventModel.ts";
import { EventService, EventServiceAdapter } from "../../services/Event/EventService.ts";

export interface EventTableProps {
}

const EventTable: React.FC<EventTableProps> = () => {
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
            title: 'Status',
            dataIndex: 'status',
            width: '20%',
        },
        {
            title: 'Start Time',
            dataIndex: 'startTime',
            width: '20%',
        },
        {
            title: 'End Time',
            dataIndex: 'endTime',
            width: '20%',
        },
    ];

    const handleSyncDataClick = async () => {
        try {
            await EventService.syncEvents();
        } catch (error) {
            console.error('Failed to sync user accounts:', error);
        }
    };

    const customActions = (): React.ReactNode => {
        return (
                <>
                    <Tooltip title="Sync Event">
                        <Button type="primary" icon={<RiLoopLeftFill/>} onClick={handleSyncDataClick}></Button>
                    </Tooltip>
                </>
        );
    };

    return (
            <SinglePageTable
                    columns={columns}
                    createButtonTooltip="Create New User Account"
                    service={EventServiceAdapter}
                    customActions={customActions}
            />
    );
};

export default EventTable;