import { NavigateFunction } from "react-router-dom";
import {
    SeatConfig,
    SeatMapRequestDto,
    SeatMapResponseDto,
    StageConfig
} from "../../models/generated/event-service-models";
import { apiClient, EVENT_SERVICE_PATH } from "../ApiClient.ts";
import { BaseService } from "../BaseService.ts";

const SEAT_MAP_PATH = "seat-maps";
const SEAT_MAPS_UPLOAD_PATH = `/${SEAT_MAP_PATH}/upload`;

const getAllSeatMaps = async (): Promise<SeatMapResponseDto[]> => {
    const response = await apiClient.get(`${EVENT_SERVICE_PATH}/${SEAT_MAP_PATH}`);
    return response.data;
}

const getSeatMapById = async (id: string): Promise<SeatMapResponseDto> => {
    const response = await apiClient.get(`${EVENT_SERVICE_PATH}/${SEAT_MAP_PATH}/${id}`);
    return response.data;
}

const createSeatMap = async (body: SeatMapRequestDto): Promise<SeatMapResponseDto> => {
    const response = await apiClient.post(`${EVENT_SERVICE_PATH}/${SEAT_MAP_PATH}`, body);
    return response.data;
}

const deleteSeatMap = async (id: string): Promise<void> => {
    await apiClient.delete(`${EVENT_SERVICE_PATH}/${SEAT_MAP_PATH}/${id}`);
}

const updateSeatMap = async (id: string, body: SeatMapRequestDto): Promise<SeatMapResponseDto> => {
    const response = await apiClient.put(`${EVENT_SERVICE_PATH}/${SEAT_MAP_PATH}/${id}`, body);
    return response.data;
}

const uploadSeatMap = async (file: FormData): Promise<{ seatConfig: SeatConfig, stageConfig: StageConfig }> => {
    const response = await apiClient.post(`${EVENT_SERVICE_PATH}${SEAT_MAPS_UPLOAD_PATH}`, file, {
        headers: {
            'Content-Type': 'multipart/form-data'
        }
    });
    return response.data;
}

const navigate = (navigator: NavigateFunction, path: string, entity?: SeatMapResponseDto) => {
    if (path === '/create') {
        navigator('/seat-map/create');
    } else if (path === '/edit') {
        navigator(`/seat-map/edit/${entity?.id}`);
    } else if (path === '/view') {
        navigator(`/seat-map/view/${entity?.id}`);
    } else {
        navigator('/seat-map');
    }
}

export const SeatMapService = {
    getAllSeatMaps,
    getSeatMapById,
    createSeatMap,
    deleteSeatMap,
    updateSeatMap,
    uploadSeatMap,
    navigate
}

export const SeatMapServiceAdapter: BaseService<SeatMapRequestDto, SeatMapResponseDto> = {
    getAll: () => SeatMapService.getAllSeatMaps(),
    create: (request) => SeatMapService.createSeatMap(request),
    update: (request, id) => SeatMapService.updateSeatMap(id, request),
    delete: (id) => SeatMapService.deleteSeatMap(id),
    getById: (id) => SeatMapService.getSeatMapById(id),
}