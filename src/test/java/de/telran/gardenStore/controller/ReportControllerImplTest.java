package de.telran.gardenStore.controller;

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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.math.BigDecimal;
import java.util.List;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ReportControllerImpl.class)
@AutoConfigureMockMvc
class ReportControllerImplTest extends AbstractTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ReportService reportService;

    @MockitoBean
    private JwtService jwtService;

    @Test
    @DisplayName("GET /v1/report/top-purchased-products/{limit} - Getting top N most ordered products")
    @WithMockUser(roles = "ADMIN")
    void getTopOrderedProducts_ShouldReturnList() throws Exception {
        int limit = 3;
        ProductReport report = ProductReport.builder()
                .product(productShortResponseDto1)
                .quantity(5L)
                .build();

        when(reportService.getTopOrderedProducts(limit)).thenReturn(List.of(report));

        mockMvc.perform(get("/v1/report/top-purchased-products/{limit}", limit))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].product.productId").value(report.getProduct().getProductId()))
                .andExpect(jsonPath("$[0].quantity").value(report.getQuantity()));
    }

    @Test
    @DisplayName("GET /v1/report/top-canceled-products/{limit} - Getting top N most canceled products")
    @WithMockUser(roles = "ADMIN")
    void getTopCanceledProducts_ShouldReturnList() throws Exception {
        int limit = 2;
        ProductReport report = ProductReport.builder()
                .product(productShortResponseDto2)
                .quantity(3L)
                .build();

        when(reportService.getTopCanceledProducts(limit)).thenReturn(List.of(report));

        mockMvc.perform(get("/v1/report/top-canceled-products/{limit}", limit))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].product.productId").value(report.getProduct().getProductId()));
    }

    @Test
    @DisplayName("GET /v1/report/awaiting-payment-products - Getting products awaiting payment")
    @WithMockUser(roles = "ADMIN")
    void getProductsAwaitingPaymentForDays_ShouldReturnList() throws Exception {
        int days = 5;
        int limit = 10;
        ProductReport report = ProductReport.builder()
                .product(productShortResponseDto3)
                .quantity(2L)
                .build();

        when(reportService.getProductsAwaitingPaymentForMoreDays(days, limit)).thenReturn(List.of(report));

        mockMvc.perform(get("/v1/report/awaiting-payment-products")
                        .param("days", String.valueOf(days))
                        .param("limit", String.valueOf(limit)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].quantity").value(report.getQuantity()));
    }

    @Test
    @DisplayName("GET /v1/report/profit - Getting profit report")
    @WithMockUser(roles = "ADMIN")
    void getProfitOverPeriod_ShouldReturnList() throws Exception {
        String timeUnit = "months";
        int timeAmount = 12;
        String groupBy = "month";

        ProfitReport report = ProfitReport.builder()
                .period("2023-01")
                .amountPerPeriod(BigDecimal.valueOf(1000))
                .build();

        when(reportService.getProfitOverPeriod(timeUnit, timeAmount, groupBy)).thenReturn(List.of(report));

        mockMvc.perform(get("/v1/report/profit")
                        .param("timeUnit", timeUnit)
                        .param("timeAmount", String.valueOf(timeAmount))
                        .param("groupBy", groupBy))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].period").value(report.getPeriod()))
                .andExpect(jsonPath("$[0].amountPerPeriod").value(report.getAmountPerPeriod()));
    }
    @Test
    @DisplayName("GET /v1/report/* - Access denied without ADMIN role")
    void accessDeniedWithoutAdminRole() throws Exception {
        mockMvc.perform(get("/v1/report/top-purchased-products/3"))
                .andExpect(status().isForbidden());
    }
}