package de.telran.gardenStore.controller;

import de.telran.gardenStore.dto.ProductCreateRequestDto;
import de.telran.gardenStore.dto.ProductResponseDto;
import de.telran.gardenStore.dto.ProductShortResponseDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RequestMapping("/v1/products")
public interface ProductController {

    @GetMapping
    List<ProductShortResponseDto> getAll(@RequestParam(required = false) @Positive Long category,
                                         @RequestParam(required = false) Boolean discount,
                                         @RequestParam(required = false) @Positive BigDecimal minPrice,
                                         @RequestParam(required = false) @Positive BigDecimal maxPrice,
                                         @RequestParam(required = false)
                                                 @Pattern(regexp = "productId|name|price|category|discountPrice|createdAt|updatedAt") String sortBy,
                                         @RequestParam(required = false) Boolean sortDirection);

    @GetMapping("/{productId}")
    ProductResponseDto getById(@PathVariable @Positive Long productId);

    @GetMapping("/purchased-products")
    @PreAuthorize("hasRole('USER')")
    List<ProductShortResponseDto> getAllPurchasedProducts();

    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    ProductResponseDto create(@RequestBody @Valid ProductCreateRequestDto productRequest);

    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @PutMapping("/{productId}")
    ProductResponseDto update(@PathVariable @Positive Long productId,
                              @RequestBody @Valid ProductCreateRequestDto productRequest);

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{productId}")
    void delete(@PathVariable @Positive Long productId);

    @PostMapping("/{productId}/discount/{discountPercentage}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.ACCEPTED)
    ProductResponseDto setDiscount(
            @PathVariable @Positive Long productId,
            @PathVariable
            @Min(value = 1, message = "Discount must be at least 1%")
            @Max(value = 99, message = "Discount cannot exceed 99%")
            BigDecimal discountPercentage
    );

    @GetMapping("/product-of-the-day")
    ProductResponseDto getProductOfTheDay();
}