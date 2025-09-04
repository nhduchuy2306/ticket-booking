import { apiClient, BASE_URL, ORDER_SERVICE_PATH } from "../ApiClient.ts";

const COUNTDOWN_PATH = '/sse/countdown';
const PAYMENT_PATH = 'payments';
const CREATE_PAYMENT_ENDPOINT_PATH = "createpaymentendpoint";

export class OrderService {
    static getCountDownTime() {
        return new EventSource(`${BASE_URL}/${ORDER_SERVICE_PATH}/${ORDER_SERVICE_PATH}${COUNTDOWN_PATH}`);
    }

    static createPaymentEndpoint = async (amount: number) => {
        const response = await apiClient.get(`${ORDER_SERVICE_PATH}/${PAYMENT_PATH}/${CREATE_PAYMENT_ENDPOINT_PATH}`, {
            params: {amount}
        });
        return response.data;
    }
}