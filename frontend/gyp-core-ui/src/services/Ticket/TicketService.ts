import { apiClient, TICKET_SERVICE_PATH } from "../ApiClient.ts";

const TICKET_GENERATION_PATH = "ticket-generations";
const GENERATE_TICKET_PATH = "generate-ticket";

const generateTicket = async (eventId: string): Promise<void> => {
    await apiClient.get(`/${TICKET_SERVICE_PATH}/${TICKET_GENERATION_PATH}/${eventId}/${GENERATE_TICKET_PATH}`);
}

export const TicketService = {
    generateTicket,
}