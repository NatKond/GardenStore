package de.telran.gardenStore.controller;

import de.telran.gardenStore.dto.ProductCreateRequestDto;
import de.telran.gardenStore.dto.ProductResponseDto;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface ProductController {
    List<ProductResponseDto> getAllProducts();

    ProductResponseDto getProductById(Long id);

    ProductResponseDto createProduct(ProductCreateRequestDto productRequest);

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    ProductResponseDto updateProduct(@PathVariable Long id,
                                     @RequestBody @Valid ProductCreateRequestDto productRequest);

    void deleteProductById(Long id);
}
