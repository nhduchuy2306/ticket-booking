export interface PromotionModel {
    createUser: any
    changeUser: any
    createTimestamp: any
    changeTimestamp: any
    id: string
    code: string
    discountAmount: number
    validFrom: string
    validTo: string
    active: boolean
    expired: boolean
}