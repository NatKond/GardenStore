package de.telran.gardenStore.controller;

import de.telran.gardenStore.converter.Converter;
import de.telran.gardenStore.dto.ProductCreateRequestDto;
import de.telran.gardenStore.dto.ProductResponseDto;
import de.telran.gardenStore.dto.ProductShortResponseDto;
import de.telran.gardenStore.entity.Product;
import de.telran.gardenStore.service.ProductService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/products")
@Validated
public class ProductControllerImpl implements ProductController {

    private final ProductService productService;

    private final Converter<Product, ProductCreateRequestDto, ProductResponseDto, ProductShortResponseDto> productConverter;

    @Override
    @GetMapping
    public List<ProductShortResponseDto> getAll(@RequestParam(required = false) @Positive Long categoryId,
                                                @RequestParam(required = false) Boolean discount,
                                                @RequestParam(required = false) @Positive BigDecimal minPrice,
                                                @RequestParam(required = false) @Positive BigDecimal maxPrice,
                                                @RequestParam(required = false)
                                                @Pattern(regexp = "productId|name|price|category|discountPrice|createdAt|updatedAt") String sortBy,
                                                @RequestParam(required = false) Boolean sortDirection) {

        if (minPrice != null && maxPrice != null && minPrice.compareTo(maxPrice) > 0) {
            throw new InvalidPriceRangeException("Min price cannot be greater than max price");
        }
        return productConverter.convertEntityListToDtoList(
                productService.getAll(categoryId, discount, minPrice, maxPrice, sortBy, sortDirection)
        );
    }

    @Override
    @GetMapping("/{productId}")
    public ProductResponseDto getById(@PathVariable @Positive Long productId) {
        return productConverter.convertEntityToDto(
                productService.getById(productId)
        );
    }

    @Override
    @GetMapping("/product-of-the-day")
    public ProductResponseDto getProductOfTheDay() {
        return productConverter.convertEntityToDto(
                productService.getProductOfTheDay());
    }

    @Override
    @Loggable
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ProductResponseDto create(@RequestBody @Valid ProductCreateRequestDto productRequest) {
        return productConverter.convertEntityToDto(productService.create(
                productConverter.convertDtoToEntity(productRequest)));
    }

    @Override
    @Loggable
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @PutMapping("/{productId}")
    public ProductResponseDto update(@PathVariable @Positive Long productId,
                                     @RequestBody @Valid ProductCreateRequestDto productRequest) {
        return productConverter.convertEntityToDto(productService.update(productId,
                productConverter.convertDtoToEntity(productRequest)));
    }

    @Override
    @Loggable
    @PatchMapping("/{productId}/discount/{discountPercentage}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ProductResponseDto setDiscount(@PathVariable @Positive Long productId,
                                          @PathVariable
                                          @Min(value = 1, message = "Discount must be at least 1%")
                                          @Max(value = 99, message = "Discount cannot exceed 99%")
                                          BigDecimal discountPercentage) {
        return productConverter.convertEntityToDto(
                productService.setDiscount(productId, discountPercentage));
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{productId}")
    public void delete(@PathVariable @Positive Long productId) {
        productService.deleteById(productId);
    }
}
