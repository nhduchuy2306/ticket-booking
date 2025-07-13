import { BaseService } from "../../components/layout/models/LayoutModel.ts";
import { CategoryRequestDto, CategoryResponseDto } from "../../models/generated/event-service-models";
import { CategoriesService } from "../../services/Event/CategoryService.ts";

export const categoryServiceAdapter: BaseService<CategoryRequestDto, CategoryResponseDto> = {
    getAll: () => CategoriesService.getAll(),
    create: (request) => CategoriesService.create(request),
    update: (request, id) => CategoriesService.update(request, id),
    delete: (id) => CategoriesService.delete(id),
};