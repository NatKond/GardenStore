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

@SpringBootTest
@AutoConfigureMockMvc(printOnlyOnFailure = false)
@Transactional
class ProductIntegrationTest extends AbstractTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("GET /v1/products — без фильтров: все продукты (по умолчанию сортировка по productId ASC)")
    void getAll_noFilters_ok() throws Exception {
        List<ProductShortResponseDto> expected = List.of(
                productShortResponseDto1,
                productShortResponseDto2,
                productShortResponseDto3
        );

        mockMvc.perform(get("/v1/products"))
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json(objectMapper.writeValueAsString(expected))
                );
    }

    @Test
    @DisplayName("GET /v1/products — сортировка по цене ASC (sortBy=price, sortDirection=false)")
    void getAll_sortByPriceAsc_ok() throws Exception {
        List<ProductShortResponseDto> expected = List.of(
                productShortResponseDto3,
                productShortResponseDto1,
                productShortResponseDto2
        );

        mockMvc.perform(get("/v1/products")
                        .param("sortBy", "price")
                        .param("sortDirection", "false"))
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json(objectMapper.writeValueAsString(expected))
                );
    }

    @Test
    @DisplayName("GET /v1/products — сортировка по цене DESC (sortBy=price, sortDirection=true)")
    void getAll_sortByPriceDesc_ok() throws Exception {
        List<ProductShortResponseDto> expected = List.of(
                productShortResponseDto2,
                productShortResponseDto1,
                productShortResponseDto3
        );

        mockMvc.perform(get("/v1/products")
                        .param("sortBy", "price")
                        .param("sortDirection", "true"))
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json(objectMapper.writeValueAsString(expected))
                );
    }

    @Test
    @DisplayName("GET /v1/products — невалидное поле сортировки приводит к 400")
    void getAll_invalidSortBy_badRequest() throws Exception {
        mockMvc.perform(get("/v1/products")
                        .param("sortBy", "pricee")
                        .param("sortDirection", "true"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("GET /v1/products — фильтр по категории=1 (Fertilizer)")
    void getAll_filterByCategory1_ok() throws Exception {
        List<ProductShortResponseDto> expected = List.of(
                productShortResponseDto1,
                productShortResponseDto2
        );

        mockMvc.perform(get("/v1/products")
                        .param("category", "1"))
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json(objectMapper.writeValueAsString(expected))
                );
    }

    @Test
    @DisplayName("GET /v1/products — фильтр по категории=2 (Protective...)")
    void getAll_filterByCategory2_ok() throws Exception {
        List<ProductShortResponseDto> expected = List.of(productShortResponseDto3);

        mockMvc.perform(get("/v1/products")
                        .param("category", "2"))
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json(objectMapper.writeValueAsString(expected))
                );
    }

    @Test
    @DisplayName("GET /v1/products — discount=true (все с ненулевым discountPrice)")
    void getAll_discountTrue_ok() throws Exception {
        List<ProductShortResponseDto> expected = List.of(
                productShortResponseDto1,
                productShortResponseDto2,
                productShortResponseDto3
        );

        mockMvc.perform(get("/v1/products")
                        .param("discount", "true"))
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json(objectMapper.writeValueAsString(expected))
                );
    }

    @Test
    @DisplayName("GET /v1/products — discount=false (все без скидки) — пустой список при текущих данных")
    void getAll_discountFalse_empty_ok() throws Exception {
        mockMvc.perform(get("/v1/products")
                        .param("discount", "false"))
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json("[]")
                );
    }

    @Test
    @DisplayName("GET /v1/products — фильтр по цене: minPrice=8, maxPrice=12 → только product1 (11.99)")
    void getAll_priceRange_ok() throws Exception {
        List<ProductShortResponseDto> expected = List.of(productShortResponseDto1);

        mockMvc.perform(get("/v1/products")
                        .param("minPrice", "8.00")
                        .param("maxPrice", "12.00")
                        .param("sortBy", "price")
                        .param("sortDirection", "false"))
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json(objectMapper.writeValueAsString(expected))
                );
    }

    @Test
    @DisplayName("GET /v1/products — minPrice > maxPrice → 400 Bad Request")
    void getAll_priceRange_invalid_badRequest() throws Exception {
        mockMvc.perform(get("/v1/products")
                        .param("minPrice", "20.00")
                        .param("maxPrice", "10.00"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("GET /v1/products — category=1 & minPrice=10 & sortBy=price DESC → product2, product1")
    void getAll_combinedFilters_ok() throws Exception {
        List<ProductShortResponseDto> expected = List.of(
                productShortResponseDto2, // 13.99
                productShortResponseDto1  // 11.99
        );

        mockMvc.perform(get("/v1/products")
                        .param("category", "1")
                        .param("minPrice", "10.00")
                        .param("sortBy", "price")
                        .param("sortDirection", "true"))
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json(objectMapper.writeValueAsString(expected))
                );
    }

    @Test
    @DisplayName("GET /v1/products/{productId} — positive")
    void getByIdPositiveCase() throws Exception {
        Long productId = 1L;

        mockMvc.perform(get("/v1/products/{productId}", productId))
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json(objectMapper.writeValueAsString(productResponseDto1))
                );
    }

    @Test
    @DisplayName("GET /v1/products/{productId} — negative: 404")
    void getByIdNegativeCase() throws Exception {
        Long productId = 999L;

        mockMvc.perform(get("/v1/products/{productId}", productId))
                .andDo(print())
                .andExpectAll(
                        status().isNotFound(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.exception").value("ProductNotFoundException"),
                        jsonPath("$.message").value("Product with id " + productId + " not found"),
                        jsonPath("$.status").value(HttpStatus.NOT_FOUND.value())
                );
    }

    @Test
    @DisplayName("POST /v1/products — create (ROLE_ADMIN)")
    void create_ok() throws Exception {
        mockMvc.perform(post("/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productCreateRequestDto))
                        .with(httpBasic("alice.johnson@example.com", "12345")))
                .andDo(print())
                .andExpectAll(
                        status().isCreated(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json(objectMapper.writeValueAsString(productResponseCreatedDto))
                );
    }

    @Test
    @DisplayName("PUT /v1/products/{id} — update (ROLE_ADMIN)")
    void update_ok() throws Exception {
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
                        content().json(objectMapper.writeValueAsString(productResponseUpdatedDto))
                );
    }

    @Test
    @DisplayName("DELETE /v1/products/{id} — delete (ROLE_ADMIN)")
    void delete_ok() throws Exception {
        Long productId = 2L;

        mockMvc.perform(delete("/v1/products/{productId}", productId)
                        .with(httpBasic("alice.johnson@example.com", "12345")))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("POST /v1/products/{id}/discount/{%} — set discount (ROLE_ADMIN)")
    void setDiscount_ok() throws Exception {
        Long productId = product1.getProductId();
        BigDecimal discountPercentage = new BigDecimal("20");
        BigDecimal originalPrice = product1.getPrice();

        BigDecimal discountPrice = originalPrice.subtract(
                originalPrice.multiply(discountPercentage)
                        .divide(new BigDecimal(100), 2, RoundingMode.HALF_UP)
        );

        ProductResponseDto expected = productResponseDto1.toBuilder()
                .discountPrice(discountPrice)
                .build();

        mockMvc.perform(post("/v1/products/{productId}/discount/{discountPercentage}", productId, discountPercentage)
                        .with(httpBasic("alice.johnson@example.com", "12345")))
                .andDo(print())
                .andExpectAll(
                        status().isAccepted(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json(objectMapper.writeValueAsString(expected))
                );
    }

    @Test
    @DisplayName("GET /v1/products/product-of-the-day — positive")
    void getProductOfTheDay_ok() throws Exception {
        ProductResponseDto expected = productResponseDto2;

        mockMvc.perform(get("/v1/products/product-of-the-day"))
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json(objectMapper.writeValueAsString(expected))
                );
    }
}


