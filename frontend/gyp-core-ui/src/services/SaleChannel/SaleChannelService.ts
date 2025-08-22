import { SaleChannelRequestDto, SaleChannelResponseDto } from "../../models/generated/sale-channel-service-models";
import { apiClient, SALE_CHANNEL_SERVICE_PATH } from "../ApiClient.ts";
import { BaseService } from "../BaseService.ts";

const SALE_CHANNEL_PATH = "sale-channels";

export class SaleChannelService {
    static getAllSaleChannels = async (): Promise<SaleChannelResponseDto[]> => {
        const res = await apiClient.get(`/${SALE_CHANNEL_SERVICE_PATH}/${SALE_CHANNEL_PATH}`);
        return res.data;
    }

    static getSaleChannelById = async (id: string): Promise<SaleChannelResponseDto> => {
        const res = await apiClient.get(`/${SALE_CHANNEL_SERVICE_PATH}/${SALE_CHANNEL_PATH}/${id}`);
        return res.data;
    }

    static createSaleChannel = async (body: SaleChannelRequestDto): Promise<SaleChannelResponseDto> => {
        const res = await apiClient.post(`/${SALE_CHANNEL_SERVICE_PATH}/${SALE_CHANNEL_PATH}`, body);
        return res.data;
    }

    static updateSaleChannel = async (body: SaleChannelRequestDto, id: string): Promise<SaleChannelResponseDto> => {
        const res = await apiClient.put(`/${SALE_CHANNEL_SERVICE_PATH}/${SALE_CHANNEL_PATH}/${id}`, body);
        return res.data;
    }

    static deleteSaleChannel = async (id: string): Promise<void> => {
        await apiClient.delete(`/${SALE_CHANNEL_SERVICE_PATH}/${SALE_CHANNEL_PATH}/${id}`);
    }
}

export const SaleChannelServiceAdapter: BaseService<SaleChannelRequestDto, SaleChannelResponseDto> = {
    create: (request) => SaleChannelService.createSaleChannel(request),
    update: (request, id) => SaleChannelService.updateSaleChannel(request, id),
    delete: (id) => SaleChannelService.deleteSaleChannel(id),
    getAll: () => SaleChannelService.getAllSaleChannels(),
    getById: (id) => SaleChannelService.getSaleChannelById(id),
}