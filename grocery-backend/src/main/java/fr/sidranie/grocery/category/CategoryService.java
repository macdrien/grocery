package fr.sidranie.grocery.category;

import org.springframework.stereotype.Service;

@Service
public class CategoryService {
    public static final String MESSAGE_NAME_ALREADY_EXISTS = "A category with the name %s already exists. Please choose another one.";
    private static final String MESSAGE_PARENT_NOT_EXISTING = "The asked parent category with the id %s does not exist.";

    private final Categories categories;

    public CategoryService(Categories categories) {
        this.categories = categories;
    }

    public Category save(Category category) {
        if (categories.existsByName(category.getName())) {
            throw new IllegalArgumentException(String.format(MESSAGE_NAME_ALREADY_EXISTS, category.getName()));
        }

        category.setId(null);

        if (category.getParent() != null &&
                categories.existsById(category.getParent().getId())) {
            throw new IllegalArgumentException(String.format(MESSAGE_PARENT_NOT_EXISTING,
                                                             category.getParent().getId()));
        }

        return categories.save(category);
    }
}
