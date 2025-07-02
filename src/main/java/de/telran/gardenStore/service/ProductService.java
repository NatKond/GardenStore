package de.telran.gardenStore.service;

import de.telran.gardenStore.entity.Product;

import java.util.List;

public interface ProductService {

    List<Product> getAllProducts();

    Product getProductById(Long productId);

    Product createProduct(Product product);

    void deleteProductById(Long productId);
}



