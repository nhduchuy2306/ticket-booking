import { apiClient, ORDER_SERVICE_PATH } from "../ApiClient.ts";

const PAYMENT_PATH = 'payments';
const CREATE_PAYMENT_ENDPOINT_PATH = "createpaymentendpoint";

export class OrderService {
    static createPaymentEndpoint = async (amount: number) => {
        const response = await apiClient.get(`${ORDER_SERVICE_PATH}/${PAYMENT_PATH}/${CREATE_PAYMENT_ENDPOINT_PATH}`, {
            params: {amount}
        });
        return response.data;
    }
}