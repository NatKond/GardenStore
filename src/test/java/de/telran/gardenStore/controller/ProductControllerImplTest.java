package de.telran.gardenStore.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.telran.gardenStore.dto.ProductCreateRequestDto;
import de.telran.gardenStore.dto.ProductResponseDto;
import de.telran.gardenStore.entity.Product;
import de.telran.gardenStore.exception.ProductNotFoundException;
import de.telran.gardenStore.service.ProductService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.hasItems;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductControllerImpl.class)
@AutoConfigureMockMvc(addFilters = false) // Отключаем security фильтры для тестов
class ProductControllerImplTest {

    @Autowired
    private MockMvc mockMvc; // Для имитации HTTP запросов

    @Autowired
    private ObjectMapper objectMapper; // Для преобразования объектов в JSON и обратно

    @MockitoBean
    private ProductService productService; // Мок сервиса

    @MockitoBean
    private ModelMapper modelMapper; // Мок маппера DTO


    private static Product product1;
    private static Product product2;
    private static ProductCreateRequestDto productCreateRequestDto;
    private static ProductResponseDto productResponseDto1;
    private static ProductResponseDto productResponseDto2;
    private static ProductResponseDto productResponseCreatedDto;

    @BeforeAll
    static void setUp() {

        product1 = Product.builder()
                .productId(1L)
                .name("Удобрение для цветов")
                .price(BigDecimal.valueOf(299.99))
                .discountPrice(BigDecimal.valueOf(249.99))
                .categoryId(1L)
                .description("Комплексное удобрение")
                .imageUrl("http://example.com/udobrenie.jpg")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        product2 = Product.builder()
                .productId(2L)
                .name("Лейка садовая")
                .price(BigDecimal.valueOf(599.99))
                .discountPrice(null)
                .categoryId(4L)
                .description("Пластиковая лейка 5л")
                .imageUrl("http://example.com/leika.jpg")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        productCreateRequestDto = ProductCreateRequestDto.builder()
                .name("Горшок для цветов")
                .price(BigDecimal.valueOf(399.99))
                .discountPrice(BigDecimal.valueOf(349.99))
                .categoryId(5L)
                .description("Керамический горшок")
                .imageUrl("http://example.com/goroshok.jpg")
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

        productResponseCreatedDto = ProductResponseDto.builder()
                .productId(3L)
                .name(productCreateRequestDto.getName())
                .price(productCreateRequestDto.getPrice())
                .discountPrice(productCreateRequestDto.getDiscountPrice())
                .categoryId(productCreateRequestDto.getCategoryId())
                .description(productCreateRequestDto.getDescription())
                .imageUrl(productCreateRequestDto.getImageUrl())
                .build();
    }

    @Test
    @DisplayName("GET /v1/products - get all products")
    void getAllProducts_ShouldReturnAllProducts() throws Exception {

        List<Product> products = List.of(product1, product2);

        when(productService.getAllProducts()).thenReturn(products);
        when(modelMapper.map(product1, ProductResponseDto.class)).thenReturn(productResponseDto1);
        when(modelMapper.map(product2, ProductResponseDto.class)).thenReturn(productResponseDto2);

        mockMvc.perform(get("/v1/products"))
                .andDo(print()) // Логирование запроса и ответа
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].productId", hasItems(1, 2))) // Проверка ID продуктов
                .andExpect(jsonPath("$[*].name", hasItems("Удобрение для цветов", "Лейка садовая")));
    }

    @Test
    @DisplayName("GET /v1/products/{id} - successful retrieval of product by ID")
    void getProductById_ShouldReturnProduct_WhenProductExists() throws Exception {
        Long productId = 1L;

        when(productService.getProductById(productId)).thenReturn(product1);
        when(modelMapper.map(product1, ProductResponseDto.class)).thenReturn(productResponseDto1);

        mockMvc.perform(get("/v1/products/{productId}", productId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productId").value(productId))
                .andExpect(jsonPath("$.name").value(product1.getName()))
                .andExpect(jsonPath("$.price").value(product1.getPrice().doubleValue()));
    }

    @Test
    @DisplayName("GET /v1/products/{id} - product not found")
    void getProductById_ShouldReturnNotFound_WhenProductNotExists() throws Exception {
        Long productId = 999L;

        when(productService.getProductById(productId))
                .thenThrow(new ProductNotFoundException("Product with id: " + productId + " not found"));


        mockMvc.perform(get("/v1/products/{productId}", productId))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST /v1/products - successful product creation")
    void createProduct_ShouldCreateNewProduct() throws Exception {

        Product newProduct = Product.builder()
                .name(productCreateRequestDto.getName())
                .price(productCreateRequestDto.getPrice())
                .discountPrice(productCreateRequestDto.getDiscountPrice())
                .categoryId(productCreateRequestDto.getCategoryId())
                .description(productCreateRequestDto.getDescription())
                .imageUrl(productCreateRequestDto.getImageUrl())
                .build();

        Product savedProduct = Product.builder()
                .productId(3L)
                .name(newProduct.getName())
                .price(newProduct.getPrice())
                .discountPrice(newProduct.getDiscountPrice())
                .categoryId(newProduct.getCategoryId())
                .description(newProduct.getDescription())
                .imageUrl(newProduct.getImageUrl())
                .build();

        when(modelMapper.map(productCreateRequestDto, Product.class)).thenReturn(newProduct);
        when(productService.createProduct(newProduct)).thenReturn(savedProduct);
        when(modelMapper.map(savedProduct, ProductResponseDto.class)).thenReturn(productResponseCreatedDto);

        mockMvc.perform(post("/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productCreateRequestDto)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.productId").value(3L))
                .andExpect(jsonPath("$.name").value(productCreateRequestDto.getName()))
                .andExpect(jsonPath("$.price").value(productCreateRequestDto.getPrice().doubleValue()));
    }

    @Test
    @DisplayName("PUT /v1/products/{id} - successful product update")
    void updateProduct_ShouldUpdateProduct() throws Exception {
        // Подготовка
        Long productId = 1L;
        Product updatedProduct = Product.builder()
                .name("Обновленное удобрение")
                .price(BigDecimal.valueOf(349.99))
                .discountPrice(BigDecimal.valueOf(299.99))
                .categoryId(1L)
                .description("Новое описание")
                .imageUrl("http://example.com/new-image.jpg")
                .build();

        ProductResponseDto updatedResponseDto = ProductResponseDto.builder()
                .productId(productId)
                .name(updatedProduct.getName())
                .price(updatedProduct.getPrice())
                .discountPrice(updatedProduct.getDiscountPrice())
                .categoryId(updatedProduct.getCategoryId())
                .description(updatedProduct.getDescription())
                .imageUrl(updatedProduct.getImageUrl())
                .build();

        when(modelMapper.map(productCreateRequestDto, Product.class)).thenReturn(updatedProduct);
        when(productService.updateProduct(productId, updatedProduct)).thenReturn(updatedProduct);
        when(modelMapper.map(updatedProduct, ProductResponseDto.class)).thenReturn(updatedResponseDto);

        mockMvc.perform(put("/v1/products/{productId}", productId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productCreateRequestDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productId").value(productId))
                .andExpect(jsonPath("$.name").value(updatedProduct.getName()));
    }

    @Test
    @DisplayName("DELETE /v1/products/{id} -  successful product deletion")
    void deleteProduct_ShouldDeleteProduct() throws Exception {

        Long productId = 1L;

        mockMvc.perform(delete("/v1/products/{productId}", productId))
                .andDo(print())
                .andExpect(status().isOk());
    }
}

//
//when(modelMapper.map(productCreateRequestDto, Product.class)).thenReturn(updatedProduct);
//when(productService.updateProduct(productId, updatedProduct)).thenReturn(updatedProduct);
//when(modelMapper.map(updatedProduct, ProductResponseDto.class)).thenReturn(updatedResponseDto);

