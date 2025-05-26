import { Table } from "antd";
import React from "react";
import { DataTableProps } from "./TableModel.ts";

const DataTable = <T extends object>({
                                         data,
                                         columns,
                                         loading = false,
                                         rowKey,
                                         onRowDoubleClick,
                                         onRowClick,
                                     }: DataTableProps<T>) => {
    const rowProps = (record: T): React.HTMLAttributes<HTMLElement> => {
        return {
            onDoubleClick: () => onRowDoubleClick?.(record),
            onClick: () => onRowClick?.(record),
            style: {cursor: onRowDoubleClick ? "pointer" : "default"},
        };
    };

    return (
            <Table<T>
                    columns={columns}
                    dataSource={data}
                    loading={loading}
                    rowKey={rowKey}
                    onRow={onRowDoubleClick || onRowClick ? rowProps : undefined}
            />
    );
}

export default DataTable;