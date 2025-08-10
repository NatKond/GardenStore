package de.telran.gardenStore.service;

import de.telran.gardenStore.AbstractTest;
import de.telran.gardenStore.dto.report.ProductReport;
import de.telran.gardenStore.dto.report.ProfitReport;
import de.telran.gardenStore.enums.OrderStatus;
import de.telran.gardenStore.service.report.ReportServiceImpl;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReportServiceImplTest extends AbstractTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private Query query;

    @InjectMocks
    private ReportServiceImpl reportService;

    @Test
    @DisplayName("Get most ordered products")
    void getTopOrderedProducts() {
        Integer limit = 3;
        List<Object[]> resultList = List.<Object[]>of(
                new Object[]{
                        3L,
                        "Slug & Snail Barrier Pellets",
                        BigDecimal.valueOf(5.75),
                        BigDecimal.valueOf(7.50),
                        2L,
                        "Pet-safe barrier pellets to protect plants from slugs",
                        "https://example.com/images/protection_slug_pellets.jpg",
                        1L
                });

        List<ProductReport> expected = List.of(ProductReport.builder()
                .product(productShortResponseDto3)
                .quantity(1L)
                .build());


        when(entityManager.createNativeQuery(anyString())).thenReturn(query);
        when(query.setParameter("status", OrderStatus.DELIVERED.name())).thenReturn(query);
        when(query.setParameter("limit", limit)).thenReturn(query);
        when(query.getResultList()).thenReturn(resultList);

        List<ProductReport> actual = reportService.getTopOrderedProducts(limit);

        assertNotNull(actual);
        assertEquals(expected.size(), actual.size());
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Get most canceled products")
    void getTopCanceledProducts() {
        Integer limit = 3;
        List<Object[]> resultList = List.<Object[]>of(
                new Object[]{
                        1L,
                        "All-Purpose Plant Fertilizer",
                        BigDecimal.valueOf(8.99),
                        BigDecimal.valueOf(11.99),
                        1L,
                        "Balanced NPK formula for all types of plants",
                        "https://example.com/images/fertilizer_all_purpose.jpg",
                        1L
                });

        List<ProductReport> expected = List.of(ProductReport.builder()
                .product(productShortResponseDto1)
                .quantity(1L)
                .build());

        when(entityManager.createNativeQuery(anyString())).thenReturn(query);
        when(query.setParameter("status", OrderStatus.CANCELLED.name())).thenReturn(query);
        when(query.setParameter("limit", limit)).thenReturn(query);
        when(query.getResultList()).thenReturn(resultList);

        List<ProductReport> actual = reportService.getTopCanceledProducts(limit);

        assertNotNull(actual);
        assertEquals(expected.size(), actual.size());
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Get products awaiting payment")
    void getProductsAwaitingPaymentForMoreDays() {
        int days = 7;
        int limit = 5;

        LocalDateTime fixedTime = LocalDateTime.of(2025, 8, 1, 12, 0);
        LocalDateTime timeBefore = fixedTime.minusDays(days);

        List<Object[]> resultList = List.<Object[]>of(
                new Object[]{
                        1L,
                        "All-Purpose Plant Fertilizer",
                        BigDecimal.valueOf(8.99),
                        BigDecimal.valueOf(11.99),
                        1L,
                        "Balanced NPK formula for all types of plants",
                        "https://example.com/images/fertilizer_all_purpose.jpg",
                        1L
                });

        List<ProductReport> expected = List.of(ProductReport.builder()
                .product(productShortResponseDto1)
                .quantity(1L)
                .build());

        when(entityManager.createNativeQuery(anyString())).thenReturn(query);
        when(query.setParameter(eq("timeBefore"), any(LocalDateTime.class))).thenReturn(query);
        when(query.setParameter("limit", limit)).thenReturn(query);
        when(query.getResultList()).thenReturn(resultList);

        List<ProductReport> actual = reportService.getProductsAwaitingPaymentForMoreDays(days, limit);

        assertNotNull(actual);
        assertEquals(expected.size(), actual.size());
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Getting profit report grouped by months")
    void getProfitOverPeriod_GroupByMonth_ShouldReturnList() {

        String timeUnit = "months";
        Integer timeAmount = 6;
        String groupBy = "month";
        LocalDateTime start = LocalDateTime.now().minusMonths(timeAmount);

        List<Object[]> resultList = List.<Object[]>of(
                new Object[]{
                        BigDecimal.valueOf(11.50),
                        "2025-05",
                        "2023-05 W1",
                        "2023-05-05",
                        "2023-05-05 17:00"}
        );

        List<ProfitReport> expected = List.of(
                ProfitReport.builder()
                        .period("2025-05")
                        .amountPerPeriod(BigDecimal.valueOf(11.50))
                        .build()
        );

        when(entityManager.createNativeQuery(anyString())).thenReturn(query);
        when(query.setParameter(eq("timeBefore"), any(LocalDateTime.class))).thenReturn(query);
        when(query.getResultList()).thenReturn(resultList);

        List<ProfitReport> actual = reportService.getProfitOverPeriod(timeUnit, timeAmount, groupBy);

        assertNotNull(actual);
        assertEquals(expected.size(), actual.size());
        assertEquals(expected, actual);
    }
}