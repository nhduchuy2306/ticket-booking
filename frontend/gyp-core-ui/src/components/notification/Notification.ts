import { notification } from "antd";
import { NotificationPlacement } from "antd/es/notification/interface";
import { NotificationType } from "../../models/enums/NotificationType.ts";

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