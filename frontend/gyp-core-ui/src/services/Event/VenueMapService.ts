import { VenueMapRequestDto, VenueMapResponseDto } from "../../models/generated/event-service-models";
import { apiClient, EVENT_SERVICE_PATH } from "../ApiClient.ts";

const VENUE_MAP_PATH = "venue-maps";

const getAllVenueMaps = async (): Promise<VenueMapResponseDto[]> => {
    const res = await apiClient.get(`/${EVENT_SERVICE_PATH}/${VENUE_MAP_PATH}`);
    return res.data;
}

const getVenueMapById = async (id: string): Promise<VenueMapResponseDto> => {
    const res = await apiClient.get(`/${EVENT_SERVICE_PATH}/${VENUE_MAP_PATH}/${id}`);
    return res.data;
}

const createVenueMap = async (body: VenueMapRequestDto): Promise<VenueMapResponseDto> => {
    const res = await apiClient.post(`/${EVENT_SERVICE_PATH}/${VENUE_MAP_PATH}`, body);
    return res.data;
}

const updateVenueMap = async (body: VenueMapRequestDto, id: string): Promise<VenueMapResponseDto> => {
    const res = await apiClient.put(`/${EVENT_SERVICE_PATH}/${VENUE_MAP_PATH}/${id}`, body);
    return res.data;
}

const deleteVenueMap = async (id: string): Promise<void> => {
    await apiClient.delete(`/${EVENT_SERVICE_PATH}/${VENUE_MAP_PATH}/${id}`);
}

export const VenueMapService = {
    getAllVenueMaps,
    createVenueMap,
    updateVenueMap,
    deleteVenueMap,
    getVenueMapById,
}