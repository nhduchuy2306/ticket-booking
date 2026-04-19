import { OrganizationRequestDto, OrganizationResponseDto } from "../../models/generated/auth-service-models";
import { apiClient, apiWithoutAuth, AUTH_SERVICE_PATH } from "../ApiClient.ts";
import { BaseService } from "../BaseService.ts";

const ORGANIZATION_PATH = "organizations";

export class OrganizationService {
    static getAllOrganizations = async (): Promise<OrganizationResponseDto[]> => {
        const res = await apiClient.get(`/${AUTH_SERVICE_PATH}/${ORGANIZATION_PATH}`);
        return res.data;
    }

    static getOrganizationById = async (id: string): Promise<OrganizationResponseDto> => {
        const res = await apiClient.get(`/${AUTH_SERVICE_PATH}/${ORGANIZATION_PATH}/${id}`);
        return res.data;
    }

    static createOrganization = async (body: OrganizationRequestDto): Promise<OrganizationResponseDto> => {
        const res = await apiClient.post(`/${AUTH_SERVICE_PATH}/${ORGANIZATION_PATH}`, body);
        return res.data;
    }

    static registerOrganization = async (body: OrganizationRequestDto): Promise<OrganizationResponseDto> => {
        const res = await apiWithoutAuth.post(`/${AUTH_SERVICE_PATH}/${ORGANIZATION_PATH}/register`, body);
        return res.data;
    }

    static updateOrganization = async (body: OrganizationRequestDto, id: string): Promise<OrganizationResponseDto> => {
        const res = await apiClient.put(`/${AUTH_SERVICE_PATH}/${ORGANIZATION_PATH}/${id}`, body);
        return res.data;
    }

    static deleteOrganization = async (id: string): Promise<void> => {
        await apiClient.delete(`/${AUTH_SERVICE_PATH}/${ORGANIZATION_PATH}/${id}`);
    }
}

export const OrganizationServiceAdapter: BaseService<OrganizationRequestDto, OrganizationResponseDto> = {
    getAll: () => OrganizationService.getAllOrganizations(),
    create: (request) => OrganizationService.createOrganization(request),
    update: (request, id) => OrganizationService.updateOrganization(request, id),
    delete: (id) => OrganizationService.deleteOrganization(id),
    getById: (id) => OrganizationService.getOrganizationById(id),
};