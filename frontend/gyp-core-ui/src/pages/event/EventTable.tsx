import { Button, Tag, Tooltip } from "antd";
import { ColumnsType } from "antd/es/table";
import React from "react";
import { RiLoopLeftFill } from "react-icons/ri";
import { useNavigate } from "react-router-dom";
import SinglePageTable from "../../components/layout/singlepage/SinglePageTable.tsx";
import { EventResponseDto } from "../../models/generated/event-service-models";
import { EventService, EventServiceAdapter } from "../../services/Event/EventService.ts";
import { DateUtils } from "../../utils/DateUtils.ts";

export interface EventTableProps {
}

const EventTable: React.FC<EventTableProps> = () => {
    const navigate = useNavigate();
    const columns: ColumnsType<EventResponseDto> = [
        {
            title: 'Name',
            dataIndex: 'name',
            sorter: true,
        },
        {
            title: 'Status',
            dataIndex: 'status',
            render: (text: string) => {
                const statusColor = {
                    "DRAFT": 'blue',
                    "PENDING_APPROVAL": 'orange',
                    "PUBLISHED": 'green',
                    "CANCELED": 'red',
                    "POSTPONED": 'purple',
                    "COMPLETED": 'cyan',
                } as const;
                type StatusKey = keyof typeof statusColor;
                return (
                        <Tag color={statusColor?.[text as StatusKey] || 'default'} style={{cursor: 'pointer'}}>
                            {text}
                        </Tag>
                );
            }
        },
        {
            title: 'Generated',
            dataIndex: 'isGenerated',
            render: (text: boolean) => {
                return (
                        <Tag color={text ? 'green' : 'red'} style={{cursor: 'pointer'}}>
                            {text ? 'True' : 'False'}
                        </Tag>
                );
            }
        },
        {
            title: 'Start Time',
            dataIndex: 'startTime',
            render: (text: string) => {
                return (
                        <span>{text ? DateUtils.formatToDateTime(text) : 'N/A'}</span>
                );
            }
        },
        {
            title: 'End Time',
            dataIndex: 'endTime',
            render: (text: string) => {
                return (
                        <span>{text ? DateUtils.formatToDateTime(text) : 'N/A'}</span>
                );
            }
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

    const handleAssignTicketType = (entity: EventResponseDto) => {
        EventService.navigate(navigate, '/assign', entity);
    }

    const additionalActions = (entity: EventResponseDto): React.ReactNode => {
        return (
                <Tooltip title="Assign Ticket Types">
                    <Button type="default" onClick={() => handleAssignTicketType(entity)}>Assign</Button>
                </Tooltip>
        );
    }

    return (
            <SinglePageTable
                    columns={columns}
                    createButtonTooltip="Create New Event"
                    service={EventServiceAdapter}
                    customActions={customActions}
                    additionalActions={additionalActions}
            />
    );
};

export default EventTable;