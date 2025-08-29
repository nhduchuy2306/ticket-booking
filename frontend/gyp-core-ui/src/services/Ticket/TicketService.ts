import { TicketResponseDto } from "../../models/generated/ticket-service-models";
import { apiClient, TICKET_SERVICE_PATH } from "../ApiClient.ts";

const AVAILABLE_TICKET_PATH = "availabletickets";
const START_SALE_TICKET_PATH = "startsaleticket";

export class TicketService {
    static getAvailableTicketById = async (eventId: string): Promise<void> => {
        await apiClient.get(`/${TICKET_SERVICE_PATH}/${TICKET_SERVICE_PATH}/${AVAILABLE_TICKET_PATH}/${eventId}`);
    }

    static startSaleTicket = async (eventId: string): Promise<void> => {
        await apiClient.get(`/${TICKET_SERVICE_PATH}/${TICKET_SERVICE_PATH}/${START_SALE_TICKET_PATH}/${eventId}`);
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