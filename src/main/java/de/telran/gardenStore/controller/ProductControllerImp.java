package de.telran.gardenStore.controller;

import de.telran.gardenStore.dto.ProductCreateRequestDto;
import de.telran.gardenStore.dto.ProductResponseDto;
import de.telran.gardenStore.dto.UserCreateRequestDto;
import de.telran.gardenStore.dto.UserResponseDto;
import de.telran.gardenStore.entity.Product;
import de.telran.gardenStore.entity.User;
import de.telran.gardenStore.service.ProductService;
import de.telran.gardenStore.service.UserService;
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
    @GetMapping("/{id}")
    public ProductResponseDto getProductById(@PathVariable Long id) {
        return modelMapper.map(productService.getProductById(id), ProductResponseDto.class);
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
    @DeleteMapping("/{id}")
    public void deleteProductById(@PathVariable Long id) {
        productService.deleteProductById(id);
    }
}
