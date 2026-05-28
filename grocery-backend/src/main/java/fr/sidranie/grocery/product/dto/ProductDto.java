package fr.sidranie.grocery.product.dto;

import fr.sidranie.grocery.data.identifier.Identifier;

public class ProductDto {
    private Identifier name;

    public ProductDto(Identifier name) {
        this.name = name;
    }

    public Identifier getName() {
        return name;
    }

    public void setName(Identifier name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "ProductDto{" +
                "name=" + name +
                '}';
    }
}
