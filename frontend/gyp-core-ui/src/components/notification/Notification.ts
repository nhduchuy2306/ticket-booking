import { notification } from "antd";
import { NotificationPlacement } from "antd/es/notification/interface";

export const NotificationType = Object.freeze({
    SUCCESS: Object.freeze({key: 'success', label: 'Success'}),
    ERROR: Object.freeze({key: 'error', label: 'Error'}),
    INFO: Object.freeze({key: 'info', label: 'Info'}),
    WARNING: Object.freeze({key: 'warning', label: 'Warning'})
});

export const createNotification = (
        title?: string,
        message?: string,
        type: keyof typeof NotificationType = "SUCCESS",
        placement: NotificationPlacement = "bottomRight"
) => {
    const notificationType = NotificationType[type];
    notification[notificationType.key]({
        message: title,
        description: message,
        placement: placement,
        duration: 3,
    });
}

export const createErrorNotification = (
        title?: string,
        message?: string,
        placement: NotificationPlacement = "bottomRight"
) => {
    createNotification(title, message, "ERROR", placement);
}

export const createSuccessNotification = (
        title?: string,
        message?: string,
        placement: NotificationPlacement = "bottomRight"
) => {
    createNotification(title, message, "SUCCESS", placement);
}