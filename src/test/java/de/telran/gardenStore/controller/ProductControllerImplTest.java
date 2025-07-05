package de.telran.gardenStore.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.telran.gardenStore.dto.ProductCreateRequestDto;
import de.telran.gardenStore.dto.ProductResponseDto;
import de.telran.gardenStore.entity.Product;
import de.telran.gardenStore.exception.ProductNotFoundException;
import de.telran.gardenStore.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductControllerImpl.class)
@AutoConfigureMockMvc(addFilters = false) // Отключаем security фильтры для тестов
public class ProductControllerImplTest {

    @Autowired
    private MockMvc mockMvc; // Для имитации HTTP запросов

    @Autowired
    private ObjectMapper objectMapper; // Для преобразования объектов в JSON и обратно

    @MockitoBean
    private ProductService productService; // Мок сервиса

    @MockitoBean
    private ModelMapper modelMapper; // Мок маппера DTO


    private Product product1;
    private Product product2;
    private Product productToCreate;
    private Product productCreated;

    private ProductCreateRequestDto productCreateRequestDto;
    private ProductResponseDto productResponseDto1;
    private ProductResponseDto productResponseDto2;
    private ProductResponseDto productResponseCreatedDto;

    @BeforeEach
    void setUp() {

        product1 = Product.builder()
                .productId(1L)
                .name("All-Purpose Plant Fertilizer")
                .discountPrice(new BigDecimal("8.99"))
                .price(new BigDecimal("11.99"))
                .categoryId(1L)
                .description("Balanced NPK formula for all types of plants")
                .imageUrl("https://example.com/images/fertilizer_all_purpose.jpg")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        product2 = Product.builder()
                .productId(2L)
                .name("Organic Tomato Feed")
                .discountPrice(new BigDecimal("10.49"))
                .price(new BigDecimal("13.99"))
                .categoryId(1L)
                .description("Organic liquid fertilizer ideal for tomatoes and vegetables")
                .imageUrl("https://example.com/images/fertilizer_tomato_feed.jpg")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        productToCreate = Product.builder()
                .name("Slug & Snail Barrier Pellets")
                .discountPrice(new BigDecimal("5.75"))
                .price(new BigDecimal("7.50"))
                .categoryId(2L)
                .description("Pet-safe barrier pellets to protect plants from slugs")
                .imageUrl("https://example.com/images/protection_slug_pellets.jpg")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        productCreated = productToCreate.toBuilder()
                .productId(3L)
                .build();

        productResponseDto1 = ProductResponseDto.builder()
                .productId(product1.getProductId())
                .name(product1.getName())
                .price(product1.getPrice())
                .discountPrice(product1.getDiscountPrice())
                .categoryId(product1.getCategoryId())
                .description(product1.getDescription())
                .imageUrl(product1.getImageUrl())
                .build();

        productResponseDto2 = ProductResponseDto.builder()
                .productId(product2.getProductId())
                .name(product2.getName())
                .price(product2.getPrice())
                .discountPrice(product2.getDiscountPrice())
                .categoryId(product2.getCategoryId())
                .description(product2.getDescription())
                .imageUrl(product2.getImageUrl())
                .build();

        productCreateRequestDto = ProductCreateRequestDto.builder()
                .name(productToCreate.getName())
                .price(productToCreate.getPrice())
                .discountPrice(productToCreate.getDiscountPrice())
                .categoryId(productToCreate.getCategoryId())
                .description(productToCreate.getDescription())
                .imageUrl(productToCreate.getImageUrl())
                .build();

        productResponseCreatedDto = ProductResponseDto.builder()
                .productId(productCreated.getProductId())
                .name(productCreated.getName())
                .price(productCreated.getPrice())
                .discountPrice(productCreated.getDiscountPrice())
                .categoryId(productCreated.getCategoryId())
                .description(productCreated.getDescription())
                .imageUrl(productCreated.getImageUrl())
                .build();
    }

    @Test
    @DisplayName("GET /v1/products - Get all products")
    void getAllProducts() throws Exception {

        List<Product> products = List.of(product1, product2);

        List<ProductResponseDto> expected = List.of(productResponseDto1, productResponseDto2);

        when(productService.getAllProducts()).thenReturn(products);
        when(modelMapper.map(product1, ProductResponseDto.class)).thenReturn(productResponseDto1);
        when(modelMapper.map(product2, ProductResponseDto.class)).thenReturn(productResponseDto2);

        mockMvc.perform(get("/v1/products"))
                .andDo(print()) // Логирование запроса и ответа
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json(objectMapper.writeValueAsString(expected)));
    }

    @Test
    @DisplayName("GET /v1/products/{productId} - Get product by ID : positive case")
    void getProductByIdPositiveCase() throws Exception {
        Long productId = 1L;

        when(productService.getProductById(productId)).thenReturn(product1);
        when(modelMapper.map(product1, ProductResponseDto.class)).thenReturn(productResponseDto1);

        mockMvc.perform(get("/v1/products/{productId}", productId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json(objectMapper.writeValueAsString(productResponseDto1)));
    }

    @Test
    @DisplayName("GET /v1/products/{productId} Get product by ID : negative case")
    void getProductByIdNegativeCase() throws Exception {
        Long productId = 999L;

        when(productService.getProductById(productId))
                .thenThrow(new ProductNotFoundException("Product with id: " + productId + " not found"));


        mockMvc.perform(get("/v1/products/{productId}", productId))
                .andDo(print())
                .andExpectAll(
                        status().isNotFound(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.exception").value("ProductNotFoundException"),
                        jsonPath("$.message").value("Product with id: " + productId + " not found"),
                        jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()));
    }

    @Test
    @DisplayName("POST /v1/products - Create new product")
    void createProduct() throws Exception {

        when(modelMapper.map(productCreateRequestDto, Product.class)).thenReturn(productToCreate);
        when(productService.createProduct(productToCreate)).thenReturn(productCreated);
        when(modelMapper.map(productCreated, ProductResponseDto.class)).thenReturn(productResponseCreatedDto);

        mockMvc.perform(post("/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productCreateRequestDto)))
                .andDo(print())
                .andExpectAll(
                        status().isCreated(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json(objectMapper.writeValueAsString(productResponseCreatedDto)));
    }

    @Test
    @DisplayName("PUT /v1/products/{id} - Update product")
    void updateProduct() throws Exception {
        Long productId = 3L;

        Product productToUpdate = productToCreate.toBuilder()
                .name("Garden Tool Set (5 pcs)")
                .build();

        Product productUpdated = productToCreate.toBuilder()
                .productId(productId)
                .build();

        ProductCreateRequestDto productUpdateRequestDto = productCreateRequestDto.toBuilder()
                .name("Garden Tool Set (5 pcs)")
                .build();

        ProductResponseDto productResponseUpdatedDto = productResponseCreatedDto.toBuilder()
                .name("Garden Tool Set (5 pcs)")
                .build();


        when(modelMapper.map(productUpdateRequestDto, Product.class)).thenReturn(productToUpdate);
        when(productService.updateProduct(productId, productToUpdate)).thenReturn(productUpdated);
        when(modelMapper.map(productUpdated, ProductResponseDto.class)).thenReturn(productResponseUpdatedDto);

        mockMvc.perform(put("/v1/products/{productId}", productId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productUpdateRequestDto)))
                .andDo(print())
                .andExpectAll(
                        status().isAccepted(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json(objectMapper.writeValueAsString(productResponseUpdatedDto)));
    }

    @Test
    @DisplayName("DELETE /v1/products/{id} - Delete product by ID")
    void deleteProduct_ShouldDeleteProduct() throws Exception {

        Long productId = 1L;

        when(productService.getProductById(productId)).thenReturn(product1);
        doNothing().when(productService).deleteProductById(productId);

        mockMvc.perform(delete("/v1/products/{productId}", productId))
                .andDo(print())
                .andExpect(status().isOk());
    }
}