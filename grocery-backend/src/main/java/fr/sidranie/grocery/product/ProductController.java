package fr.sidranie.grocery.product;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final Products products;
    private final ProductService productService;

    public ProductController(Products products, ProductService productService) {
        this.products = products;
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<List<Product>> getAll() {
        return ResponseEntity.ok(products.findAll());
    }

    @GetMapping("/{slug}")
    public ResponseEntity<Product> getBySlug(@PathVariable("slug") String slug) {
        Product product = products.findBySlug(slug);

        return product == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(product);
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(Product product) {
        Product saved = productService.save(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }
}
