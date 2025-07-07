package de.telran.gardenStore.controller;

import de.telran.gardenStore.dto.ProductCreateRequestDto;
import de.telran.gardenStore.dto.ProductResponseDto;
import de.telran.gardenStore.entity.Product;
import de.telran.gardenStore.service.ProductService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@Validated
public class ProductControllerImpl implements ProductController {

    private final ProductService productService;
    private final ModelMapper modelMapper;

    @Override
    public List<ProductResponseDto> getAllProducts(@Positive Long categoryId,
                                                   Boolean discount,
                                                   @Positive BigDecimal minPrice,
                                                   @Positive BigDecimal maxPrice,
                                                   @Pattern(regexp = "productId|name|price|category|discountPrice|createdAt|updatedAt")
                                                   String sortBy,
                                                   Boolean sortDirection){

        if (minPrice != null && maxPrice != null && minPrice.compareTo(maxPrice) > 0) {
            throw new IllegalArgumentException("Min price cannot be greater than max price.");
        }

        List<Product> products = productService.getAllProducts(categoryId, discount, minPrice, maxPrice, sortBy, sortDirection);

        return products.stream().map(product -> modelMapper.map(product, ProductResponseDto.class)).collect(Collectors.toList());
    }

    @Override
    public ProductResponseDto getProductById(@Positive Long productId) {
        Product product = productService.getProductById(productId);
        return modelMapper.map(product, ProductResponseDto.class);
    }

    @Override
    public ProductResponseDto createProduct(@Valid ProductCreateRequestDto productRequest) {
        Product product = modelMapper.map(productRequest, Product.class);
        Product savedProduct = productService.createProduct(product);
        return modelMapper.map(savedProduct, ProductResponseDto.class);
    }

    @Override
    public ProductResponseDto updateProduct(@Positive Long productId, @Valid ProductCreateRequestDto productRequest) {

        Product product = modelMapper.map(productRequest, Product.class);
        Product updatedProduct = productService.updateProduct(productId, product);
        return modelMapper.map(updatedProduct, ProductResponseDto.class);
    }

    @Override
    public void deleteProductById(@Positive Long productId) {
        productService.deleteProductById(productId);
    }
}