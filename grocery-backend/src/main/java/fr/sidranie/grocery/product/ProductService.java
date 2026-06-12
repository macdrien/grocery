package fr.sidranie.grocery.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.sidranie.grocery.data.identifier.Identifier;
import fr.sidranie.grocery.data.price.Price;
import fr.sidranie.grocery.data.slug.Slug;
import fr.sidranie.grocery.exception.NotFoundException;

@Service
public class ProductService {

    public static final String MESSAGE_SLUG_ALREADY_EXISTS = "The generated slug (%s) already exists. Please choose another name to generate another slug.";
    public static final String MESSAGE_NO_PRODUCT_WITH_ID_FOUND = "No product with the id %s found.";
    public static final String MESSAGE_NAME_ALREADY_EXISTS = "A product with the name %s already exists. Please choose another one.";
    public static final String MESSAGE_INVALID_PRICE = "Invalid price. Price must be 0 or more.";

    private final Products products;

    @Autowired
    public ProductService(Products products) {
        this.products = products;
    }

    public Product save(Product product) throws IllegalArgumentException {
        if (products.existsByName(product.getName())) {
            throw new IllegalArgumentException(MESSAGE_NAME_ALREADY_EXISTS.formatted(product.getName().getValue()));
        }

        updateProductSlugFromName(product, product.getName());
        product.setId(null);

        if (!product.getPrice().isValid()) {
            throw new IllegalArgumentException(MESSAGE_INVALID_PRICE);
        }

        return products.save(product);
    }

    public Product updateProduct(Long id, Product updates) throws NotFoundException {
        Product product = products.findById(id)
                .orElseThrow(() -> new NotFoundException(MESSAGE_NO_PRODUCT_WITH_ID_FOUND.formatted(id)));

        if (updates.getName() != null) {
            if (products.existsByName(product.getName())) {
                throw new IllegalArgumentException(MESSAGE_NAME_ALREADY_EXISTS.formatted(product.getName()));
            }
            product.setName(updates.getName());
            updateProductSlugFromName(product, updates.getName());
        }

        Price price = updates.getPrice();
        if (price != null) {
            if (!price.isValid()) {
                throw new IllegalArgumentException(MESSAGE_INVALID_PRICE);
            }
            product.setPrice(updates.getPrice());
        }

        return products.save(product);
    }

    private void updateProductSlugFromName(Product product, Identifier name) {
        Slug slug = Slug.fromString(name.getValue());
        if (products.existsBySlug(slug)) {
            throw new IllegalArgumentException(MESSAGE_SLUG_ALREADY_EXISTS.formatted(slug.getValue()));
        }
        product.setSlug(slug);
    }
}