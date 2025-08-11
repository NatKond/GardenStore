package de.telran.gardenStore.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.telran.gardenStore.AbstractTest;
import de.telran.gardenStore.dto.ProductCreateRequestDto;
import de.telran.gardenStore.dto.ProductResponseDto;
import de.telran.gardenStore.dto.ProductShortResponseDto;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;


@SpringBootTest
@AutoConfigureMockMvc(printOnlyOnFailure = false)
@Transactional
//@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ProductIntegrationTest extends AbstractTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("GET /v1/products - Get all products : positive case")
    void getAll() throws Exception {
        List<ProductShortResponseDto> expected = List.of(productShortResponseDto1, productShortResponseDto2, productShortResponseDto3);

        mockMvc.perform(get("/v1/products"))
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json(objectMapper.writeValueAsString(expected)));
    }

    @Test
    @DisplayName("GET /v1/products/{productId} - Get product by ID : positive case")
    void getByIdPositiveCase() throws Exception {
        Long productId = 1L;

        mockMvc.perform(get("/v1/products/{productId}", productId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json(objectMapper.writeValueAsString(productResponseDto1)));
    }

    @Test
    @DisplayName("GET /v1/products/{productId} - Get product by ID : negative case")
    void getByIdNegativeCase() throws Exception {
        Long productId = 999L;

        mockMvc.perform(get("/v1/products/{productId}", productId))
                .andDo(print())
                .andExpectAll(
                        status().isNotFound(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.exception").value("ProductNotFoundException"),
                        jsonPath("$.message").value("Product with id " + productId + " not found"),
                        jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()));
    }

    @Test
    @DisplayName("POST /v1/products - Create new product")
    void create() throws Exception {

        mockMvc.perform(post("/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productCreateRequestDto))
                        .with(httpBasic("alice.johnson@example.com", "12345")))
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

        ProductCreateRequestDto productUpdateRequestDto = ProductCreateRequestDto.builder()
                .name("Pet-Safe Slug Defense")
                .description(product3.getDescription())
                .price(product3.getPrice())
                .discountPrice(product3.getDiscountPrice())
                .categoryId(product3.getCategory().getCategoryId())
                .imageUrl(product3.getImageUrl())
                .build();

        ProductResponseDto productResponseUpdatedDto = productResponseDto3.toBuilder()
                .name("Pet-Safe Slug Defense")
                .build();

        mockMvc.perform(put("/v1/products/{productId}", productId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productUpdateRequestDto))
                        .with(httpBasic("alice.johnson@example.com", "12345")))
                .andDo(print())
                .andExpectAll(
                        status().isAccepted(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json(objectMapper.writeValueAsString(productResponseUpdatedDto)));
    }

    @Test
    @DisplayName("DELETE /v1/products/{productId} - Delete product by ID")
    void deleteProduct() throws Exception {
        Long productId = 2L;

        mockMvc.perform(delete("/v1/products/{productId}", productId)
                        .with(httpBasic("alice.johnson@example.com", "12345")))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("POST /v1/products/{productId}/discount/{discountPercentage} - Set discount")
    void setDiscountPositiveCase() throws Exception {
        Long productId = product1.getProductId();
        BigDecimal discountPercentage = new BigDecimal("20");
        BigDecimal originalPrice = product1.getPrice();

        BigDecimal discountPrice = originalPrice.subtract(originalPrice.multiply(discountPercentage)
                .divide(new BigDecimal(100), 2, RoundingMode.HALF_UP));

        ProductResponseDto expected = productResponseDto1.toBuilder()
                .discountPrice(discountPrice)
                .build();

        mockMvc.perform(post("/v1/products/{productId}/discount/{discountPercentage}", productId, discountPercentage)
                        .with(httpBasic("alice.johnson@example.com", "12345")))
                .andDo(print())
                .andExpectAll(
                        status().isAccepted(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json(objectMapper.writeValueAsString(expected)));
    }

    @Test
    @DisplayName("GET /v1/products/product-of-the-day - Get the product of the day: positive case")
    void getProductOfTheDayPositiveCase() throws Exception {
        ProductResponseDto expected = productResponseDto2;

        mockMvc.perform(get("/v1/products/product-of-the-day"))
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json(objectMapper.writeValueAsString(expected)));
    }
}