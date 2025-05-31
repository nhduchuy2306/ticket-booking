import {
    CreateUserGroupModel,
    UserGroupModel,
    UserGroupPermissionModel
} from "../../models/AuthService/UserGroupModel.ts";
import { apiClient, AUTH_SERVICE_PATH } from "../ApiClient.ts";

const USER_GROUP_PATH = "usergroups";
const USER_GROUP_ACTIONS_PATH = "usergroupactions";

const getAllUserGroups = async (): Promise<UserGroupModel[]> => {
    const res = await apiClient.get(`/${AUTH_SERVICE_PATH}/${USER_GROUP_PATH}`);
    return res.data;
}

const createUserGroup = async (body: CreateUserGroupModel): Promise<UserGroupModel> => {
    const res = await apiClient.post(`/${AUTH_SERVICE_PATH}/${USER_GROUP_PATH}`, body);
    return res.data;
}

const updateUserGroup = async (body: CreateUserGroupModel, id: string): Promise<UserGroupModel> => {
    const res = await apiClient.put(`/${AUTH_SERVICE_PATH}/${USER_GROUP_PATH}/${id}`, body);
    return res.data;
}

const deleteUserGroup = async (id: string): Promise<UserGroupModel> => {
    const res = await apiClient.delete(`/${AUTH_SERVICE_PATH}/${USER_GROUP_PATH}/${id}`);
    return res.data;
}

const getApplicationPermissions = async (): Promise<UserGroupPermissionModel[]> => {
    const res = await apiClient.get(`/${AUTH_SERVICE_PATH}/${USER_GROUP_PATH}/${USER_GROUP_ACTIONS_PATH}`);
    return res.data;
}

export const UserGroupService = {
    getAllUserGroups,
    getApplicationPermissions,
    createUserGroup,
    updateUserGroup,
    deleteUserGroup
}