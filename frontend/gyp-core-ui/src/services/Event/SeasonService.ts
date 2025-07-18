import { SeasonRequestDto, SeasonResponseDto } from "../../models/generated/event-service-models";
import { apiClient, EVENT_SERVICE_PATH } from "../ApiClient.ts";
import { BaseService } from "../BaseService.ts";

const SEASON_PATH = "seasons";

const getAllSeasons = async (): Promise<SeasonResponseDto[]> => {
    const res = await apiClient.get(`/${EVENT_SERVICE_PATH}/${SEASON_PATH}`);
    return res.data;
}

const getSeasonById = async (id: string): Promise<SeasonResponseDto> => {
    const res = await apiClient.get(`/${EVENT_SERVICE_PATH}/${SEASON_PATH}/${id}`);
    return res.data;
}

const createSeason = async (body: SeasonRequestDto): Promise<SeasonResponseDto> => {
    const res = await apiClient.post(`/${EVENT_SERVICE_PATH}/${SEASON_PATH}`, body);
    return res.data;
}

const updateSeason = async (body: SeasonRequestDto, id: string): Promise<SeasonResponseDto> => {
    const res = await apiClient.put(`/${EVENT_SERVICE_PATH}/${SEASON_PATH}/${id}`, body);
    return res.data;
}

const deleteSeason = async (id: string): Promise<void> => {
    await apiClient.delete(`/${EVENT_SERVICE_PATH}/${SEASON_PATH}/${id}`);
}

export const SeasonService = {
    getAllSeasons,
    createSeason,
    updateSeason,
    deleteSeason,
    getSeasonById,
}

export const SeasonServiceAdapter: BaseService<SeasonRequestDto, SeasonResponseDto> = {
    getAll: () => SeasonService.getAllSeasons(),
    create: (request) => SeasonService.createSeason(request),
    update: (request, id) => SeasonService.updateSeason(request, id),
    delete: (id) => SeasonService.deleteSeason(id),
    getById: (id) => SeasonService.getSeasonById(id),
};