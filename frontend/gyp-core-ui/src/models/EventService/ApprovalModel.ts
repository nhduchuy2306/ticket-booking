export interface ApprovalModel {
    createUser: any
    changeUser: any
    createTimestamp: any
    changeTimestamp: any
    id: string
    status: string
    approvedBy?: string
    approvalDate?: string
    rejectionReason: any
}