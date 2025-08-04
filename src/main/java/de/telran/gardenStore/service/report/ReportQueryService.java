package de.telran.gardenStore.service.report;

import de.telran.gardenStore.dto.report.ProductReport;
import de.telran.gardenStore.dto.report.ProfitReport;
import de.telran.gardenStore.enums.OrderStatus;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportQueryService {

    private final EntityManager entityManager;

    private final ReportMapper reportMapper;

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
        return getProductReports(limit, nativeQuery);
    }

    public List<ProductReport> getProductsAwaitingPaymentByTime(LocalDateTime timeBefore, Integer limit) {
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
        return getProductReports(limit, nativeQuery);
    }

    public List<ProfitReport> getProfitGroupBy(LocalDateTime timeBefore, String groupBy) {
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

        Integer rowNumber = switch (groupBy) {
            case "month" -> 1;
            case "week" -> 2;
            case "day" -> 3;
            default -> 4;
        };

        Map<String, BigDecimal> groupedMap = resultList.stream()
                .collect(Collectors.groupingBy(raw -> String.valueOf(raw[rowNumber]),
                        LinkedHashMap::new,
                        Collectors.reducing(BigDecimal.ZERO, raw -> (BigDecimal) raw[0], BigDecimal::add)));

        return groupedMap.entrySet().stream()
                .map((entry) ->
                        ProfitReport.builder()
                                .period(entry.getKey())
                                .amountPerPeriod(entry.getValue())
                                .build())
                .collect(Collectors.toList());
    }

    private List<ProductReport> getProductReports(Integer limit, Query nativeQuery) {
        nativeQuery.setParameter("limit", limit);
        List<Object[]> resultList = (List<Object[]>) nativeQuery.getResultList();
        return resultList
                .stream()
                .map(reportMapper::mapToProductReport)
                .collect(Collectors.toList());
    }

}
