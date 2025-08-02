import { Button, Flex, Modal, notification, Table, TableProps, Tooltip } from "antd";
import React, { useEffect, useState } from "react";
import { BiPlus } from "react-icons/bi";
import { BaseService } from "../../../services/BaseService.ts";
import { useSinglePageContext } from "./SinglePageContext.tsx";

export interface SinglePageTableProps {
    columns: TableProps<any>['columns'];
    service: BaseService<any, any>;
    createButtonTooltip?: string;
    onDelete?: (entity: any) => Promise<void>;
    additionalActions?: (entity: any) => React.ReactNode;
    customActions?: () => React.ReactNode;
    pagination?: {
        pageSize?: number;
        showSizeChanger?: boolean;
        showTotal?: (total: number, range: [number, number]) => string;
    };
    rowSelection?: TableProps<any>['rowSelection'];
}

const SinglePageTable: React.FC<SinglePageTableProps> = ({
                                                             columns,
                                                             service,
                                                             createButtonTooltip = "Add New Item",
                                                             onDelete,
                                                             additionalActions,
                                                             customActions,
                                                             pagination = {pageSize: 10, showSizeChanger: true},
                                                             rowSelection,
                                                         }) => {
    const {
        handleCreate,
        handleEdit,
        handleView,
        reload,
        setReload,
        isLoading,
        setIsLoading
    } = useSinglePageContext();

    const [modal, modalContextHolder] = Modal.useModal();
    const [data, setData] = useState<any[]>([]);
    const [paginationState, setPaginationState] = useState({
        current: 1,
        pageSize: pagination.pageSize || 10,
        total: 0,
    });

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
                await service.delete((entity as any).id);
                await new Promise(resolve => setTimeout(resolve, 300));
                void fetchData();
                notification.success({message: "Delete successfully"});
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
            width: '150px',
            render: (_: any, record: any) => (
                    <Flex gap="small">
                        {additionalActions?.(record)}
                        <Button
                                type="link"
                                onClick={(e) => {
                                    e.stopPropagation();
                                    handleView(record);
                                }}
                        >
                            View
                        </Button>
                        <Button
                                type="link"
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

    const handleRowDoubleClick = (record: any) => {
        handleEdit(record);
    };

    const handleTableChange = (newPagination: any) => {
        setPaginationState({
            ...paginationState,
            current: newPagination.current,
            pageSize: newPagination.pageSize,
        });
    };

    const rowProps = (record: any): React.HTMLAttributes<HTMLElement> => ({
        onDoubleClick: () => handleRowDoubleClick(record),
        style: {cursor: "pointer"},
    });

    return (
            <div className="p-6! overflow-auto! h-[calc(100vh-100px)]!">
                <Flex justify="flex-end" align="center" gap={10} className="mb-4! mt-4!">
                    {customActions?.()}
                    <Tooltip title={createButtonTooltip}>
                        <Button type="primary" icon={<BiPlus/>} onClick={handleCreate}></Button>
                    </Tooltip>
                </Flex>
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
                        scroll={{x: 'max-content'}}
                />
                {modalContextHolder}
            </div>
    );
};

export default SinglePageTable;