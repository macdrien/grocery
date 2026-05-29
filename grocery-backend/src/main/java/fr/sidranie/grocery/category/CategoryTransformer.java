package fr.sidranie.grocery.category;

import fr.sidranie.grocery.category.dto.CategoryDto;

public class CategoryTransformer {
    public static Category categoryFromCategoryDto(CategoryDto categoryDto) {
        Category category = new Category();
        category.setName(categoryDto.getName());
        if (categoryDto.getParentId() != null) {
            category.setParent(new Category(categoryDto.getParentId()));
        }
        return category;
    }
}
