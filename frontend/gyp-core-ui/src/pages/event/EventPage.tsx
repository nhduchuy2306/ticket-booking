import { Button, Dropdown, Flex, Table, TableProps, Tooltip } from "antd";
import React, { useEffect, useState } from "react";
import { BiPlus } from "react-icons/bi";
import { RiLoopLeftFill } from "react-icons/ri";
import { useNavigate } from "react-router-dom";
import { EventModel } from "../../models/EventService/EventModel.ts";
import { EventService } from "../../services/Event/EventService.ts";

interface EventPageProps {
}

type ColumnsType<T extends object = object> = TableProps<T>['columns'];

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

const EventPage: React.FC<EventPageProps> = () => {
    const navigate = useNavigate();
    const [isLoading, setIsLoading] = React.useState<boolean>(false);
    const [data, setData] = React.useState<EventModel[]>([]);
    const [pagination, setPagination] = useState({
        current: 1,
        pageSize: 5,
        total: 0,
    });

    useEffect(() => {
        void fetchAllEvents();
    }, []);

    const fetchAllEvents = async () => {
        try {
            setIsLoading(true);
            const res = await EventService.getAllEvents();
            if (res && res.data) {
                setData(res.data);
            } else {
                console.error("No data received from EventService");
            }
        } catch (error) {
            console.error("Error fetching events:", error);
        } finally {
            setIsLoading(false);
        }
    }

    const buildColumn = () => {
        return [
            ...columns,
            {
                title: 'Actions',
                width: '10%',
                render: (_: any, record: EventModel) => (
                        <Dropdown menu={{
                            items: [
                                {
                                    key: 'delete',
                                    label: 'Delete',
                                    danger: true,
                                    onClick: (e) => {
                                        e.domEvent.stopPropagation();
                                        console.log(record);
                                    }
                                },
                                {
                                    key: 'edit',
                                    label: 'Edit',
                                    danger: false,
                                    onClick: (e) => {
                                        e.domEvent.stopPropagation();
                                        console.log(record);
                                        navigate(`/event/${record.id}`)
                                    }
                                }
                            ]
                        }}>
                            <Button type="text" onClick={(e) => e.stopPropagation()}>⋮</Button>
                        </Dropdown>
                )
            }
        ];
    }

    const RowProps = (record: EventModel): React.HTMLAttributes<HTMLElement> => ({
        onDoubleClick: (e) => {
            e.stopPropagation();
            navigate(`/event/${record.id}`);
        },
        style: {cursor: 'pointer'}
    });

    const handleTableChange = (newPagination: any) => {
        setPagination({
            ...pagination,
            current: newPagination.current,
        });
    };

    const handleCreateClick = () => {
        navigate("/event/new");
    }

    const handleSyncDataClick = async () => {
        setIsLoading(true);
        try {
            await EventService.syncEvents();
            await fetchAllEvents();
        } catch (error) {
            console.error('Failed to sync events:', error);
        } finally {
            setIsLoading(false);
        }
    }

    return (
            <Flex vertical={true} align="end" className="bg-white !pt-4">
                <Flex vertical={false} className="gap-2.5 !mr-2">
                    <Tooltip title="Sync">
                        <Button type="default" icon={<RiLoopLeftFill/>} onClick={handleSyncDataClick}
                                className="!mb-1"/>
                    </Tooltip>
                    <Tooltip title="Add New User Account">
                        <Button type="default" icon={<BiPlus/>} onClick={handleCreateClick} className="!mb-1"/>
                    </Tooltip>
                </Flex>
                <Table<EventModel>
                        className="w-[100%]"
                        columns={buildColumn()}
                        rowKey={(record) => record.id}
                        dataSource={data}
                        onRow={RowProps}
                        pagination={{
                            ...pagination,
                            showSizeChanger: false,
                            "showTotal": (total, range) => `${range[0]}-${range[1]} of ${total} items`,
                        }}
                        loading={isLoading}
                        onChange={handleTableChange}
                />
            </Flex>
    );
};

export default EventPage;