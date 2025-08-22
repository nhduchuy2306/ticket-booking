import {
    ApplicationPermissionDto,
    UserAccountResponseDto,
    UserGroupRequestDto,
    UserGroupResponseDto
} from "../../models/generated/auth-service-models";
import { apiClient, AUTH_SERVICE_PATH } from "../ApiClient.ts";
import { BaseService } from "../BaseService.ts";

const USER_GROUP_PATH = "usergroups";
const USER_GROUP_ACTIONS_PATH = "usergroupactions";
const USER_ACCOUNT_PATH = "useraccounts";

export class UserGroupService {
    static getAllUserGroups = async (): Promise<UserGroupResponseDto[]> => {
        const res = await apiClient.get(`/${AUTH_SERVICE_PATH}/${USER_GROUP_PATH}`);
        return res.data;
    }

    static getUserGroupById = async (id: string): Promise<UserGroupResponseDto> => {
        const res = await apiClient.get(`/${AUTH_SERVICE_PATH}/${USER_GROUP_PATH}/${id}`);
        return res.data;
    }

    static createUserGroup = async (body: UserGroupRequestDto): Promise<UserGroupResponseDto> => {
        const res = await apiClient.post(`/${AUTH_SERVICE_PATH}/${USER_GROUP_PATH}`, body);
        return res.data;
    }

    static updateUserGroup = async (body: UserGroupRequestDto, id: string): Promise<UserGroupResponseDto> => {
        const res = await apiClient.put(`/${AUTH_SERVICE_PATH}/${USER_GROUP_PATH}/${id}`, body);
        return res.data;
    }

    static deleteUserGroup = async (id: string): Promise<void> => {
        await apiClient.delete(`/${AUTH_SERVICE_PATH}/${USER_GROUP_PATH}/${id}`);
    }

    static getApplicationPermissions = async (): Promise<ApplicationPermissionDto[]> => {
        const res = await apiClient.get(`/${AUTH_SERVICE_PATH}/${USER_GROUP_PATH}/${USER_GROUP_ACTIONS_PATH}`);
        return res.data;
    }

    static getUserAccountByUserGroupId = async (userGroupId: string): Promise<UserAccountResponseDto> => {
        const res = await apiClient.get(`/${AUTH_SERVICE_PATH}/${USER_GROUP_PATH}/${userGroupId}/${USER_ACCOUNT_PATH}`);
        return res.data;
    }
}

export const UserGroupServiceAdapter: BaseService<UserGroupRequestDto, UserGroupResponseDto> = {
    getAll: () => UserGroupService.getAllUserGroups(),
    create: (request) => UserGroupService.createUserGroup(request),
    update: (request, id) => UserGroupService.updateUserGroup(request, id),
    delete: (id) => UserGroupService.deleteUserGroup(id),
    getById: (id: string) => UserGroupService.getUserGroupById(id)
};