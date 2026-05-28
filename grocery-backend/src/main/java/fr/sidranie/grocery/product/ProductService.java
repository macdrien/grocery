package fr.sidranie.grocery.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.sidranie.grocery.data.identifier.Identifier;
import fr.sidranie.grocery.data.slug.Slug;
import fr.sidranie.grocery.exception.NotFoundException;

@Service
public class ProductService {

    public static final String MESSAGE_SLUG_ALREADY_EXISTS = "The generated slug (%s) already exists. Please choose another name to generate another slug.";
    private final Products products;

    @Autowired
    public ProductService(Products products) {
        this.products = products;
    }

    public Product save(Product product) throws IllegalArgumentException {
        if (products.existsByName(product.getName())) {
            throw new IllegalArgumentException("A product with the name " + product.getName() + " already exists. Please choose another one.");
        }

        updateProductSlugFromName(product, product.getName());
        product.setId(null);

        return products.save(product);
    }

    public Product updateProduct(Slug slug, Product updates) throws NotFoundException {
        Product product = products.findBySlug(slug);
        if (product == null) {
            throw new NotFoundException("No product with the slug " + slug + " found.");
        }

        product.setName(updates.getName());
        updateProductSlugFromName(product, updates.getName());

        return products.save(product);
    }

    private void updateProductSlugFromName(Product product, Identifier name) {
        Slug slug = Slug.fromString(name.getValue());
        if (products.existsBySlug(slug)) {
            throw new IllegalArgumentException(String.format(MESSAGE_SLUG_ALREADY_EXISTS, slug.getValue()));
        }
        product.setSlug(slug);
    }
}