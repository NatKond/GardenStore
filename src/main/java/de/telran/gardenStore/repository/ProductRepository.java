package de.telran.gardenStore.repository;

import de.telran.gardenStore.entity.AppUser;
import de.telran.gardenStore.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query(value = """
            SELECT p.*
            FROM products p
            WHERE p.discount_price IS NOT NULL
            AND FLOOR(((p.price - p.discount_price) * 100) / p.price) = (
                SELECT MAX(FLOOR(((p.price - p.discount_price) * 100) / p.price))
                FROM products p
            )
            """, nativeQuery = true)
    List<Product> findProductsWithHighestDiscount();

    @Query("""
                SELECT DISTINCT p
                FROM Product p
                JOIN OrderItem oi ON oi.product = p
                JOIN Order o ON oi.order = o
                WHERE o.user = :user
                AND o.status = 'DELIVERED'
            """)
    List<Product> findAllPurchasedByUser(AppUser user);
}