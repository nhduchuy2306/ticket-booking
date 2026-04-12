import {
    EventSectionMappingListRequestDto,
    EventSectionMappingResponseDto
} from "../../models/generated/event-service-models";
import { apiClient, EVENT_SERVICE_PATH } from "../ApiClient.ts";

const EVENT_SECTION_MAPPINGS_RESOURCE_PATH = "event-section-mappings"

export class EventSectionMappingService {
    static getAllEventSectionMappingsByEventId = async (eventId: string): Promise<EventSectionMappingResponseDto[]> => {
        const response = await apiClient.get(`${EVENT_SERVICE_PATH}/${EVENT_SECTION_MAPPINGS_RESOURCE_PATH}/${eventId}`)
        return response.data;
    }

    static createEventSectionMappings = async (body: EventSectionMappingListRequestDto): Promise<EventSectionMappingResponseDto[]> => {
        const response = await apiClient.post(`${EVENT_SERVICE_PATH}/${EVENT_SECTION_MAPPINGS_RESOURCE_PATH}`, body);
        return response.data;
    }

    static updateEventSectionMappings = async (body: EventSectionMappingListRequestDto): Promise<EventSectionMappingResponseDto[]> => {
        const response = await apiClient.put(`${EVENT_SERVICE_PATH}/${EVENT_SECTION_MAPPINGS_RESOURCE_PATH}`, body);
        return response.data;
    }
}