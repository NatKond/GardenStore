package de.telran.gardenStore.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.telran.gardenStore.AbstractTest;
import de.telran.gardenStore.dto.report.ProductReport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(printOnlyOnFailure = false)
public class ReportIntegrationTest extends AbstractTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("GET /v1/report/top-purchased-products/{limit} - Get most ordered products")
    void getTopOrderedProducts() throws Exception {
        Integer limit = 3;
        List<ProductReport> expected = List.of(ProductReport.builder()
                .product(productShortResponseDto3)
                .quantity(1L)
                .build());

        mockMvc.perform(get("/v1/report/top-purchased-products/{limit}", String.valueOf(limit))
                        .with(httpBasic("alice.johnson@example.com", "12345")))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json(objectMapper.writeValueAsString(expected)));
    }

    @Test
    @DisplayName("GET /v1/report/top-canceled-products/{limit} - Get most canceled products")
    void getTopCanceledProducts_ShouldReturnList() throws Exception {
        Integer limit = 2;
        List<ProductReport> expected = List.of(ProductReport.builder()
                .product(productShortResponseDto1)
                .quantity(1L)
                .build());

        mockMvc.perform(get("/v1/report/top-canceled-products/{limit}", limit)
                        .with(httpBasic("alice.johnson@example.com", "12345")))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json(objectMapper.writeValueAsString(expected)));
    }

    @Test
    @DisplayName("GET /v1/report/awaiting-payment-products - Get products awaiting payment")
    void getProductsAwaitingPaymentForDays_ShouldReturnList() throws Exception {
        Integer days = 5;
        Integer limit = 10;
        List<ProductReport> expected = List.of(
                ProductReport.builder()
                        .product(productShortResponseDto1)
                        .quantity(1L)
                        .build(),
                ProductReport.builder()
                        .product(productShortResponseDto3)
                        .quantity(1L)
                        .build());

        mockMvc.perform(get("/v1/report/awaiting-payment-products")
                        .param("days", String.valueOf(days))
                        .param("limit", String.valueOf(limit))
                        .with(httpBasic("alice.johnson@example.com", "12345")))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json(objectMapper.writeValueAsString(expected)));
    }
}
