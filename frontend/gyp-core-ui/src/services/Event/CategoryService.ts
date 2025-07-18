import { CategoryRequestDto, CategoryResponseDto } from "../../models/generated/event-service-models";
import { apiClient, EVENT_SERVICE_PATH } from "../ApiClient.ts";
import { BaseService } from "../BaseService.ts";

const CATEGORY_PATH = "categories";

const getAllCategories = async (): Promise<CategoryResponseDto[]> => {
    const res = await apiClient.get(`/${EVENT_SERVICE_PATH}/${CATEGORY_PATH}`);
    return res.data;
}

const getCategoryById = async (id: string): Promise<CategoryResponseDto> => {
    const res = await apiClient.get(`/${EVENT_SERVICE_PATH}/${CATEGORY_PATH}/${id}`);
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

export const CategoryService = {
    getAllCategories,
    createCategory,
    updateCategory,
    deleteCategory,
    getCategoryById,
}

export const CategoryServiceAdapter: BaseService<CategoryRequestDto, CategoryResponseDto> = {
    getAll: () => CategoryService.getAllCategories(),
    create: (request) => CategoryService.createCategory(request),
    update: (request, id) => CategoryService.updateCategory(request, id),
    delete: (id) => CategoryService.deleteCategory(id),
    getById: (id) => CategoryService.getCategoryById(id),
};