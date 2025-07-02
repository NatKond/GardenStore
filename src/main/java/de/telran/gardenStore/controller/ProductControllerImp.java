package de.telran.gardenStore.controller;

import de.telran.gardenStore.dto.ProductCreateRequestDto;
import de.telran.gardenStore.dto.ProductResponseDto;
import de.telran.gardenStore.entity.Product;
import de.telran.gardenStore.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user")

public class ProductControllerImp implements ProductController{

    private final ProductService productService;

    private final ModelMapper modelMapper;

    @Override
    @GetMapping
    public List<ProductResponseDto> getAllProducts() {
        return productService.getAllProducts().stream().map(product -> modelMapper.map(product, ProductResponseDto.class)).collect(Collectors.toList());
    }

    @Override
    @GetMapping("/{productId}")
    public ProductResponseDto getProductById(@PathVariable Long productId) {
        return modelMapper.map(productService.getProductById(productId), ProductResponseDto.class);
    }

    @Override
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/register")
    public ProductResponseDto createProduct(@RequestBody ProductCreateRequestDto productRequest) {
        return modelMapper.map(
                productService.createProduct(
                        modelMapper.map(productRequest, Product.class)),
                ProductResponseDto.class);
    }

    @Override
    @DeleteMapping("/{productId}")
    public void deleteProductById(@PathVariable Long productId) {
        productService.deleteProductById(productId);
    }
}
