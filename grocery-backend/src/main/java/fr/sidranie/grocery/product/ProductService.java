package fr.sidranie.grocery.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.sidranie.grocery.data.slug.Slug;

@Service
public class ProductService {

    private final Products products;

    @Autowired
    public ProductService(Products products) {
        this.products = products;
    }

    public Product save(Product product) throws IllegalArgumentException {
        if (products.existsByName(product.getName())) {
            throw new IllegalArgumentException("A product with the name " + product.getName() + " already exists. Please choose another one.");
        }
        Slug slug = Slug.fromString(product.getName().getValue());

        if (products.existsBySlug(slug)) {
            throw new IllegalArgumentException("The generated slug (" + product.getSlug() + ") already exists. Please choose another name to generate another slug.");
        }

        product.setId(null);
        product.setSlug(slug);

        return products.save(product);
    }
}
