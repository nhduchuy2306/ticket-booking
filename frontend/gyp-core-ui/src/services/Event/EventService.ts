import { apiClient, EVENT_SERVICE_PATH } from "../ApiClient.ts";

const EVENTS_PATH = "events";

const getAllEvents = async () => {
    const response = await apiClient.get(`${EVENT_SERVICE_PATH}/${EVENTS_PATH}`);
    return response.data;
}

const getEventById = async (id: string) => {
    const response = await apiClient.get(`${EVENT_SERVICE_PATH}/${EVENTS_PATH}/${id}`);
    return response.data;
}

const createEvent = async (values: any) => {
    const response = await apiClient.post(`${EVENT_SERVICE_PATH}/${EVENTS_PATH}`, values);
    return response.data;
}

const deleteEvent = async (id: string) => {
    const response = await apiClient.delete(`${EVENT_SERVICE_PATH}/${EVENTS_PATH}/${id}`);
    return response.data;
}

const updateEvent = async (id: string, values: any) => {
    const response = await apiClient.put(`${EVENT_SERVICE_PATH}/${EVENTS_PATH}/${id}`, values);
    return response.data;
}

const syncEvents = async () => {

}

export const EventService = {
    getAllEvents,
    getEventById,
    syncEvents,
    createEvent,
    deleteEvent,
    updateEvent
}