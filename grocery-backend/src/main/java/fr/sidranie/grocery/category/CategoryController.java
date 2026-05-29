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
    public ResponseEntity<Response<List<Category>>> getAll() {
        return ResponseEntity.ok(new Response<>(categories.findAll()));
    }

    @PostMapping
    public ResponseEntity<Response<Category>> createCategory(@RequestBody CategoryDto categoryDto) {
        Category input = CategoryTransformer.categoryFromCategoryDto(categoryDto);
        Category saved;
        try {
            saved = categoryService.save(input);
        } catch (IllegalArgumentException exception) {
            return ResponseEntity.badRequest().body(new Response<>(exception.getMessage()));
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(new Response<>(saved));
    }
}
