import { apiClient, ORDER_SERVICE_PATH } from "../ApiClient.ts";

const PAYMENT_PATH = 'payments';
const CREATE_PAYMENT_ENDPOINT_PATH = "createpaymentendpoint";

export class PaymentService {
    static createPaymentEndpoint = async (amount: number, orderId: string) => {
        const response = await apiClient.get(`${ORDER_SERVICE_PATH}/${PAYMENT_PATH}/${CREATE_PAYMENT_ENDPOINT_PATH}`, {
            params: {amount, orderId}
        });
        return response.data;
    }
}