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
@RequestMapping("/products")
public class ProductControllerImp implements ProductController {

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
    @GetMapping("/{id}")
    public ProductResponseDto getProductById(@PathVariable Long id) {
        return modelMapper.map(productService.getProductById(id), ProductResponseDto.class);
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

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ProductResponseDto updateProduct(@PathVariable Long id,
                                            @RequestBody @Valid ProductCreateRequestDto productRequest) {
        return modelMapper.map(
                productService.updateProduct(
                        id,
                        modelMapper.map(productRequest, Product.class)
                ),
                ProductResponseDto.class
        );
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public void deleteProductById(@PathVariable Long id) {
        productService.deleteProductById(id);
    }
}

