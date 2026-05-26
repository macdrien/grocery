package fr.sidranie.grocery.product;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.sidranie.grocery.Response;
import fr.sidranie.grocery.data.slug.Slug;

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
    public ResponseEntity<Response<List<Product>>> getAll() {
        return ResponseEntity.ok(new Response<>(products.findAll()));
    }

    @GetMapping("/{slug}")
    public ResponseEntity<Response<Product>> getBySlug(@PathVariable("slug") Slug slug) {
        Product product = products.findBySlug(slug);

        return product == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(new Response<>(product));
    }

    @PostMapping
    public ResponseEntity<Response<Product>> createProduct(@RequestBody Product product) {
        Product saved;
        try {
            saved = productService.save(product);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new Response<>(e.getMessage()));
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(new Response<>(saved));
    }
}
