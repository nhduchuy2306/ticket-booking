import { VenueRequestDto, VenueResponseDto } from "../../models/generated/event-service-models";
import { apiClient, apiWithoutAuth, EVENT_SERVICE_PATH } from "../ApiClient.ts";
import { BaseService } from "../BaseService.ts";

const VENUE_PATH = "venues";

export class VenueService {
    static getAllVenues = async (): Promise<VenueResponseDto[]> => {
        const res = await apiClient.get(`/${EVENT_SERVICE_PATH}/${VENUE_PATH}`);
        return res.data;
    }

    static getVenueById = async (id: string): Promise<VenueResponseDto> => {
        const res = await apiWithoutAuth.get(`/${EVENT_SERVICE_PATH}/${VENUE_PATH}/${id}`);
        return res.data;
    }

    static createVenue = async (body: VenueRequestDto): Promise<VenueResponseDto> => {
        const res = await apiClient.post(`/${EVENT_SERVICE_PATH}/${VENUE_PATH}`, body);
        return res.data;
    }

    static updateVenue = async (body: VenueRequestDto, id: string): Promise<VenueResponseDto> => {
        const res = await apiClient.put(`/${EVENT_SERVICE_PATH}/${VENUE_PATH}/${id}`, body);
        return res.data;
    }

    static deleteVenue = async (id: string): Promise<void> => {
        await apiClient.delete(`/${EVENT_SERVICE_PATH}/${VENUE_PATH}/${id}`);
    }
}

export const VenueServiceAdapter: BaseService<VenueRequestDto, VenueResponseDto> = {
    getAll: () => VenueService.getAllVenues(),
    create: (request) => VenueService.createVenue(request),
    update: (request, id) => VenueService.updateVenue(request, id),
    delete: (id) => VenueService.deleteVenue(id),
    getById: (id) => VenueService.getVenueById(id),
};