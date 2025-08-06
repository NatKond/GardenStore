package de.telran.gardenStore.service.report;

import de.telran.gardenStore.converter.ReportMapper;
import de.telran.gardenStore.dto.report.ProductReport;
import de.telran.gardenStore.dto.report.ProfitReport;

import de.telran.gardenStore.enums.OrderStatus;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private static final int MONTH_INDEX = 1;

    private static final int WEEK_INDEX  = 2;

    private static final int DAY_INDEX   = 3;

    private static final int HOUR_INDEX  = 4;

    private final EntityManager entityManager;

    private final ReportMapper reportMapper;

    @Override
    public List<ProductReport> getTopOrderedProducts(Integer limit) {
        return getTopProductsByStatus(OrderStatus.DELIVERED, limit);
    }

    @Override
    public List<ProductReport> getTopCanceledProducts(Integer limit) {
        return getTopProductsByStatus(OrderStatus.CANCELLED, limit);
    }

    @Override
    public List<ProductReport> getProductsAwaitingPaymentForMoreDays(Integer days, Integer limit) {
        return getProductsAwaitingPaymentByTime(LocalDateTime.now().minusDays(days), limit);
    }

    @Override
    public List<ProfitReport> getProfitOverPeriod(String timeUnit, Integer timeAmount, String groupBy) {
        LocalDateTime start = switch (timeUnit) {
            case "days" -> LocalDateTime.now().minusDays(timeAmount);
            case "months" -> LocalDateTime.now().minusMonths(timeAmount);
            case "years" -> LocalDateTime.now().minusYears(timeAmount);
            default -> LocalDateTime.now().minusYears(1);
        };
        return getProfitReports(start, groupBy);
    }

    public List<ProductReport> getTopProductsByStatus(OrderStatus status, Integer limit) {
        String query = """
                SELECT p.product_id, p.name, p.discount_price, p.price, p.category_id, p.description, p.image_url, count(p.product_id)
                FROM products p
                JOIN order_items oi on p.product_id = oi.product_id
                JOIN orders o on oi.order_id = o.order_id AND status = :status
                GROUP BY p.product_id, p.name, p.discount_price, p.price, p.category_id, p.description, p.image_url
                ORDER BY count(p.product_id) DESC
                LIMIT :limit;
                """;
        Query nativeQuery = entityManager.createNativeQuery(query);
        nativeQuery.setParameter("status", status.name());
        nativeQuery.setParameter("limit", limit);
        List<Object[]> resultList = (List<Object[]>) nativeQuery.getResultList();
        return reportMapper.mapToProductReportsList(resultList);
    }

    private List<ProductReport> getProductsAwaitingPaymentByTime(LocalDateTime timeBefore, Integer limit) {
        String query = """
                SELECT p.product_id, p.name, p.discount_price, p.price, p.category_id, p.description, p.image_url, count(p.product_id)
                FROM products p
                JOIN order_items oi on p.product_id = oi.product_id
                JOIN orders o on oi.order_id = o.order_id AND o.status = 'AWAITING_PAYMENT' AND o.updated_at < :timeBefore
                GROUP BY p.product_id, p.name, p.discount_price, p.price, p.category_id, p.description, p.image_url
                ORDER BY count(p.product_id) DESC
                LIMIT :limit;
                """;
        Query nativeQuery = entityManager.createNativeQuery(query);
        nativeQuery.setParameter("timeBefore", timeBefore);
        nativeQuery.setParameter("limit", limit);
        List<Object[]> resultList = (List<Object[]>) nativeQuery.getResultList();
        return reportMapper.mapToProductReportsList(resultList);
    }

    private List<ProfitReport> getProfitReports(LocalDateTime timeBefore, String groupBy) {
        String query = """
                SELECT sum(oi.price_at_purchase * oi.quantity),
                TO_CHAR(o.updated_at, 'YYYY-MM') AS month,
                TO_CHAR(o.updated_at, 'YYYY-MM "W"') || TO_CHAR(o.updated_at, 'W') AS week,
                TO_CHAR(o.updated_at, 'YYYY-MM-DD')AS day,
                TO_CHAR(o.updated_at, 'YYYY-MM-DD HH24:00') AS hour
                FROM orders o
                JOIN order_items oi ON o.order_id = oi.order_id
                                     AND o.status = 'DELIVERED'
                                     AND o.updated_at > :timeBefore
                GROUP BY month, week, day, hour
                ORDER BY month;
                """;
        Query nativeQuery = entityManager.createNativeQuery(query);
        nativeQuery.setParameter("timeBefore", timeBefore);
        List<Object[]> resultList = (List<Object[]>) nativeQuery.getResultList();

        return switch (groupBy) {
            case "month" -> reportMapper.mapToProfitReportsList(resultList, MONTH_INDEX);
            case "week"  -> reportMapper.mapToProfitReportsList(resultList, WEEK_INDEX);
            case "day"   -> reportMapper.mapToProfitReportsList(resultList, DAY_INDEX);
            default      -> reportMapper.mapToProfitReportsList(resultList, HOUR_INDEX);
        };
    }
}
