import { NavigateFunction } from "react-router-dom";
import {
    SeatConfig,
    SeatMapRequestDto,
    SeatMapResponseDto,
    StageConfig
} from "../../models/generated/event-service-models";
import { apiClient, apiWithoutAuth, EVENT_SERVICE_PATH } from "../ApiClient.ts";
import { BaseService } from "../BaseService.ts";

const SEAT_MAP_PATH = "seat-maps";
const SEAT_MAPS_UPLOAD_PATH = `/${SEAT_MAP_PATH}/upload`;
const GENERATE_SEAT_MAP_TICKET_PATH = "/generateseatmapticket";

export class SeatMapService {
    static getAllSeatMaps = async (): Promise<SeatMapResponseDto[]> => {
        const response = await apiClient.get(`${EVENT_SERVICE_PATH}/${SEAT_MAP_PATH}`);
        return response.data;
    }

    static getSeatMapById = async (id: string): Promise<SeatMapResponseDto> => {
        const response = await apiWithoutAuth.get(`${EVENT_SERVICE_PATH}/${SEAT_MAP_PATH}/${id}`);
        return response.data;
    }

    static createSeatMap = async (body: SeatMapRequestDto): Promise<SeatMapResponseDto> => {
        const response = await apiClient.post(`${EVENT_SERVICE_PATH}/${SEAT_MAP_PATH}`, body);
        return response.data;
    }

    static deleteSeatMap = async (id: string): Promise<void> => {
        await apiClient.delete(`${EVENT_SERVICE_PATH}/${SEAT_MAP_PATH}/${id}`);
    }

    static updateSeatMap = async (id: string, body: SeatMapRequestDto): Promise<SeatMapResponseDto> => {
        const response = await apiClient.put(`${EVENT_SERVICE_PATH}/${SEAT_MAP_PATH}/${id}`, body);
        return response.data;
    }

    static uploadSeatMap = async (file: FormData): Promise<{ seatConfig: SeatConfig, stageConfig: StageConfig }> => {
        const response = await apiClient.post(`${EVENT_SERVICE_PATH}${SEAT_MAPS_UPLOAD_PATH}`, file, {
            headers: {
                'Content-Type': 'multipart/form-data'
            }
        });
        return response.data;
    }

    static generateSeatMapTicket = async (eventId: string): Promise<void> => {
        const response = await apiClient.get(`${EVENT_SERVICE_PATH}/${SEAT_MAP_PATH}${GENERATE_SEAT_MAP_TICKET_PATH}/${eventId}`);
        return response.data;
    }

    static navigate = (navigator: NavigateFunction, path: string, entity?: SeatMapResponseDto) => {
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
}

export const SeatMapServiceAdapter: BaseService<SeatMapRequestDto, SeatMapResponseDto> = {
    getAll: () => SeatMapService.getAllSeatMaps(),
    create: (request) => SeatMapService.createSeatMap(request),
    update: (request, id) => SeatMapService.updateSeatMap(id, request),
    delete: (id) => SeatMapService.deleteSeatMap(id),
    getById: (id) => SeatMapService.getSeatMapById(id),
}