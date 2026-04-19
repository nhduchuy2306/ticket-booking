import { NavigateFunction } from "react-router-dom";
import { EventRequestDto, EventResponseDto } from "../../models/generated/event-service-models";
import { apiClient, apiWithoutAuth, EVENT_SERVICE_PATH } from "../ApiClient.ts";
import { BaseService } from "../BaseService.ts";

const EVENTS_PATH = "events";
const SYNC_EVENT_PATH = "syncevent";
const ACTIVE_PATH = "active";
const WITH_UPLOAD_PATH = "with-upload";
const ON_SALE_PATH = "on-sale";
const COMING_EVENTS_PATH = "/coming-events";
const PUBLISH_EVENTS_PATH = "publish";

export class EventService {
    static getAllEvents = async (): Promise<EventResponseDto[]> => {
        const response = await apiClient.get(`${EVENT_SERVICE_PATH}/${EVENTS_PATH}`);
        return response.data;
    }

    static getEventById = async (id: string): Promise<EventResponseDto> => {
        const response = await apiClient.get(`${EVENT_SERVICE_PATH}/${EVENTS_PATH}/${id}`);
        return response.data;
    }

    static getActiveEvents = async (): Promise<EventResponseDto[]> => {
        const response = await apiClient.get(`${EVENT_SERVICE_PATH}/${EVENTS_PATH}/${ACTIVE_PATH}`);
        return response.data;
    }

    static createEvent = async (body: EventRequestDto): Promise<EventResponseDto> => {
        const response = await apiClient.post(`${EVENT_SERVICE_PATH}/${EVENTS_PATH}`, body);
        return response.data;
    }

    static deleteEvent = async (id: string): Promise<void> => {
        await apiClient.delete(`${EVENT_SERVICE_PATH}/${EVENTS_PATH}/${id}`);
    }

    static updateEvent = async (id: string, body: EventRequestDto): Promise<EventResponseDto> => {
        const response = await apiClient.put(`${EVENT_SERVICE_PATH}/${EVENTS_PATH}/${id}`, body);
        return response.data;
    }

    static syncEvents = async (): Promise<void> => {
        await apiClient.get(`/${EVENT_SERVICE_PATH}/${EVENTS_PATH}/${SYNC_EVENT_PATH}`);
    }

    static createEventWithUpload = async (
            body: EventRequestDto, logoFile: File | null): Promise<EventResponseDto> => {
        const formData = this.createFormData(body, logoFile);
        const response = await apiClient.post(`${EVENT_SERVICE_PATH}/${EVENTS_PATH}/${WITH_UPLOAD_PATH}`, formData, {
            headers: {
                'Content-Type': 'multipart/form-data'
            }
        });
        return response.data;
    }

    static updateEventWithUpload = async (
            id: string, body: EventRequestDto, logoFile: File | null
    ): Promise<EventResponseDto> => {
        const formData = this.createFormData(body, logoFile);
        const response = await apiClient.put(`${EVENT_SERVICE_PATH}/${EVENTS_PATH}/${WITH_UPLOAD_PATH}/${id}`, formData, {
            headers: {
                'Content-Type': 'multipart/form-data'
            }
        });
        return response.data;
    }

    static publishEvent = async (id: string): Promise<EventResponseDto> => {
        const response = await apiClient.post(`${EVENT_SERVICE_PATH}/${EVENTS_PATH}/${id}/${PUBLISH_EVENTS_PATH}`);
        return response.data;
    }

    static navigate = (navigator: NavigateFunction, path: string, entity?: EventResponseDto) => {
        if (path === '/create') {
            navigator('/event/create');
        } else if (path === '/edit') {
            navigator(`/event/edit/${entity?.id}`);
        } else if (path === '/view') {
            navigator(`/event/view/${entity?.id}`);
        } else if (path === '/assign') {
            navigator(`/event/assign/${entity?.id}`);
        } else {
            navigator('/event');
        }
    }

    static getOnSaleEvents = async (): Promise<EventResponseDto[]> => {
        const response = await apiWithoutAuth.get(`${EVENT_SERVICE_PATH}/${EVENTS_PATH}/${ON_SALE_PATH}`);
        return response.data;
    }

    static getComingEvents = async (): Promise<EventResponseDto[]> => {
        const response = await apiWithoutAuth.get(`${EVENT_SERVICE_PATH}/${EVENTS_PATH}${COMING_EVENTS_PATH}`);
        return response.data;
    }

    private static createFormData = (body: EventRequestDto, logoFile: File | null) => {
        const formData = new FormData();
        if (logoFile) {
            formData.append('logo', logoFile);
        }
        formData.append('event', new Blob([JSON.stringify(body)], {type: "application/json"}));
        return formData;
    }
}

export const EventServiceAdapter: BaseService<EventRequestDto, EventResponseDto> = {
    getAll: () => EventService.getAllEvents(),
    create: (request) => EventService.createEvent(request),
    update: (request, id) => EventService.updateEvent(id, request),
    delete: (id) => EventService.deleteEvent(id),
    getById: (id) => EventService.getEventById(id),
}