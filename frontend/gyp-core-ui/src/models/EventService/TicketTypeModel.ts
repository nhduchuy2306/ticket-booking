export interface TicketTypeModel {
    createUser: any
    changeUser: any
    createTimestamp: any
    changeTimestamp: any
    id: string
    name: string
    description: string
    price: number
    quantityAvailable: number
    status: string
    saleStartDate: string
    saleEndDate: string
    soldTickets: any
    saleActive: boolean
    soldOut: boolean
}