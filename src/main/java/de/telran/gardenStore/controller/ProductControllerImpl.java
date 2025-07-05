package de.telran.gardenStore.controller;

import de.telran.gardenStore.dto.ProductCreateRequestDto;
import de.telran.gardenStore.dto.ProductResponseDto;
import de.telran.gardenStore.entity.Product;
import de.telran.gardenStore.service.ProductService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RequiredArgsConstructor
@RestController
@Validated
public class ProductControllerImpl implements ProductController {

    private final ProductService productService;
    private final ModelMapper modelMapper;

    @Override
    public Page<ProductResponseDto> getAllProducts(
            @RequestParam(required = false) Long category,
            @RequestParam(required = false) Boolean discount,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) String[] sort,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<Product> products = productService.getAllProducts(
                category, discount, minPrice, maxPrice, sort, page, size);

        return products.map(product -> modelMapper.map(product, ProductResponseDto.class));
    }

    @Override
    public ProductResponseDto getProductById(@PathVariable @Positive Long productId) {
        Product product = productService.getProductById(productId);
        return modelMapper.map(product, ProductResponseDto.class);
    }

    @Override
    public ProductResponseDto createProduct(@RequestBody @Valid ProductCreateRequestDto productRequest) {
        Product product = modelMapper.map(productRequest, Product.class);
        Product savedProduct = productService.createProduct(product);
        return modelMapper.map(savedProduct, ProductResponseDto.class);
    }

    @Override
    public ProductResponseDto updateProduct(
            @PathVariable @Positive Long productId,
            @RequestBody @Valid ProductCreateRequestDto productRequest) {

        Product product = modelMapper.map(productRequest, Product.class);
        Product updatedProduct = productService.updateProduct(productId, product);
        return modelMapper.map(updatedProduct, ProductResponseDto.class);
    }

    @Override
    public void deleteProductById(@PathVariable @Positive Long productId) {
        productService.deleteProductById(productId);
    }
}