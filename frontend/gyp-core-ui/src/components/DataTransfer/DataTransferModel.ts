export interface RoleItemModel {
    key: string;
    title: string;
    description?: string;
}

export const EXAMPLE_ROLES_DATA: RoleItemModel[] = [
    {key: '1', title: 'Admin', description: 'Full access'},
    {key: '2', title: 'Editor', description: 'Edit content'},
    {key: '3', title: 'Viewer', description: 'View only'},
    {key: '4', title: 'Auditor', description: 'Review logs'},
]