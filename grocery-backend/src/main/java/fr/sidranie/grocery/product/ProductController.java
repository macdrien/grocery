package fr.sidranie.grocery.product;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.sidranie.grocery.Response;
import fr.sidranie.grocery.data.slug.Slug;
import fr.sidranie.grocery.exception.NotFoundException;
import fr.sidranie.grocery.product.dto.ProductDto;
import fr.sidranie.grocery.security.RoleAdmin;
import jakarta.transaction.Transactional;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final Products products;
    private final ProductService productService;

    @Autowired
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
    @RoleAdmin
    @Transactional
    public ResponseEntity<Response<Product>> createProduct(@RequestBody ProductDto productDto) {
        Product input = ProductTransformer.productFromProductDto(productDto);
        Product saved;
        try {
            saved = productService.save(input);
        } catch (IllegalArgumentException exception) {
            return ResponseEntity.badRequest().body(new Response<>(exception.getMessage()));
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(new Response<>(saved));
    }

    @PatchMapping("/{slug}")
    @RoleAdmin
    @Transactional
    public ResponseEntity<Response<Product>> updateProduct(@PathVariable("slug") Slug slug,
                                                           @RequestBody ProductDto productDto) {
        Product input = ProductTransformer.productFromProductDto(productDto);
        Product saved;
        try {
            saved = productService.updateProduct(slug, input);
        } catch (IllegalArgumentException exception) {
            return ResponseEntity.badRequest().body(new Response<>(exception.getMessage()));
        } catch (NotFoundException _) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(new Response<>(saved));
    }

    @DeleteMapping("/{slug}")
    @RoleAdmin
    @Transactional
    public ResponseEntity<Void> deleteProduct(@PathVariable("slug") Slug slug) {
        products.deleteBySlug(slug);
        return ResponseEntity.noContent().build();
    }
}
