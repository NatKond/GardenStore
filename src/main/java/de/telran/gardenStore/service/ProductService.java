package de.telran.gardenStore.service;

import de.telran.gardenStore.entity.Product;

import java.math.BigDecimal;
import java.util.List;


public interface ProductService {

    List<Product> getAll(Long categoryId, Boolean discount, BigDecimal minPrice, BigDecimal maxPrice, String sortBy, Boolean sortDirection);

    Product getById(Long productId);

    Product create(Product product);

    Product update(Long productId, Product product);

    void deleteById(Long productId);

    Product setDiscount(Long productId, BigDecimal discountPercentage);

    Product getProductOfTheDay();
}



