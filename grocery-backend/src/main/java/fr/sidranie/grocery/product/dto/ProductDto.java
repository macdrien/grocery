package fr.sidranie.grocery.product.dto;

import fr.sidranie.grocery.data.identifier.Identifier;
import fr.sidranie.grocery.data.price.Price;

public class ProductDto {
    private Identifier name;
    private Price price;

    public ProductDto(Identifier name, Price price) {
        this.name = name;
        this.price = price;
    }

    public Identifier getName() {
        return name;
    }

    public void setName(Identifier name) {
        this.name = name;
    }

    public Price getPrice() {
        return price;
    }

    public void setPrice(Price price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "ProductDto{" +
                "name='" + name +
                "', price=" + price +
                '}';
    }
}
