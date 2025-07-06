package de.telran.gardenStore.controller;

import de.telran.gardenStore.dto.ProductCreateRequestDto;
import de.telran.gardenStore.dto.ProductFilter;
import de.telran.gardenStore.dto.ProductResponseDto;
import de.telran.gardenStore.entity.Product;
import de.telran.gardenStore.service.ProductService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@Validated
public class ProductControllerImpl implements ProductController {

    private final ProductService productService;
    private final ModelMapper modelMapper;

    @Override
    public List<ProductResponseDto> getAllProducts(ProductFilter productFilter){
         //(Long categoryId, Boolean discount, BigDecimal minPrice, BigDecimal maxPrice, String sortBy, Boolean sortDirection){
//        ProductFilter productFilter = ProductFilter.builder()
//                .categoryId(categoryId)
//                .discount(discount)
//                .maxPrice(maxPrice)
//                .minPrice(minPrice)
//                .sortBy(sortBy)
//                .sortDirection(sortDirection)
//                .build();

        List<Product> products = productService.getAllProducts(productFilter);

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