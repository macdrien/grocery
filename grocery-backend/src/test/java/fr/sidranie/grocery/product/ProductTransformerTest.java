package fr.sidranie.grocery.product;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import fr.sidranie.grocery.data.identifier.Identifier;
import fr.sidranie.grocery.data.price.Price;
import fr.sidranie.grocery.product.dto.ProductDto;

class ProductTransformerTest {

    @Test
    void testProductFromProductDto() {
        Identifier inputIdentifier = new Identifier("name");
        Price inputPrice = new Price(1f);
        ProductDto input = new ProductDto(inputIdentifier, inputPrice);

        Product expected = new Product();
        expected.setName(inputIdentifier);
        expected.setPrice(inputPrice);

        Product result = ProductTransformer.productFromProductDto(input);

        assertEquals(expected, result);
    }
}