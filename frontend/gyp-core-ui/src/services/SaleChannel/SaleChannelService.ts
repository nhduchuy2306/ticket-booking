import { SaleChannelRequestDto, SaleChannelResponseDto } from "../../models/generated/sale-channel-service-models";
import { apiClient, apiWithoutAuth, SALE_CHANNEL_SERVICE_PATH } from "../ApiClient.ts";
import { BaseService } from "../BaseService.ts";

const SALE_CHANNEL_PATH = "sale-channels";
const EVENT_PATH = "events";
const ACTIVE_PATH = "active";
const BY_SLUG_PATH = "by-slug";

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

    static getSaleChannelsByEventId = async (eventId: string): Promise<SaleChannelResponseDto[]> => {
        const res = await apiClient.get(`/${SALE_CHANNEL_SERVICE_PATH}/${SALE_CHANNEL_PATH}/${EVENT_PATH}/${eventId}`);
        return res.data;
    }

    static getActiveSaleChannels = async (): Promise<SaleChannelResponseDto[]> => {
        const res = await apiWithoutAuth.get(`/${SALE_CHANNEL_SERVICE_PATH}/${SALE_CHANNEL_PATH}/${ACTIVE_PATH}`);
        return res.data;
    }

    static getSaleChannelBySlug = async (orgSlug: string): Promise<SaleChannelResponseDto> => {
        const res = await apiWithoutAuth.get(`/${SALE_CHANNEL_SERVICE_PATH}/${SALE_CHANNEL_PATH}/${BY_SLUG_PATH}/${orgSlug}`);
        return res.data;
    }
}

export const SaleChannelServiceAdapter: BaseService<SaleChannelRequestDto, SaleChannelResponseDto> = {
    create: (request) => SaleChannelService.createSaleChannel(request),
    update: (request, id) => SaleChannelService.updateSaleChannel(request, id),
    delete: (id) => SaleChannelService.deleteSaleChannel(id),
    getAll: () => SaleChannelService.getAllSaleChannels(),
    getById: (id) => SaleChannelService.getSaleChannelById(id),
}