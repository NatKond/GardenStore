package de.telran.gardenStore.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.telran.gardenStore.AbstractTest;
import de.telran.gardenStore.converter.Converter;
import de.telran.gardenStore.dto.ProductCreateRequestDto;
import de.telran.gardenStore.dto.ProductResponseDto;
import de.telran.gardenStore.dto.ProductShortResponseDto;
import de.telran.gardenStore.entity.Product;
import de.telran.gardenStore.exception.NoDiscountedProductsException;
import de.telran.gardenStore.exception.ProductNotFoundException;
import de.telran.gardenStore.service.ProductService;
import de.telran.gardenStore.service.security.JwtService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductControllerImpl.class)
@AutoConfigureMockMvc(addFilters = false)
public class ProductControllerImplTest extends AbstractTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private ProductService productService;

    @MockitoBean
    private Converter<Product, ProductCreateRequestDto, ProductResponseDto, ProductShortResponseDto> productConverter;

    @Test
    @DisplayName("GET /v1/products - Get all products : positive case")
    void getAllPositiveCase() throws Exception {
        List<Product> products = List.of(product1, product2);

        List<ProductShortResponseDto> expected = List.of(productShortResponseDto1, productShortResponseDto2);

        when(productService.getAll(null, null, null, null, null, null)).thenReturn(products);
        when(productConverter.convertEntityListToDtoList(products)).thenReturn(expected);

        mockMvc.perform(get("/v1/products"))
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json(objectMapper.writeValueAsString(expected)));
    }

    @Test
    @DisplayName("GET /v1/products - Get all products : negative case")
    void getAllNegativeCase() throws Exception {

        mockMvc.perform(get("/v1/products")
                        .param("minPrice", "10")
                        .param("maxPrice", "5"))
                .andDo(print())
                .andExpectAll(
                        status().isBadRequest(),
                        jsonPath("$.exception").value("IllegalArgumentException"),
                        jsonPath("$.message").value("Min price cannot be greater than max price"));
    }

    @Test
    @DisplayName("GET /v1/products/{productId} - Get product by ID : positive case")
    void getByIdPositiveCase() throws Exception {
        Long productId = 1L;

        when(productService.getById(productId)).thenReturn(product1);
        when(productConverter.convertEntityToDto(product1)).thenReturn(productResponseDto1);

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
    void getByIdNegativeCase() throws Exception {
        Long productId = 999L;

        when(productService.getById(productId))
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
    void create() throws Exception {

        when(productConverter.convertDtoToEntity(productCreateRequestDto)).thenReturn(productToCreate);
        when(productService.create(productToCreate)).thenReturn(productCreated);
        when(productConverter.convertEntityToDto(productCreated)).thenReturn(productResponseCreatedDto);

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
    @DisplayName("PUT /v1/products/{productId} - Update product")
    void update() throws Exception {
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


        when(productConverter.convertDtoToEntity(productUpdateRequestDto)).thenReturn(productToUpdate);
        when(productService.update(productId, productToUpdate)).thenReturn(productUpdated);
        when(productConverter.convertEntityToDto(productUpdated)).thenReturn(productResponseUpdatedDto);

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
    @DisplayName("DELETE /v1/products/{productId} - Delete product by ID")
    void deleteProduct() throws Exception {
        Long productId = 1L;

        doNothing().when(productService).deleteById(productId);

        mockMvc.perform(delete("/v1/products/{productId}", productId))
                .andDo(print())
                .andExpect(status().isOk());

        verify(productService).deleteById(productId);
    }

    @Test
    @DisplayName("POST /v1/products/{productId}/discount/{discountPercentage} - Set discount")
    void setDiscountPositiveCase() throws Exception {
        Long productId = product1.getProductId();
        BigDecimal discountPercentage = new BigDecimal("20");
        BigDecimal originalPrice = product1.getPrice();

        BigDecimal discountPrice = originalPrice.multiply(discountPercentage)
                .divide(new BigDecimal(100),2, RoundingMode.HALF_UP);

        Product productWithDiscount = product1.toBuilder()
                .discountPrice(discountPrice)
                .build();

        ProductResponseDto expected = productResponseDto1.toBuilder()
                .discountPrice(discountPrice)
                .build();

        when(productService.setDiscount(productId, discountPercentage)).thenReturn(productWithDiscount);
        when(productConverter.convertEntityToDto(productWithDiscount)).thenReturn(expected);

        mockMvc.perform(post("/v1/products/{productId}/discount/{discountPercentage}", productId, discountPercentage))
                .andDo(print())
                .andExpectAll(
                        status().isAccepted(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json(objectMapper.writeValueAsString(expected)));
    }

    @Test
    @DisplayName("GET /v1/products/product-of-the-day - Get the product of the day: positive case")
    void getProductOfTheDayPositiveCase() throws Exception {
        Product productOfTheDay = product1;
        ProductResponseDto expected = productResponseDto1;

        when(productService.getProductOfTheDay()).thenReturn(productOfTheDay);
        when(productConverter.convertEntityToDto(productOfTheDay)).thenReturn(expected);

        mockMvc.perform(get("/v1/products/product-of-the-day"))
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json(objectMapper.writeValueAsString(expected)));
    }

    @Test
    @DisplayName("GET /v1/products/product-of-the-day - Get the product of the day: negative case")
    void getProductOfTheDayNegativeCase() throws Exception {
        when(productService.getProductOfTheDay()).thenThrow(new NoDiscountedProductsException("No discounted products available"));

        mockMvc.perform(get("/v1/products/product-of-the-day"))
                .andDo(print())
                .andExpectAll(
                        status().isBadRequest(),
                        jsonPath("$.exception").value("NoDiscountedProductsException"),
                        jsonPath("$.message").value("No discounted products available"));
    }
}