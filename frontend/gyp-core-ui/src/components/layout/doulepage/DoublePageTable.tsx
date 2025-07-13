import { Button, Flex, Modal, notification, Table, TableProps, Tooltip } from "antd";
import React, { useEffect, useState } from "react";
import { BiPlus } from "react-icons/bi";
import { ThemeColors } from "../../../configs/Constants.ts";
import { BaseService } from "../models/LayoutModel.ts";
import { useDoublePageContext } from "./DoublePageContext.tsx";

export interface DoublePageTableProps {
    columns: TableProps<any>['columns'];
    service: BaseService<any, any>;
    createButtonTooltip?: string;
    onDelete?: (entity: any) => Promise<void>;
    additionalActions?: (entity: any) => React.ReactNode;
    pagination?: {
        pageSize?: number;
        showSizeChanger?: boolean;
        showTotal?: (total: number, range: [number, number]) => string;
    };
    rowSelection?: TableProps<any>['rowSelection'];
}

export const DoublePageTable: React.FC<DoublePageTableProps> = ({
                                                                    columns,
                                                                    service,
                                                                    createButtonTooltip = "Add New Item",
                                                                    onDelete,
                                                                    additionalActions,
                                                                    pagination = {pageSize: 5, showSizeChanger: false},
                                                                    rowSelection
                                                                }) => {
    const {
        handleCreate,
        handleEdit,
        handleView,
        reload,
        setReload,
        isLoading,
        setIsLoading
    } = useDoublePageContext();

    const [modal, modalContextHolder] = Modal.useModal();
    const [data, setData] = useState<any[]>([]);
    const [paginationState, setPaginationState] = useState({
        current: 1,
        pageSize: pagination.pageSize || 5,
        total: 0,
    });
    const [selectedRowId, setSelectedRowId] = useState<string>('');

    useEffect(() => {
        void fetchData();
    }, []);

    useEffect(() => {
        if (reload) {
            void (async () => {
                await fetchData();
                setReload(false);
            })();
        }
    }, [reload]);

    const fetchData = async () => {
        setIsLoading(true);
        try {
            const entities = await service.getAll();
            setData(entities);
            setPaginationState(prev => ({...prev, total: entities.length}));
        } catch (error) {
            console.error('Failed to fetch data:', error);
        } finally {
            setIsLoading(false);
        }
    };

    const handleDelete = async (entity: any) => {
        try {
            if (onDelete) {
                await onDelete(entity);
            } else {
                await service.delete(entity.id);
                await new Promise(resolve => setTimeout(resolve, 300));
                void fetchData();
                notification.success({
                    message: "Delete successfully",
                    placement: "bottomRight"
                });
            }
        } catch (error: any) {
            modal.error({
                title: "Error",
                content: error.response?.data || "Failed to delete item",
            });
        }
    };

    const buildColumns = () => {
        const actionColumn = {
            title: 'Actions',
            width: '10%',
            render: (_: any, record: object) => (
                    <Flex gap="small">
                        {additionalActions?.(record)}
                        <Button
                                type="text"
                                danger
                                onClick={(e) => {
                                    e.stopPropagation();
                                    modal.confirm({
                                        title: 'Confirm',
                                        content: 'Do you want to remove this record?',
                                        onOk: () => handleDelete(record),
                                    });
                                }}
                        >
                            Delete
                        </Button>
                    </Flex>
            )
        };

        return [...(columns || []), actionColumn];
    };

    const handleRowClick = (record: any) => {
        if (record.id === selectedRowId) {
            handleView(null);
            setSelectedRowId('');
        } else {
            handleView(record);
            setSelectedRowId(record.id);
        }
    };

    const handleRowDoubleClick = (record: any) => {
        handleEdit(record);
    };

    const handleTableChange = (newPagination: any) => {
        setPaginationState({
            ...paginationState,
            current: newPagination.current,
        });
    };

    const rowProps = (record: any): React.HTMLAttributes<HTMLElement> => ({
        onDoubleClick: () => handleRowDoubleClick(record),
        onClick: () => handleRowClick(record),
        style: {
            cursor: "pointer",
            background: record.id === selectedRowId ? ThemeColors.SELECTED : 'transparent',
        },
    });

    return (
            <div className="flex-1">
                <Flex gap="middle" vertical align="flex-end">
                    <Tooltip title={createButtonTooltip}>
                        <Button type="default" icon={<BiPlus/>} onClick={handleCreate}/>
                    </Tooltip>
                    <Table
                            columns={buildColumns()}
                            dataSource={data}
                            loading={isLoading}
                            rowKey={(record) => (record as any).id}
                            onRow={rowProps}
                            rowSelection={rowSelection}
                            pagination={{
                                ...paginationState,
                                showSizeChanger: pagination.showSizeChanger,
                                showTotal: pagination.showTotal || ((total, range) => `${range[0]}-${range[1]} of ${total} items`),
                            }}
                            onChange={handleTableChange}
                    />
                </Flex>
                {modalContextHolder}
            </div>
    );
};