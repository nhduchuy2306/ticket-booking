export interface BaseService<T, U> {
    getAll(): Promise<U[]>;

    create(entity: T): Promise<U>;

    update(entity: T, id: string): Promise<U>;

    delete(id: string): Promise<void>;

    getById(id: string): Promise<U>;
}