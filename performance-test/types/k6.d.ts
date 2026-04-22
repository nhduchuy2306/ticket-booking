declare const __ENV: Record<string, string | undefined>;

declare module 'k6' {
  export function check<T>(value: T, sets: Record<string, (value: T) => boolean>): boolean;
  export function fail(message: string): never;
  export function sleep(seconds: number): void;
}

declare module 'k6/http' {
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
}

