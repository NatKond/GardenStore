package de.telran.gardenStore.service;

import de.telran.gardenStore.entity.Product;

import java.math.BigDecimal;
import java.util.List;


public interface ProductService {

    List<Product> getAllProducts(Long categoryId, Boolean discount, BigDecimal minPrice, BigDecimal maxPrice, String sortBy, Boolean sortDirection);

    Product getProductById(Long productId);

    Product createProduct(Product product);

    Product updateProduct(Long productId, Product product);

    void deleteProductById(Long productId);
}



