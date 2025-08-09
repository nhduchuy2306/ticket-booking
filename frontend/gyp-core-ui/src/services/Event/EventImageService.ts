import { EventRequestDto, EventResponseDto } from "../../models/generated/event-service-models";
import { apiClient, EVENT_SERVICE_PATH } from "../ApiClient.ts";
import { BaseService } from "../BaseService.ts";

const EVENT_IMAGES_PATH = "event-images";

const getAllEventImages = async (): Promise<EventResponseDto[]> => {
    const response = await apiClient.get(`${EVENT_SERVICE_PATH}/${EVENT_IMAGES_PATH}`);
    return response.data;
}

const getEventImageById = async (id: string): Promise<EventResponseDto> => {
    const response = await apiClient.get(`${EVENT_SERVICE_PATH}/${EVENT_IMAGES_PATH}/${id}`);
    return response.data;
}

const createEventImage = async (body: EventRequestDto): Promise<EventResponseDto> => {
    const response = await apiClient.post(`${EVENT_SERVICE_PATH}/${EVENT_IMAGES_PATH}`, body);
    return response.data;
}

const deleteEventImage = async (id: string): Promise<void> => {
    await apiClient.delete(`${EVENT_SERVICE_PATH}/${EVENT_IMAGES_PATH}/${id}`);
}

const updateEventImage = async (id: string, body: EventRequestDto): Promise<EventResponseDto> => {
    const response = await apiClient.put(`${EVENT_SERVICE_PATH}/${EVENT_IMAGES_PATH}/${id}`, body);
    return response.data;
}

export const EventImageService = {
    getAllEventImages,
    getEventImageById,
    createEventImage,
    deleteEventImage,
    updateEventImage
}

export const EventImageServiceAdapter: BaseService<EventRequestDto, EventResponseDto> = {
    getAll: () => EventImageService.getAllEventImages(),
    create: (request) => EventImageService.createEventImage(request),
    update: (request, id) => EventImageService.updateEventImage(id, request),
    delete: (id) => EventImageService.deleteEventImage(id),
    getById: (id) => EventImageService.getEventImageById(id),
}