package fr.sidranie.grocery.category;

import fr.sidranie.grocery.category.dto.CategoryCreationDto;
import fr.sidranie.grocery.category.dto.CategoryDto;

public class CategoryTransformer {
    private CategoryTransformer() {
        /* This utility class should not be instantiated */
    }

    public static Category categoryFromCategoryCreationDto(CategoryCreationDto categoryCreationDto) {
        Category category = new Category();
        category.setName(categoryCreationDto.getName());
        if (categoryCreationDto.getParentId() != null) {
            category.setParent(new Category(categoryCreationDto.getParentId()));
        }
        return category;
    }

    public static CategoryDto categoryDtoFromCategory(Category category) {
        CategoryDto categoryDto = new CategoryDto(category.getId(), category.getName());
        Category parent = category.getParent();
        if (parent != null) {
            categoryDto.setParentId(parent.getId());
        }
        return categoryDto;
    }
}
