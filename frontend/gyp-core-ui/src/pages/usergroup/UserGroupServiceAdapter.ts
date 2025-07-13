import { BaseService } from "../../components/layout/models/LayoutModel.ts";
import { UserGroupRequestDto, UserGroupResponseDto } from "../../models/generated/auth-service-models";
import { UserGroupCRUDService } from "../../services/Auth/UserGroupService.ts";

export const UserGroupServiceAdapter: BaseService<UserGroupRequestDto, UserGroupResponseDto> = {
    getAll: () => UserGroupCRUDService.getAll(),
    create: (request) => UserGroupCRUDService.create(request),
    update: (request, id) => UserGroupCRUDService.update(request, id),
    delete: (id) => UserGroupCRUDService.delete(id),
};