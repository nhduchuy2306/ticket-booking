import { OrderDetailRequestDto } from "../../models/generated/order-service-models";
import { apiWithoutAuth, ORDER_SERVICE_PATH } from "../ApiClient.ts";

const ORDER_DETAIL_CONTROLLER_PATH = "order-details"

export class OrderDetailService {
    static createOrderDetail = async (orderDetailRequestDto: OrderDetailRequestDto) => {
        const response = await apiWithoutAuth.post(`${ORDER_SERVICE_PATH}/${ORDER_DETAIL_CONTROLLER_PATH}`, orderDetailRequestDto);
        return response.data;
    }
}