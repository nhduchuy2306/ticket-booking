import { apiWithoutAuth, ORDER_SERVICE_PATH } from "../ApiClient.ts";

const STORE_CONTROLLER_RESOURCE_PATH = "stores";
const COUNTDOWN_SESSION_PATH = "countdown-session";

export class StoreService {
    static getCountDownSession = async (sessionId: string) => {
        const response = await apiWithoutAuth.get(`${ORDER_SERVICE_PATH}/${STORE_CONTROLLER_RESOURCE_PATH}/${COUNTDOWN_SESSION_PATH}/${sessionId}`);
        return response.data;
    }

    static createCountDownSession = async () => {
        const response = await apiWithoutAuth.post(`${ORDER_SERVICE_PATH}/${STORE_CONTROLLER_RESOURCE_PATH}/${COUNTDOWN_SESSION_PATH}`);
        return response.data;
    }
}