import { OrganizationRequestDto, OrganizationResponseDto } from "../../models/generated/auth-service-models";
import { apiClient, AUTH_SERVICE_PATH } from "../ApiClient.ts";
import { BaseService } from "../BaseService.ts";

const ORGANIZATION_PATH = "organizations";

const getAllOrganizations = async (): Promise<OrganizationResponseDto[]> => {
    const res = await apiClient.get(`/${AUTH_SERVICE_PATH}/${ORGANIZATION_PATH}`);
    return res.data;
}

const getOrganizationById = async (id: string): Promise<OrganizationResponseDto> => {
    const res = await apiClient.get(`/${AUTH_SERVICE_PATH}/${ORGANIZATION_PATH}/${id}`);
    return res.data;
}

const createOrganization = async (body: OrganizationRequestDto): Promise<OrganizationResponseDto> => {
    const res = await apiClient.post(`/${AUTH_SERVICE_PATH}/${ORGANIZATION_PATH}`, body);
    return res.data;
}

const updateOrganization = async (body: OrganizationRequestDto, id: string): Promise<OrganizationResponseDto> => {
    const res = await apiClient.put(`/${AUTH_SERVICE_PATH}/${ORGANIZATION_PATH}/${id}`, body);
    return res.data;
}

const deleteOrganization = async (id: string): Promise<void> => {
    await apiClient.delete(`/${AUTH_SERVICE_PATH}/${ORGANIZATION_PATH}/${id}`);
}

export const OrganizationService = {
    getAllOrganizations,
    getOrganizationById,
    createOrganization,
    updateOrganization,
    deleteOrganization,
}

export const OrganizationServiceAdapter: BaseService<OrganizationRequestDto, OrganizationResponseDto> = {
    getAll: () => OrganizationService.getAllOrganizations(),
    create: (request) => OrganizationService.createOrganization(request),
    update: (request, id) => OrganizationService.updateOrganization(request, id),
    delete: (id) => OrganizationService.deleteOrganization(id),
};