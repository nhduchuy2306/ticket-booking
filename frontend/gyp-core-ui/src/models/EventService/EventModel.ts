import { ApprovalModel } from "./ApprovalModel.ts";
import { CategoryModel } from "./CategoryMode.ts";
import { OrganizerModel } from "./OrganizerModel.ts";
import { PromotionModel } from "./PromotionModel.ts";
import { TicketTypeModel } from "./TicketTypeModel.ts";
import { VenueModel } from "./VenueModel.ts";

export interface EventModel {
    createUser: any
    changeUser: any
    createTimestamp: any
    changeTimestamp: any
    id: string
    name: string
    description: string
    status: string
    startTime: string
    endTime: string
    doorOpenTime: string
    doorCloseTime: string
    organizer: OrganizerModel
    venue: VenueModel
    categories: CategoryModel[]
    ticketTypes: TicketTypeModel[]
    promotions: PromotionModel[]
    approvals: ApprovalModel[]
    ticketsSold: number
    eventInProgress: boolean
    eventCompleted: boolean
}
