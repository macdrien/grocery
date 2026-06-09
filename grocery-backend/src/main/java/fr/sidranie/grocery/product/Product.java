package fr.sidranie.grocery.product;

import java.util.Objects;

import fr.sidranie.grocery.data.identifier.Identifier;
import fr.sidranie.grocery.data.identifier.IdentifierConverter;
import fr.sidranie.grocery.data.price.Price;
import fr.sidranie.grocery.data.price.PriceConverter;
import fr.sidranie.grocery.data.slug.Slug;
import fr.sidranie.grocery.data.slug.SlugConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    @Convert(converter = IdentifierConverter.class)
    private Identifier name;

    @Column(nullable = false, unique = true)
    @Convert(converter = SlugConverter.class)
    private Slug slug;

    @Column(nullable = false)
    @Convert(converter = PriceConverter.class)
    private Price price;

    public Product() {
    }

    public Product(Long id, Identifier name, Slug slug, Price price) {
        this.id = id;
        this.name = name;
        this.slug = slug;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Identifier getName() {
        return name;
    }

    public void setName(Identifier name) {
        this.name = name;
    }

    public Slug getSlug() {
        return slug;
    }

    public void setSlug(Slug slug) {
        this.slug = slug;
    }

    public Price getPrice() {
        return price;
    }

    public void setPrice(Price price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Product product = (Product) o;
        return Objects.equals(id, product.id) && Objects.equals(name,
                                                                product.name) && Objects.equals(
                slug,
                product.slug) && Objects.equals(price, product.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, slug, price);
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name +
                "', slug='" + slug +
                "', price=" + price +
                "}";
    }
}
