import { apiClient, EVENT_SERVICE_PATH } from "../ApiClient.ts";

const EVENTS_PATH = "events";

const getAllEvents = async () => {
    const response = await apiClient.get(`${EVENT_SERVICE_PATH}/${EVENTS_PATH}`);
    return response.data;
}

const syncEvents = async () => {

}

export const EventService = {
    getAllEvents,
    syncEvents
}