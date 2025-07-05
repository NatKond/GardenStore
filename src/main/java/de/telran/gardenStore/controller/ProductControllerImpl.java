package de.telran.gardenStore.controller;

import de.telran.gardenStore.dto.ProductCreateRequestDto;
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
@RequestMapping("/products")
public class ProductControllerImp implements ProductController {

    private final ProductService productService;

    private final ModelMapper modelMapper;

    @Override
    public List<ProductResponseDto> getAllProducts() {
        return productService.getAllProducts().stream()
                .map(product -> modelMapper.map(product, ProductResponseDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public ProductResponseDto getProductById(@Positive Long productId) {
        return modelMapper.map(productService.getProductById(productId), ProductResponseDto.class);
    }

    @Override
    public ProductResponseDto createProduct(@Valid ProductCreateRequestDto productRequest) {
        return modelMapper.map(
                productService.createProduct(
                        modelMapper.map(productRequest, Product.class)),
                ProductResponseDto.class);
    }

    @Override
    public ProductResponseDto updateProduct(@Positive Long productId,
                                            @Valid ProductCreateRequestDto productRequest) {
        return modelMapper.map(
                productService.updateProduct(
                        productId,
                        modelMapper.map(productRequest, Product.class)),
                ProductResponseDto.class
        );
    }

    @Override
    public void deleteProductById(@Positive Long productId) {
        productService.deleteProductById(productId);
    }
}

