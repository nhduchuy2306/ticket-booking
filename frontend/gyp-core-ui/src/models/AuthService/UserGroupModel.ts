export interface UserGroupPermissionModel {
    name: string;
    applicationId: string;
    actionPermissions: string[];
}

export interface UserGroupModel {
    id: string
    createUser?: string
    createTimestamp?: string
    changeUser?: string
    changeTimestamp?: string
    name: string
    description: string
    administrator: boolean
    userGroupPermissions: UserGroupPermissions
}

export interface CreateUserGroupModel {
    name: string
    description: string
    administrator: boolean
    userGroupPermissions: UserGroupPermissions
}

export interface UserGroupPermissions {
    permissionItems: PermissionItem[]
}

export interface PermissionItem {
    actions: string[]
    applicationId: string
}
