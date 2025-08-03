package de.telran.gardenStore.repository;

import de.telran.gardenStore.entity.Product;
import de.telran.gardenStore.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT p " +
            "FROM OrderItem oi "+
            "JOIN oi.product p " +
            "JOIN oi.order o " +
            "WHERE o.status = :orderStatus " +
            "GROUP BY oi.product " +
            "ORDER BY COUNT(oi.product) DESC " +
            "LIMIT :limit"
    )
    List<Product> getTopWithStatus(OrderStatus orderStatus, Integer limit);

    @Query("SELECT p " +
            "FROM OrderItem oi "+
            "JOIN oi.product p " +
            "JOIN oi.order o " +
            "WHERE o.status = :orderStatus AND o.updatedAt < :days " +
            "GROUP BY oi.product " +
            "ORDER BY COUNT(oi.product) DESC " +
            "LIMIT :limit"
    )
    List<Product> getAwaitingPaymentForDays(LocalDateTime amountOfDays, Integer limit);
}

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
}
