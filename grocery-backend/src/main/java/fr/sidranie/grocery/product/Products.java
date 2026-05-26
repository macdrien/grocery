package fr.sidranie.grocery.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fr.sidranie.grocery.data.identifier.Identifier;
import fr.sidranie.grocery.data.slug.Slug;

@Repository
public interface Products extends JpaRepository<Product, Long> {
    Product findBySlug(Slug slug);

    boolean existsByName(Identifier name);

    boolean existsBySlug(Slug slug);
}
