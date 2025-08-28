import { Button, Flex, Modal, Table, TableProps, Tag } from "antd";
import React, { useEffect, useState } from "react";
import { createErrorNotification } from "../../components/notification/Notification.ts";
import { TicketResponseDto } from "../../models/generated/ticket-service-models";
import { TicketService } from "../../services/Ticket/TicketService.ts";
import { DateUtils } from "../../utils/DateUtils.ts";

interface TicketTableProps {
    eventId?: string;
    refreshTrigger?: number;
}

const TicketTable: React.FC<TicketTableProps> = ({eventId, refreshTrigger}) => {
    const [generatedTickets, setGeneratedTickets] = useState<TicketResponseDto[]>();
    const [isLoading, setIsLoading] = useState<boolean>(true);
    const [modal, modalContextHolder] = Modal.useModal();

    useEffect(() => {
        const fetchGeneratedTickets = async () => {
            setIsLoading(true);
            if (!eventId) {
                return;
            }
            try {
                const response = await TicketService.getAllGeneratedTickets(eventId);
                if (response) {
                    setGeneratedTickets(response);
                } else {
                    setGeneratedTickets([]);
                }
            } catch (error) {
                setIsLoading(false);
                console.error("Error fetching generated tickets:", error);
                createErrorNotification(
                        "Failed to fetch generated tickets",
                        "An error occurred while fetching the generated tickets."
                );
            } finally {
                setIsLoading(false);
            }
        }
        void fetchGeneratedTickets();
    }, [eventId, refreshTrigger]);

    const renderDetailForm = (record: TicketResponseDto) => {
        return (
                <div>
                    <p><strong>Ticket Code:</strong> {record.ticketCode}</p>
                    <p><strong>Seat Info:</strong> {record.seatInfo}</p>
                    <p>
                        <strong>Ticket Type: </strong>
                        <Tag color={record.ticketTypeColor} style={{cursor: 'pointer'}}>
                            {record.ticketTypeName}
                        </Tag>
                    </p>
                    <p>
                        <strong>Event Date Time: </strong>
                        {DateUtils.formatToDateTime(record.eventDateTime as string)}
                    </p>
                    <p><strong>Status:</strong> {record.status}</p>
                </div>
        );
    }

    const columns: TableProps['columns'] = [
        {
            title: 'Ticket Code',
            dataIndex: 'ticketCode',
            key: 'ticketCode',
        },
        {
            title: 'Seat Info',
            dataIndex: 'seatInfo',
            key: 'seatInfo',
        },
        {
            title: 'Ticket Type',
            dataIndex: 'ticketTypeName',
            key: 'ticketTypeName',
            render: (ticketTypeName: string, record: TicketResponseDto) => {
                const color = record.ticketTypeColor;
                return (
                        <Tag color={color} style={{cursor: 'pointer'}}>
                            {ticketTypeName}
                        </Tag>
                );
            }
        },
        {
            title: 'Event Date Time',
            dataIndex: 'eventDateTime',
            key: 'eventDateTime',
            render: (dateTime: string) => {
                return DateUtils.formatToDateTime(dateTime);
            }
        },
        {
            title: 'Status',
            dataIndex: 'status',
            key: 'status',
            render: (status: string) => {
                const statusColor = {
                    AVAILABLE: 'green',
                    SOLD_OUT: 'red',
                    COMING_SOON: 'blue',
                    OFF_SALE: 'orange',
                    CANCELLED: 'gray',
                } as const;
                type StatusKey = keyof typeof statusColor;
                return (
                        <Tag color={statusColor?.[status as StatusKey] || 'default'} style={{cursor: 'pointer'}}>
                            {status}
                        </Tag>
                );
            }
        },
        {
            title: 'Action',
            render: (_, record: TicketResponseDto) => (
                    <Flex gap="small">
                        <Button
                                type="default"
                                onClick={(e) => {
                                    e.stopPropagation();
                                    console.log("Ticket Detail:", record);
                                    modal.info({
                                        title: 'Ticket Detail',
                                        content: renderDetailForm(record),
                                        maskClosable: true
                                    });
                                }}
                        >Detail</Button>
                    </Flex>
            )
        }
    ];

    return (
            <>
                <div className={generatedTickets && generatedTickets.length > 0 ? "!-mt-2 !h-full" : "bg-white p-4 flex flex-col justify-center items-center"}>
                    {generatedTickets && generatedTickets.length > 0 ? (
                            <Table
                                    columns={columns}
                                    dataSource={generatedTickets}
                                    loading={isLoading}
                                    rowKey={(record) => record.id}
                                    scroll={{y: 50 * 10}}
                            />
                    ) : (
                            <>
                                <h1 className="text-xl font-bold mb-4">Ticket Table</h1>
                                <p>This is where the ticket table will be displayed.</p>
                            </>
                    )}
                </div>
                {modalContextHolder}
            </>
    );
}

export default TicketTable;