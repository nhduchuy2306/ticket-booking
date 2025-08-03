import { Form } from "antd";
import { useEffect } from "react";
import { FormState } from "../../components/layout/models/LayoutModel.ts";

export interface UseFormLogicProps<T> {
    entity?: T;
    mode: string;
    onSave: (values: T) => Promise<void>;
}

export const useFormLogic = <T, >({entity, mode, onSave}: UseFormLogicProps<T>) => {
    const [form] = Form.useForm();

    useEffect(() => {
        if (entity) {
            form.setFieldsValue(entity);
        } else {
            form.resetFields();
        }
    }, [entity, form]);

    const isReadOnly = mode === FormState.READ_ONLY.key;
    const isCreateMode = mode === FormState.CREATE.key;
    const isEditMode = mode === FormState.EDIT.key;

    const handleSubmit = async (values: T) => {
        await onSave(values);
        form.resetFields();
    };

    const handleReset = () => {
        if (entity) {
            form.setFieldsValue(entity);
        } else {
            form.resetFields();
        }
    };

    return {
        form,
        isReadOnly,
        isCreateMode,
        isEditMode,
        handleSubmit,
        handleReset
    };
}