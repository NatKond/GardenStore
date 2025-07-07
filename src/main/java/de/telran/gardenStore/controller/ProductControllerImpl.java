package de.telran.gardenStore.controller;

import de.telran.gardenStore.converter.Converter;
import de.telran.gardenStore.dto.ProductCreateRequestDto;
import de.telran.gardenStore.dto.ProductResponseDto;
import de.telran.gardenStore.entity.Product;
import de.telran.gardenStore.service.ProductService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RequiredArgsConstructor
@RestController
@Validated
public class ProductControllerImpl implements ProductController {

    private final ProductService productService;

    private final Converter<Product, ProductCreateRequestDto, ProductResponseDto, ProductResponseDto> productConverter;

    @Override
    public List<ProductResponseDto> getAllProducts(@Positive Long categoryId,
                                                   Boolean discount,
                                                   @Positive BigDecimal minPrice,
                                                   @Positive BigDecimal maxPrice,
                                                   @Pattern(regexp = "productId|name|price|category|discountPrice|createdAt|updatedAt")
                                                   String sortBy,
                                                   Boolean sortDirection) {

        if (minPrice != null && maxPrice != null && minPrice.compareTo(maxPrice) > 0) {
            throw new IllegalArgumentException("Min price cannot be greater than max price.");
        }
        return productConverter.convertEntityListToDtoList(productService.getAllProducts(categoryId, discount, minPrice, maxPrice, sortBy, sortDirection));
    }

    @Override
    public ProductResponseDto getProductById(@Positive Long productId) {
        return productConverter.convertEntityToDto(productService.getProductById(productId));
    }

    @Override
    public ProductResponseDto createProduct(@Valid ProductCreateRequestDto productRequest) {
        return productConverter.convertEntityToDto(productService.createProduct(
                productConverter.convertDtoToEntity(productRequest)));
    }

    @Override
    public ProductResponseDto updateProduct(@Positive Long productId, @Valid ProductCreateRequestDto productRequest) {
        return productConverter.convertEntityToDto(productService.updateProduct(productId,
                productConverter.convertDtoToEntity(productRequest)));
    }

    @Override
    public void deleteProductById(@Positive Long productId) {
        productService.deleteProductById(productId);
    }
}