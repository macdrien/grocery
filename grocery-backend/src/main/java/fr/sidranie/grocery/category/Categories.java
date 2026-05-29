package fr.sidranie.grocery.category;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.sidranie.grocery.data.identifier.Identifier;

public interface Categories extends JpaRepository<Category, Long> {
    boolean existsByName(Identifier identifier);
}
