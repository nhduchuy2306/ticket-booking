import { UserGroupModel } from "./UserGroupModel.ts";

export interface UserAccountModel {
    id: string
    createUser?: string
    createTimestamp?: string
    changeUser?: string
    changeTimestamp?: string
    name: string
    username: string
    dob?: string
    phoneNumber?: string
    email?: string
    userGroupList: UserGroupModel[]
}

export interface CreateAccountModel {
    name: string
    username: string
    dob?: string
    phoneNumber?: string
    email?: string
    userGroupList: string[]
}