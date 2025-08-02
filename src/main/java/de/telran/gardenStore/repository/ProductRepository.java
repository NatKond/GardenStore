package de.telran.gardenStore.repository;

import de.telran.gardenStore.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT p FROM Product p WHERE p.discountPrice IS NOT NULL ORDER BY (p.price - p.discountPrice) DESC")
    List<Product> findProductsWithHighestDiscount();
}