import { EventRequestDto, EventResponseDto } from "../../models/generated/event-service-models";
import { apiClient, EVENT_SERVICE_PATH } from "../ApiClient.ts";
import { BaseService } from "../BaseService.ts";

const EVENTS_PATH = "events";
const SYNC_EVENT_PATH = "syncevent";

const getAllEvents = async (): Promise<EventResponseDto[]> => {
    const response = await apiClient.get(`${EVENT_SERVICE_PATH}/${EVENTS_PATH}`);
    return response.data;
}

const getEventById = async (id: string): Promise<EventResponseDto> => {
    const response = await apiClient.get(`${EVENT_SERVICE_PATH}/${EVENTS_PATH}/${id}`);
    return response.data;
}

const createEvent = async (body: EventRequestDto): Promise<EventResponseDto> => {
    const response = await apiClient.post(`${EVENT_SERVICE_PATH}/${EVENTS_PATH}`, body);
    return response.data;
}

const deleteEvent = async (id: string): Promise<void> => {
    await apiClient.delete(`${EVENT_SERVICE_PATH}/${EVENTS_PATH}/${id}`);
}

const updateEvent = async (id: string, body: EventRequestDto): Promise<EventResponseDto> => {
    const response = await apiClient.put(`${EVENT_SERVICE_PATH}/${EVENTS_PATH}/${id}`, body);
    return response.data;
}

const syncEvents = async (): Promise<void> => {
    await apiClient.get(`/${EVENT_SERVICE_PATH}/${EVENTS_PATH}/${SYNC_EVENT_PATH}`);
}

export const EventService = {
    getAllEvents,
    getEventById,
    syncEvents,
    createEvent,
    deleteEvent,
    updateEvent
}

export const EventServiceAdapter: BaseService<EventRequestDto, EventResponseDto> = {
    getAll: () => EventService.getAllEvents(),
    create: (request) => EventService.createEvent(request),
    update: (request, id) => EventService.updateEvent(id, request),
    delete: (id) => EventService.deleteEvent(id),
    getById: (id) => EventService.getEventById(id),
}