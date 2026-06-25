package fr.sidranie.grocery.category;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.sidranie.grocery.Response;
import fr.sidranie.grocery.category.dto.CategoryCreationDto;
import fr.sidranie.grocery.category.dto.CategoryDto;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;
    private final Categories categories;

    @Autowired
    public CategoryController(CategoryService categoryService, Categories categories) {
        this.categoryService = categoryService;
        this.categories = categories;
    }

    @GetMapping
    public ResponseEntity<Response<List<CategoryDto>>> getAll() {
        List<CategoryDto> categoryList = categories.findAll()
                .stream()
                .map(CategoryTransformer::categoryDtoFromCategory)
                .toList();
        return ResponseEntity.ok(new Response<>(categoryList));
    }

    @PostMapping
    public ResponseEntity<Response<CategoryDto>> createCategory(@RequestBody CategoryCreationDto categoryCreationDto) {
        Category input = CategoryTransformer.categoryFromCategoryCreationDto(categoryCreationDto);
        Category saved;
        try {
            saved = categoryService.save(input);
        } catch (IllegalArgumentException exception) {
            return ResponseEntity.badRequest().body(new Response<>(exception.getMessage()));
        }
        CategoryDto output = CategoryTransformer.categoryDtoFromCategory(saved);
        return ResponseEntity.status(HttpStatus.CREATED).body(new Response<>(output));
    }
}
