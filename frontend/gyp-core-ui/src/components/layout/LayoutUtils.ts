import { FormInstance } from "antd";
import { FormState } from "./models/LayoutModel.ts";

export const isFormReadOnly = (mode: string): boolean => {
    return mode === FormState.READ_ONLY.key;
}

export const isFormCreateMode = (mode: string): boolean => {
    return mode === FormState.CREATE.key;
}

export const isFormEditMode = (mode: string): boolean => {
    return mode === FormState.EDIT.key;
}

export const handleSubmit = async (
        values: any,
        form: FormInstance,
        onSave: (values: any) => Promise<void>
) => {
    await onSave(values);
    form.resetFields();
};

export const handleReset = (entity: any, form: FormInstance) => {
    if (entity) {
        form.setFieldsValue(entity);
    } else {
        form.resetFields();
    }
};