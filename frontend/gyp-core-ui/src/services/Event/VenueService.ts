import { VenueRequestDto, VenueResponseDto } from "../../models/generated/event-service-models";
import { apiClient, EVENT_SERVICE_PATH } from "../ApiClient.ts";
import { BaseService } from "../BaseService.ts";

const VENUE_PATH = "venues";

const getAllVenues = async (): Promise<VenueResponseDto[]> => {
    const res = await apiClient.get(`/${EVENT_SERVICE_PATH}/${VENUE_PATH}`);
    return res.data;
}

const getVenueById = async (id: string): Promise<VenueResponseDto> => {
    const res = await apiClient.get(`/${EVENT_SERVICE_PATH}/${VENUE_PATH}/${id}`);
    return res.data;
}

const createVenue = async (body: VenueRequestDto): Promise<VenueResponseDto> => {
    const res = await apiClient.post(`/${EVENT_SERVICE_PATH}/${VENUE_PATH}`, body);
    return res.data;
}

const updateVenue = async (body: VenueRequestDto, id: string): Promise<VenueResponseDto> => {
    const res = await apiClient.put(`/${EVENT_SERVICE_PATH}/${VENUE_PATH}/${id}`, body);
    return res.data;
}

const deleteVenue = async (id: string): Promise<void> => {
    await apiClient.delete(`/${EVENT_SERVICE_PATH}/${VENUE_PATH}/${id}`);
}

export const VenueService = {
    getAllVenues,
    createVenue,
    updateVenue,
    deleteVenue,
    getVenueById,
}

export const VenueServiceAdapter: BaseService<VenueRequestDto, VenueResponseDto> = {
    getAll: () => VenueService.getAllVenues(),
    create: (request) => VenueService.createVenue(request),
    update: (request, id) => VenueService.updateVenue(request, id),
    delete: (id) => VenueService.deleteVenue(id),
    getById: (id) => VenueService.getVenueById(id),
};