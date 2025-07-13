import { UserGroupPermissionModel } from "../../models/AuthService/UserGroupModel.ts";
import { UserGroupRequestDto, UserGroupResponseDto } from "../../models/generated/auth-service-models";
import { apiClient, AUTH_SERVICE_PATH } from "../ApiClient.ts";
import { BaseService } from "../BaseService.ts";

const USER_GROUP_PATH = "usergroups";
const USER_GROUP_ACTIONS_PATH = "usergroupactions";

const getAllUserGroups = async (): Promise<UserGroupResponseDto[]> => {
    const res = await apiClient.get(`/${AUTH_SERVICE_PATH}/${USER_GROUP_PATH}`);
    return res.data;
}

const createUserGroup = async (body: UserGroupRequestDto): Promise<UserGroupResponseDto> => {
    const res = await apiClient.post(`/${AUTH_SERVICE_PATH}/${USER_GROUP_PATH}`, body);
    return res.data;
}

const updateUserGroup = async (body: UserGroupRequestDto, id: string): Promise<UserGroupResponseDto> => {
    const res = await apiClient.put(`/${AUTH_SERVICE_PATH}/${USER_GROUP_PATH}/${id}`, body);
    return res.data;
}

const deleteUserGroup = async (id: string): Promise<void> => {
    await apiClient.delete(`/${AUTH_SERVICE_PATH}/${USER_GROUP_PATH}/${id}`);
}

const getApplicationPermissions = async (): Promise<UserGroupPermissionModel[]> => {
    const res = await apiClient.get(`/${AUTH_SERVICE_PATH}/${USER_GROUP_PATH}/${USER_GROUP_ACTIONS_PATH}`);
    return res.data;
}

export const UserGroupService = {
    getApplicationPermissions,
}

export const UserGroupCRUDService: BaseService<UserGroupRequestDto, UserGroupResponseDto> = {
    getAll: getAllUserGroups,
    create: createUserGroup,
    update: updateUserGroup,
    delete: deleteUserGroup
}