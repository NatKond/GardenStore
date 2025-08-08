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
import java.util.ArrayList;
import java.util.Arrays;
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
    @DisplayName("Getting top N most ordered products")
    void getTopOrderedProducts_ShouldReturnList() {
        int limit = 3;
        List<Object[]> resultList = List.of(
                new Object[]{1L, "Product 1", BigDecimal.valueOf(10), BigDecimal.valueOf(8), 1L, "Desc 1", "url1", 5L},
                new Object[]{2L, "Product 2", BigDecimal.valueOf(20), BigDecimal.valueOf(15), 2L, "Desc 2", "url2", 3L}
        );

        when(entityManager.createNativeQuery(anyString())).thenReturn(query);
        when(query.setParameter("status", OrderStatus.DELIVERED.name())).thenReturn(query);
        when(query.setParameter("limit", limit)).thenReturn(query);
        when(query.getResultList()).thenReturn(resultList);

        List<ProductReport> reports = reportService.getTopOrderedProducts(limit);

        assertNotNull(reports);
        assertEquals(2, reports.size());
        assertEquals(1L, reports.get(0).getProduct().getProductId());
        assertEquals(5L, reports.get(0).getQuantity());
        verify(entityManager).createNativeQuery(anyString());
    }
    @Test
    @DisplayName("Getting top N most canceled products")
    void getTopCanceledProducts_ShouldReturnList() {
        int limit = 2;
        Object[] row1 = new Object[]{3L, "Product 3", BigDecimal.valueOf(30), BigDecimal.valueOf(25), 3L, "Desc 3", "url3", 4L};
        List<Object[]> resultList = new ArrayList<>();
        resultList.add(row1);

        when(entityManager.createNativeQuery(anyString())).thenReturn(query);
        when(query.setParameter("status", OrderStatus.CANCELLED.name())).thenReturn(query);
        when(query.setParameter("limit", limit)).thenReturn(query);
        when(query.getResultList()).thenReturn(resultList);

        List<ProductReport> reports = reportService.getTopCanceledProducts(limit);

        assertNotNull(reports);
        assertEquals(1, reports.size());
        assertEquals(3L, reports.get(0).getProduct().getProductId());
        assertEquals(4L, reports.get(0).getQuantity());
    }

    @Test
    @DisplayName("Getting products awaiting payment for more than N days")
    void getProductsAwaitingPaymentForMoreDays_ShouldReturnList() {

        int days = 7;
        int limit = 5;

        LocalDateTime fixedTime = LocalDateTime.of(2023, 1, 1, 12, 0);
        LocalDateTime timeBefore = fixedTime.minusDays(days);

        List<Object[]> resultList = List.of(
                new Object[]{1L, "Product 1", BigDecimal.valueOf(10), BigDecimal.valueOf(8), 1L, "Desc 1", "url1", 2L},
                new Object[]{2L, "Product 2", BigDecimal.valueOf(20), BigDecimal.valueOf(15), 2L, "Desc 2", "url2", 1L}
        );


        when(entityManager.createNativeQuery(anyString())).thenReturn(query);
        when(query.setParameter(eq("timeBefore"), any(LocalDateTime.class))).thenReturn(query);
        when(query.setParameter("limit", limit)).thenReturn(query);
        when(query.getResultList()).thenReturn(resultList);

        List<ProductReport> reports = reportService.getProductsAwaitingPaymentForMoreDays(days, limit);

        assertNotNull(reports);
        assertEquals(2, reports.size());
        assertEquals(1L, reports.get(0).getProduct().getProductId());
        assertEquals(2L, reports.get(0).getQuantity());
    }
    @Test
    @DisplayName("Getting profit report grouped by months")
    void getProfitOverPeriod_GroupByMonth_ShouldReturnList() {

        String timeUnit = "months";
        Integer timeAmount = 6;
        String groupBy = "month";
        LocalDateTime start = LocalDateTime.now().minusMonths(timeAmount);

        List<Object[]> resultList = Arrays.asList(
                new Object[]{BigDecimal.valueOf(1000), "2023-01", "2023-01 W1", "2023-01-01", "2023-01-01 12:00"},
                new Object[]{BigDecimal.valueOf(1500), "2023-02", "2023-02 W1", "2023-02-01", "2023-02-01 12:00"}
        );

        when(entityManager.createNativeQuery(anyString())).thenReturn(query);
        when(query.setParameter(eq("timeBefore"), any(LocalDateTime.class))).thenReturn(query);
        when(query.getResultList()).thenReturn(resultList);

        List<ProfitReport> reports = reportService.getProfitOverPeriod(timeUnit, timeAmount, groupBy);

        assertNotNull(reports);
        assertEquals(2, reports.size());
        assertEquals("2023-01", reports.get(0).getPeriod());
        assertEquals(BigDecimal.valueOf(1000), reports.get(0).getAmountPerPeriod());
    }

    @Test
    @DisplayName("Getting profit report with unsupported period")
    void getProfitOverPeriod_UnsupportedTimeUnit_ShouldUseDefault() {

        String timeUnit = "unsupported";
        int timeAmount = 1;
        String groupBy = "day";

        when(entityManager.createNativeQuery(anyString())).thenReturn(query);
        when(query.setParameter(anyString(), any())).thenReturn(query);
        when(query.getResultList()).thenReturn(List.of());

        List<ProfitReport> reports = reportService.getProfitOverPeriod(timeUnit, timeAmount, groupBy);

        assertNotNull(reports);
        assertTrue(reports.isEmpty());
    }
}