import React from "react";
import { DoublePageForm } from "../../components/layout/doulepage/DoublePageForm.tsx";
import DoublePageLayout from "../../components/layout/doulepage/DoublePageLayout.tsx";
import { CategoryServiceAdapter } from "../../services/Event/CategoryService.ts";
import CategoryForm from "./CategoryForm.tsx";
import CategoryTable from "./CategoryTable.tsx";

export interface CategoryPageProps {
}

const CategoryPage: React.FC<CategoryPageProps> = () => {
    return (
            <DoublePageLayout>
                <CategoryTable/>
                <DoublePageForm
                        service={CategoryServiceAdapter}
                        successMessages={{
                            create: "Category created successfully",
                            update: "Category updated successfully"
                        }}
                >
                    {({entity, mode, onSave, onCancel}) => (
                            <CategoryForm
                                    entity={entity}
                                    mode={mode}
                                    onSave={onSave}
                                    onCancel={onCancel}
                            />
                    )}
                </DoublePageForm>
            </DoublePageLayout>
    );
}

export default CategoryPage;