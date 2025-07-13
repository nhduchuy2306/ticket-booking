import { CategoryRequestDto, CategoryResponseDto } from "../../models/generated/event-service-models";
import { apiClient, EVENT_SERVICE_PATH } from "../ApiClient.ts";
import { BaseService } from "../BaseService.ts";

const CATEGORY_PATH = "categories";

const getAllCategories = async (): Promise<CategoryResponseDto[]> => {
    const res = await apiClient.get(`/${EVENT_SERVICE_PATH}/${CATEGORY_PATH}`);
    return res.data;
}

const createCategory = async (body: CategoryRequestDto): Promise<CategoryResponseDto> => {
    const res = await apiClient.post(`/${EVENT_SERVICE_PATH}/${CATEGORY_PATH}`, body);
    return res.data;
}

const updateCategory = async (body: CategoryRequestDto, id: string): Promise<CategoryResponseDto> => {
    const res = await apiClient.put(`/${EVENT_SERVICE_PATH}/${CATEGORY_PATH}/${id}`, body);
    return res.data;
}

const deleteCategory = async (id: string): Promise<void> => {
    await apiClient.delete(`/${EVENT_SERVICE_PATH}/${CATEGORY_PATH}/${id}`);
}

export const CategoriesService = {
    getAllCategories,
    createCategory,
    updateCategory,
    deleteCategory,
}

export const CategoryServiceAdapter: BaseService<CategoryRequestDto, CategoryResponseDto> = {
    getAll: () => CategoriesService.getAllCategories(),
    create: (request) => CategoriesService.createCategory(request),
    update: (request, id) => CategoriesService.updateCategory(request, id),
    delete: (id) => CategoriesService.deleteCategory(id),
};