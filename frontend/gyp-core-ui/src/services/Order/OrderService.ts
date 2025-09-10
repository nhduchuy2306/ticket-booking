import { OrderRequestDto, OrderResponseDto } from "../../models/generated/order-service-models";
import { apiWithoutAuth, ORDER_SERVICE_PATH } from "../ApiClient.ts";

const ORDERS_PATH = 'orders';
const CREATE_ORDER_ENDPOINT_PATH = "createorder";

export class OrderService {
    static createOrder = async (orderRequestDto: OrderRequestDto): Promise<OrderResponseDto> => {
        const response = await apiWithoutAuth.post(`${ORDER_SERVICE_PATH}/${ORDERS_PATH}/${CREATE_ORDER_ENDPOINT_PATH}`, orderRequestDto);
        return response.data;
    }
}