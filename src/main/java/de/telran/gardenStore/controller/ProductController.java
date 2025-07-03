package de.telran.gardenStore.controller;

import de.telran.gardenStore.dto.ProductCreateRequestDto;
import de.telran.gardenStore.dto.ProductResponseDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/v1/products")
public interface ProductController {

    @GetMapping
    List<ProductResponseDto> getAllProducts();

    @GetMapping("/{productId}")
    ProductResponseDto getProductById(@PathVariable @Positive Long productId);

    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    ProductResponseDto createProduct(@RequestBody @Valid ProductCreateRequestDto productRequest);

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{productId}")
    ProductResponseDto updateProduct(@PathVariable @Positive Long productId,
                                     @RequestBody @Valid ProductCreateRequestDto productRequest);

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{productId}")
    void deleteProductById(@PathVariable @Positive Long productId);
}

