import { UserAccountRequestDto, UserAccountResponseDto } from "../../models/generated/auth-service-models";
import { apiClient, AUTH_SERVICE_PATH } from "../ApiClient.ts";
import { BaseService } from "../BaseService.ts";

const USER_ACCOUNT_PATH = "useraccounts";
const SYNC_ORGANIZER_PATH = "syncorganizer";

export class UserAccountService {
    static getAllUserAccounts = async (): Promise<UserAccountResponseDto[]> => {
        const res = await apiClient.get(`/${AUTH_SERVICE_PATH}/${USER_ACCOUNT_PATH}`);
        return res.data;
    }

    static getUserAccountById = async (id: string): Promise<UserAccountResponseDto> => {
        const res = await apiClient.get(`/${AUTH_SERVICE_PATH}/${USER_ACCOUNT_PATH}/${id}`);
        return res.data;
    }

    static createUserAccount = async (body: UserAccountRequestDto): Promise<UserAccountResponseDto> => {
        const res = await apiClient.post(`/${AUTH_SERVICE_PATH}/${USER_ACCOUNT_PATH}`, body);
        return res.data;
    }

    static updateUserAccount = async (body: UserAccountRequestDto, id: string): Promise<UserAccountResponseDto> => {
        const res = await apiClient.put(`/${AUTH_SERVICE_PATH}/${USER_ACCOUNT_PATH}/${id}`, body);
        return res.data;
    }

    static deleteUserAccount = async (id: string): Promise<void> => {
        await apiClient.delete(`/${AUTH_SERVICE_PATH}/${USER_ACCOUNT_PATH}/${id}`);
    }

    static syncOrganizer = async (): Promise<void> => {
        await apiClient.get(`/${AUTH_SERVICE_PATH}/${USER_ACCOUNT_PATH}/${SYNC_ORGANIZER_PATH}`);
    }
}

export const UserAccountServiceAdapter: BaseService<UserAccountRequestDto, UserAccountResponseDto> = {
    getAll: () => UserAccountService.getAllUserAccounts(),
    create: (request) => UserAccountService.createUserAccount(request),
    update: (request, id) => UserAccountService.updateUserAccount(request, id),
    delete: (id) => UserAccountService.deleteUserAccount(id),
    getById: (id) => UserAccountService.getUserAccountById(id),
};