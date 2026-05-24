package fr.sidranie.grocery.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Products extends JpaRepository<Product, Long> {
    Product findBySlug(String slug);
}
