import { TicketTypeRequestDto, TicketTypeResponseDto } from "../../models/generated/event-service-models";
import { apiClient, EVENT_SERVICE_PATH } from "../ApiClient.ts";
import { BaseService } from "../BaseService.ts";

const TICKET_TYPE_PATH = "ticket-types";

const getAllTicketTypes = async (): Promise<TicketTypeResponseDto[]> => {
    const res = await apiClient.get(`/${EVENT_SERVICE_PATH}/${TICKET_TYPE_PATH}`);
    return res.data;
}

const getTicketTypeById = async (id: string): Promise<TicketTypeResponseDto> => {
    const res = await apiClient.get(`/${EVENT_SERVICE_PATH}/${TICKET_TYPE_PATH}/${id}`);
    return res.data;
}

const createTicketType = async (body: TicketTypeRequestDto): Promise<TicketTypeResponseDto> => {
    const res = await apiClient.post(`/${EVENT_SERVICE_PATH}/${TICKET_TYPE_PATH}`, body);
    return res.data;
}

const updateTicketType = async (body: TicketTypeRequestDto, id: string): Promise<TicketTypeResponseDto> => {
    const res = await apiClient.put(`/${EVENT_SERVICE_PATH}/${TICKET_TYPE_PATH}/${id}`, body);
    return res.data;
}

const deleteTicketType = async (id: string): Promise<void> => {
    await apiClient.delete(`/${EVENT_SERVICE_PATH}/${TICKET_TYPE_PATH}/${id}`);
}

export const TicketTypeService = {
    getAllTicketTypes,
    createTicketType,
    updateTicketType,
    deleteTicketType,
    getTicketTypeById,
}

export const TicketTypeServiceAdapter: BaseService<TicketTypeRequestDto, TicketTypeResponseDto> = {
    getAll: () => TicketTypeService.getAllTicketTypes(),
    create: (request) => TicketTypeService.createTicketType(request),
    update: (request, id) => TicketTypeService.updateTicketType(request, id),
    delete: (id) => TicketTypeService.deleteTicketType(id),
    getById: (id) => TicketTypeService.getTicketTypeById(id),
};