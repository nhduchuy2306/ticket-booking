export const FormState = Object.freeze({
    EDIT: Object.freeze({key: "EDIT", value: "edit"}),
    ADD: Object.freeze({key: "ADD", value: "add"}),
    CREATE: Object.freeze({key: "CREATE", value: "create"}),
    READ_ONLY: Object.freeze({key: "READ_ONLY", value: "read_only"}),
});

export interface BaseEntity {
    id: string;
}

export interface BaseService<T, U> {
    getAll(): Promise<U[]>;

    create(entity: T): Promise<U>;

    update(entity: T, id: string): Promise<U>;

    delete(id: string): Promise<void>;
}