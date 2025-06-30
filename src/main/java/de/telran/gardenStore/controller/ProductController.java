package de.telran.gardenStore.controller;

import de.telran.gardenStore.dto.ProductCreateRequestDto;
import de.telran.gardenStore.dto.ProductResponseDto;

import java.util.List;

public interface ProductController {
    List<ProductResponseDto> getAllProducts();

    ProductResponseDto getProductById(Long id);

    ProductResponseDto createProduct(ProductCreateRequestDto productRequest);

    void deleteProductById(Long id);
}
