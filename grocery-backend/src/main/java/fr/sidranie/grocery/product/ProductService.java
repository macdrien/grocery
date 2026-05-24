package fr.sidranie.grocery.product;

import fr.sidranie.grocery.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    private final Products products;

    @Autowired
    public ProductService(Products products) {
        this.products = products;
    }

    public Product save(Product product) {
        product.setId(null);
        product.setSlug(StringUtils.toSlug(product.getName()));
        Product saved = products.save(product);
        return saved;
    }
}
