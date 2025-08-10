import {
    EventImageRequestDto,
    EventImageResponseDto,
    EventRequestDto,
    EventResponseDto
} from "../../models/generated/event-service-models";
import { apiClient, EVENT_SERVICE_PATH } from "../ApiClient.ts";
import { BaseService } from "../BaseService.ts";

const EVENT_IMAGES_PATH = "event-images";

const getAllEventImages = async (): Promise<EventImageResponseDto[]> => {
    const response = await apiClient.get(`${EVENT_SERVICE_PATH}/${EVENT_IMAGES_PATH}`);
    return response.data;
}

const getEventImageById = async (id: string): Promise<EventImageResponseDto> => {
    const response = await apiClient.get(`${EVENT_SERVICE_PATH}/${EVENT_IMAGES_PATH}/${id}`);
    return response.data;
}

const createEventImage = async (body: EventImageRequestDto): Promise<EventImageResponseDto> => {
    const response = await apiClient.post(`${EVENT_SERVICE_PATH}/${EVENT_IMAGES_PATH}`, body);
    return response.data;
}

const deleteEventImage = async (id: string): Promise<void> => {
    await apiClient.delete(`${EVENT_SERVICE_PATH}/${EVENT_IMAGES_PATH}/${id}`);
}

const updateEventImage = async (id: string, body: EventRequestDto): Promise<EventImageResponseDto> => {
    const response = await apiClient.put(`${EVENT_SERVICE_PATH}/${EVENT_IMAGES_PATH}/${id}`, body);
    return response.data;
}

const createEventImageWithUpload = async (
        body: EventImageRequestDto, imageFile: File | null
): Promise<EventImageResponseDto> => {
    const formData = createFormData(body, imageFile);
    const response = await apiClient.post(`${EVENT_SERVICE_PATH}/${EVENT_IMAGES_PATH}`, formData, {
        headers: {
            'Content-Type': 'multipart/form-data'
        }
    });
    return response.data;
}

const updateEventImageWithUpload = async (
        id: string, body: EventImageRequestDto, imageFile: File | null
): Promise<EventImageResponseDto> => {
    const formData = createFormData(body, imageFile);
    const response = await apiClient.put(`${EVENT_SERVICE_PATH}/${EVENT_IMAGES_PATH}/${id}`, formData, {
        headers: {
            'Content-Type': 'multipart/form-data'
        }
    });
    return response.data;
}

const createFormData = (body: EventImageRequestDto, imageFile: File | null) => {
    const formData = new FormData();
    if (imageFile) {
        formData.append('image', imageFile);
    }
    formData.append('data', new Blob([JSON.stringify(body)], {type: "application/json"}));
    return formData;
}

export const EventImageService = {
    getAllEventImages,
    getEventImageById,
    createEventImage,
    deleteEventImage,
    updateEventImage,
    createEventImageWithUpload,
    updateEventImageWithUpload
}

export const EventImageServiceAdapter: BaseService<EventRequestDto, EventResponseDto> = {
    getAll: () => EventImageService.getAllEventImages(),
    create: (request) => EventImageService.createEventImage(request),
    update: (request, id) => EventImageService.updateEventImage(id, request),
    delete: (id) => EventImageService.deleteEventImage(id),
    getById: (id) => EventImageService.getEventImageById(id),
}