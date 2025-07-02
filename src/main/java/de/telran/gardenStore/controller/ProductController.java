package de.telran.gardenStore.controller;

import de.telran.gardenStore.dto.ProductCreateRequestDto;
import de.telran.gardenStore.dto.ProductResponseDto;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface ProductController {

    List<ProductResponseDto> getAllProducts();

    ProductResponseDto getProductById(@PathVariable Long productId);

    @PreAuthorize("hasRole('ADMIN')")
    ProductResponseDto createProduct(@RequestBody @Valid ProductCreateRequestDto productRequest);

    @PreAuthorize("hasRole('ADMIN')")
    ProductResponseDto updateProduct(@PathVariable Long productId,
                                     @RequestBody @Valid ProductCreateRequestDto productRequest);

    @PreAuthorize("hasRole('ADMIN')")
    void deleteProductById(@PathVariable Long productId);
}

