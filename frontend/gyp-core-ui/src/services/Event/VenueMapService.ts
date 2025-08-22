import { VenueMapRequestDto, VenueMapResponseDto } from "../../models/generated/event-service-models";
import { apiClient, EVENT_SERVICE_PATH } from "../ApiClient.ts";
import { BaseService } from "../BaseService.ts";

const VENUE_MAP_PATH = "venue-maps";

export class VenueMapService {
    static getAllVenueMaps = async (): Promise<VenueMapResponseDto[]> => {
        const res = await apiClient.get(`/${EVENT_SERVICE_PATH}/${VENUE_MAP_PATH}`);
        return res.data;
    }

    static getVenueMapById = async (id: string): Promise<VenueMapResponseDto> => {
        const res = await apiClient.get(`/${EVENT_SERVICE_PATH}/${VENUE_MAP_PATH}/${id}`);
        return res.data;
    }

    static createVenueMap = async (body: VenueMapRequestDto): Promise<VenueMapResponseDto> => {
        const res = await apiClient.post(`/${EVENT_SERVICE_PATH}/${VENUE_MAP_PATH}`, body);
        return res.data;
    }

    static updateVenueMap = async (body: VenueMapRequestDto, id: string): Promise<VenueMapResponseDto> => {
        const res = await apiClient.put(`/${EVENT_SERVICE_PATH}/${VENUE_MAP_PATH}/${id}`, body);
        return res.data;
    }

    static deleteVenueMap = async (id: string): Promise<void> => {
        await apiClient.delete(`/${EVENT_SERVICE_PATH}/${VENUE_MAP_PATH}/${id}`);
    }
}

export const VenueMapServiceAdapter: BaseService<VenueMapRequestDto, VenueMapResponseDto> = {
    create: (request) => VenueMapService.createVenueMap(request),
    update: (request, id) => VenueMapService.updateVenueMap(request, id),
    delete: (id) => VenueMapService.deleteVenueMap(id),
    getAll: () => VenueMapService.getAllVenueMaps(),
    getById: (id) => VenueMapService.getVenueMapById(id),
}