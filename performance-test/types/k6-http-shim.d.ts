export type Params = {
  headers?: Record<string, string>;
  tags?: Record<string, string>;
};

export type RefinedResponse<T = any> = {
  status: number;
  body?: string;
  json(): T;
};

export function get(url: string, params?: Params): RefinedResponse<any>;
export function post(url: string, body?: string, params?: Params): RefinedResponse<any>;

