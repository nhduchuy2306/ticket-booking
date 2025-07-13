import { UserAccountRequestDto, UserAccountResponseDto } from "../../models/generated/auth-service-models";
import { apiClient, AUTH_SERVICE_PATH } from "../ApiClient.ts";
import { BaseService } from "../BaseService.ts";

const USER_ACCOUNT_PATH = "useraccounts";
const SYNC_ORGANIZER_PATH = "syncorganizer";

const getAllUserAccounts = async (): Promise<UserAccountResponseDto[]> => {
    const res = await apiClient.get(`/${AUTH_SERVICE_PATH}/${USER_ACCOUNT_PATH}`);
    return res.data;
}

const getUserAccountById = async (id: string): Promise<UserAccountResponseDto> => {
    const res = await apiClient.get(`/${AUTH_SERVICE_PATH}/${USER_ACCOUNT_PATH}/${id}`);
    return res.data;
}

const createUserAccount = async (body: UserAccountRequestDto): Promise<UserAccountResponseDto> => {
    const res = await apiClient.post(`/${AUTH_SERVICE_PATH}/${USER_ACCOUNT_PATH}`, body);
    return res.data;
}

const updateUserAccount = async (body: UserAccountRequestDto, id: string): Promise<UserAccountResponseDto> => {
    const res = await apiClient.put(`/${AUTH_SERVICE_PATH}/${USER_ACCOUNT_PATH}/${id}`, body);
    return res.data;
}

const deleteUserAccount = async (id: string): Promise<void> => {
    await apiClient.delete(`/${AUTH_SERVICE_PATH}/${USER_ACCOUNT_PATH}/${id}`);
}

const syncOrganizer = async (): Promise<void> => {
    await apiClient.get(`/${AUTH_SERVICE_PATH}/${USER_ACCOUNT_PATH}/${SYNC_ORGANIZER_PATH}`);
}

export const UserAccountService = {
    getAllUserAccounts,
    getUserAccountById,
    createUserAccount,
    updateUserAccount,
    deleteUserAccount,
    syncOrganizer
}

export const UserAccountServiceAdapter: BaseService<UserAccountRequestDto, UserAccountResponseDto> = {
    getAll: () => UserAccountService.getAllUserAccounts(),
    create: (request) => UserAccountService.createUserAccount(request),
    update: (request, id) => UserAccountService.updateUserAccount(request, id),
    delete: (id) => UserAccountService.deleteUserAccount(id),
};