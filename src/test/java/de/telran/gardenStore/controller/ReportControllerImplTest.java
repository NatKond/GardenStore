package de.telran.gardenStore.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.telran.gardenStore.AbstractTest;
import de.telran.gardenStore.dto.report.ProductReport;
import de.telran.gardenStore.dto.report.ProfitReport;
import de.telran.gardenStore.service.report.ReportService;
import de.telran.gardenStore.service.security.JwtService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.math.BigDecimal;
import java.util.List;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ReportControllerImpl.class)
@AutoConfigureMockMvc(addFilters = false)
class ReportControllerImplTest extends AbstractTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ReportService reportService;

    @MockitoBean
    private JwtService jwtService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("GET /v1/report/top-purchased-products/{limit} - Get most ordered products")
    void getTopPurchasedProducts() throws Exception {
        int limit = 3;
        List<ProductReport> expected = List.of(ProductReport.builder()
                .product(productShortResponseDto3)
                .quantity(1L)
                .build());

        when(reportService.getTopPurchasedProducts(limit)).thenReturn(expected);

        mockMvc.perform(get("/v1/report/top-purchased-products/{limit}", limit))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json(objectMapper.writeValueAsString(expected)));
    }

    @Test
    @DisplayName("GET /v1/report/top-canceled-products/{limit} - Get most canceled products")

    void getTopCanceledProducts() throws Exception {
        int limit = 2;
        List<ProductReport> expected = List.of(ProductReport.builder()
                .product(productShortResponseDto1)
                .quantity(1L)
                .build());

        when(reportService.getTopCanceledProducts(limit)).thenReturn(expected);

        mockMvc.perform(get("/v1/report/top-canceled-products/{limit}", limit))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json(objectMapper.writeValueAsString(expected)));
    }

    @Test
    @DisplayName("GET /v1/report/awaiting-payment-products - Get products awaiting payment")
    void getProductsAwaitingPaymentForDays() throws Exception {
        int days = 5;
        int limit = 10;
        List<ProductReport> expected = List.of(ProductReport.builder()
                .product(productShortResponseDto1)
                .quantity(1L)
                .build());

        when(reportService.getProductsAwaitingPaymentForMoreDays(days, limit)).thenReturn(expected);

        mockMvc.perform(get("/v1/report/awaiting-payment-products")
                        .param("days", String.valueOf(days))
                        .param("limit", String.valueOf(limit)))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json(objectMapper.writeValueAsString(expected)));
    }

    @Test
    @DisplayName("GET /v1/report/profit - Get profit report")
    void getProfitOverPeriod() throws Exception {
        String timeUnit = "months";
        int timeAmount = 6;
        String groupBy = "month";

        List<ProfitReport> expected = List.of(
                ProfitReport.builder()
                        .period("2025-05")
                        .amountPerPeriod(BigDecimal.valueOf(11.50))
                        .build()
        );

        when(reportService.getProfitOverPeriod(timeUnit, timeAmount, groupBy)).thenReturn(expected);

        mockMvc.perform(get("/v1/report/profit")
                        .param("timeUnit", timeUnit)
                        .param("timeAmount", String.valueOf(timeAmount))
                        .param("groupBy", groupBy))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json(objectMapper.writeValueAsString(expected)));
    }
}