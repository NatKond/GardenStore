package de.telran.gardenStore.repository;

import de.telran.gardenStore.entity.Category;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query(value = """
            SELECT c
            FROM Category c
            WHERE categoryId =:categoryId
            """)
    @EntityGraph(attributePaths = {"products"})
    Optional<Category> findByIdWithProducts(Long categoryId);

    Optional<Category> findCategoryByName(String name);
}