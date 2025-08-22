import { TicketResponseDto } from "../../models/generated/ticket-service-models";
import { apiClient, TICKET_SERVICE_PATH } from "../ApiClient.ts";

const TICKET_GENERATION_PATH = "ticket-generations";
const GENERATE_TICKET_PATH = "generate-ticket";

export class TicketService {
    static generateTicket = async (eventId: string): Promise<void> => {
        await apiClient.get(`/${TICKET_SERVICE_PATH}/${TICKET_GENERATION_PATH}/${eventId}/${GENERATE_TICKET_PATH}`);
    }

    static getAllGeneratedTickets = async (eventId: string): Promise<TicketResponseDto[]> => {
        const response = await apiClient.get(`/${TICKET_SERVICE_PATH}/${TICKET_SERVICE_PATH}`, {
            params: {
                eventId: eventId
            }
        });
        return response.data;
    }
}