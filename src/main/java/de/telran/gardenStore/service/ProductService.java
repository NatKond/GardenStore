package de.telran.gardenStore.service;

import de.telran.gardenStore.entity.Product;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;


public interface ProductService {
    Page<Product> getAllProducts(Long category, Boolean discount,
                                 BigDecimal minPrice, BigDecimal maxPrice,
                                 String[] sort, int page, int size);
    Product getProductById(Long productId);

    Product createProduct(Product product);

    Product updateProduct(Long productId, Product product);

    void deleteProductById(Long productId);
}



