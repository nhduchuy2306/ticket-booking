export function check<T>(value: T, sets: Record<string, (value: T) => boolean>): boolean;
export function fail(message: string): never;
export function sleep(seconds: number): void;

