import { BaseService } from "../../components/layout/models/LayoutModel.ts";
import { CategoryRequestDto, CategoryResponseDto } from "../../models/generated/event-service-models";
import { CategoriesCRUDService } from "../../services/Event/CategoryService.ts";

export const CategoryServiceAdapter: BaseService<CategoryRequestDto, CategoryResponseDto> = {
    getAll: () => CategoriesCRUDService.getAll(),
    create: (request) => CategoriesCRUDService.create(request),
    update: (request, id) => CategoriesCRUDService.update(request, id),
    delete: (id) => CategoriesCRUDService.delete(id),
};