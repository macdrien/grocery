package fr.sidranie.grocery.product.transformer;

import fr.sidranie.grocery.product.Product;
import fr.sidranie.grocery.product.dto.ProductDto;

public class ProductTransformer {

    public static Product productFromProductDto(ProductDto productDto) {
        Product product = new Product();
        product.setName(productDto.getName());
        product.setPrice(productDto.getPrice());
        return product;
    }
}
