package de.telran.gardenStore.repository;

import de.telran.gardenStore.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query(value = """
            SELECT p.*
            FROM products p
            WHERE p.discount_price IS NOT NULL
            AND FLOOR(((p.price - p.discount_price) * 100) / p.price) = (
                SELECT MAX(FLOOR(((p.price - p.discount_price) * 100) / p.price))
                FROM products p
                WHERE p.discount_price IS NOT NULL
            )
            ORDER BY random()
            LIMIT 1;
            """, nativeQuery = true)
    Optional<Product> findProductsWithHighestDiscount();

    @Query(value = """
                    SELECT CASE WHEN EXISTS (
                    SELECT 1
                    FROM products p
                    JOIN order_items oi ON p.product_id = oi.product_id
                    AND p.product_id = :productId)
                    THEN TRUE ELSE FALSE END
            """, nativeQuery = true)
    Boolean isInOrder(Long productId);
}