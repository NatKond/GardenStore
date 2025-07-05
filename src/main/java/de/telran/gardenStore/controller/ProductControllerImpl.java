package de.telran.gardenStore.controller;

import de.telran.gardenStore.dto.ProductCreateRequestDto;
import de.telran.gardenStore.dto.ProductResponseDto;
import de.telran.gardenStore.entity.Product;
import de.telran.gardenStore.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/products")
public class ProductControllerImpl implements ProductController {

    private final ProductService productService;
    private final ModelMapper modelMapper;

    @Override
    @GetMapping
    public List<ProductResponseDto> getAllProducts() {
        return productService.getAllProducts().stream()
                .map(product -> modelMapper.map(product, ProductResponseDto.class))
                .collect(Collectors.toList());
    }

    @Override
    @GetMapping("/{productId}")
    public ProductResponseDto getProductById(@PathVariable Long productId) {
        return modelMapper.map(productService.getProductById(productId), ProductResponseDto.class);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ProductResponseDto createProduct(@RequestBody @Valid ProductCreateRequestDto productRequest) {
        return modelMapper.map(
                productService.createProduct(
                        modelMapper.map(productRequest, Product.class)),
                ProductResponseDto.class);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{productId}")
    public ProductResponseDto updateProduct(@PathVariable Long productId,
                                            @RequestBody @Valid ProductCreateRequestDto productRequest) {
        return modelMapper.map(
                productService.updateProduct(
                        productId,
                        modelMapper.map(productRequest, Product.class)
                ),
                ProductResponseDto.class
        );
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{productId}")
    public void deleteProductById(@PathVariable Long productId) {
        productService.deleteProductById(productId);
    }
}

