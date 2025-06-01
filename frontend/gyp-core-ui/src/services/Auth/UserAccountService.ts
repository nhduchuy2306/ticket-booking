import { CreateAccountModel, UserAccountModel } from "../../models/AuthService/UserAccountModel.ts";
import { apiClient, AUTH_SERVICE_PATH } from "../ApiClient.ts";

const USER_ACCOUNT_PATH = "useraccounts";
const SYNC_ORGANIZER_PATH = "syncorganizer";

const getAllUserAccounts = async (): Promise<UserAccountModel[]> => {
    const res = await apiClient.get(`/${AUTH_SERVICE_PATH}/${USER_ACCOUNT_PATH}`);
    return res.data;
}

const getUserAccountById = async (id: string): Promise<UserAccountModel> => {
    const res = await apiClient.get(`/${AUTH_SERVICE_PATH}/${USER_ACCOUNT_PATH}/${id}`);
    return res.data;
}

const createUserAccount = async (body: CreateAccountModel): Promise<UserAccountModel> => {
    const res = await apiClient.post(`/${AUTH_SERVICE_PATH}/${USER_ACCOUNT_PATH}`, body);
    return res.data;
}

const updateUserAccount = async (id: string, body: CreateAccountModel): Promise<UserAccountModel> => {
    const res = await apiClient.put(`/${AUTH_SERVICE_PATH}/${USER_ACCOUNT_PATH}/${id}`, body);
    return res.data;
}

const syncOrganizer = async (): Promise<void> => {
    await apiClient.get(`/${AUTH_SERVICE_PATH}/${USER_ACCOUNT_PATH}/${SYNC_ORGANIZER_PATH}`);
}

export const UserAccountService = {
    getAllUserAccounts,
    getUserAccountById,
    createUserAccount,
    updateUserAccount,
    syncOrganizer
}