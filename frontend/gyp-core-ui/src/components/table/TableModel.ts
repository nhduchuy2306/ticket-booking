import type { GetProp, TableProps } from "antd";
import type { SorterResult } from "antd/es/table/interface";

type TablePaginationConfig = Exclude<GetProp<TableProps, 'pagination'>, boolean>;

export interface TableParams {
    pagination?: TablePaginationConfig;
    sortField?: SorterResult<any>['field'];
    sortOrder?: SorterResult<any>['order'];
    filters?: Parameters<GetProp<TableProps, 'onChange'>>[1];
}