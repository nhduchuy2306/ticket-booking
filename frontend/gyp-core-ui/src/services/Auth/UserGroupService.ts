import { UserGroupModel, UserGroupPermissionModel } from "../../models/AuthService/UserGroupModel.ts";
import { apiClient, AUTH_SERVICE_PATH } from "../ApiClient.ts";

const USER_GROUP_PATH = "usergroups";
const USER_GROUP_ACTIONS_PATH = "usergroupactions";

export class UserGroupService {
    static async getAllUserGroups(): Promise<UserGroupModel[]> {
        const res = await apiClient.get(`/${AUTH_SERVICE_PATH}/${USER_GROUP_PATH}`);
        return res.data;
    }

    static async getApplicationPermissions(): Promise<UserGroupPermissionModel[]> {
        const res = await apiClient.get(`/${AUTH_SERVICE_PATH}/${USER_GROUP_PATH}/${USER_GROUP_ACTIONS_PATH}`);
        return res.data;
    }
}