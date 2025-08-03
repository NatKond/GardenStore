package de.telran.gardenStore.service;

import de.telran.gardenStore.dto.ProductShortResponseDto;
import de.telran.gardenStore.dto.report.ProductReport;
import de.telran.gardenStore.dto.report.ProfitReport;

import de.telran.gardenStore.enums.OrderStatus;
import de.telran.gardenStore.repository.ProductRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final ProductRepository productRepository;

    private final EntityManager entityManager;

    @Override
    public List<ProductReport> getTopOrderedProducts(Integer limit) {
        //return getTopProductsByStatus(OrderStatus.DELIVERED, limit);
        return productRepository.getTopWithStatus(OrderStatus.DELIVERED, limit);
    }

    @Override
    public List<ProductReport> getTopCanceledProducts(Integer limit) {
        //return getTopProductsByStatus(OrderStatus.CANCELLED, limit);
        return productRepository.getTopWithStatus(OrderStatus.CANCELLED, limit);
    }

    @Override
    public List<ProductReport> getProductsAwaitingPaymentForMoreDays(Integer days, Integer limit) {
        //return getProductsAwaitingPaymentByTime(LocalDateTime.now().minusDays(days), limit);
        return productRepository.getAwaitingPaymentForDays(LocalDateTime.now().minusDays(days), limit, OrderStatus.AWAITING_PAYMENT);
    }

    @Override
    public List<ProfitReport> getProfitOverPeriod(String timeUnit, Integer timeAmount, String groupBy) {
        LocalDateTime start = switch (timeUnit) {
            case "days" -> LocalDateTime.now().minusDays(timeAmount);
            case "months" -> LocalDateTime.now().minusMonths(timeAmount);
            case "years" -> LocalDateTime.now().minusYears(timeAmount);
            default -> LocalDateTime.now().minusYears(1);
        };
        return getProfitGroupBy(start, groupBy);
    }

    private List<ProductReport> getTopProductsByStatus(OrderStatus status, Integer limit) {
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
        return getProductReports(limit, nativeQuery);
    }

    private List<ProductReport> getProductReports(Integer limit, Query nativeQuery) {
        nativeQuery.setParameter("limit", limit);
        List<Object[]> resultList = (List<Object[]>) nativeQuery.getResultList();
        return resultList
                .stream()
                .map(row ->
                        ProductReport.builder()
                                .product(
                                        ProductShortResponseDto.builder()
                                                .productId((Long) row[0])
                                                .name((String) row[1])
                                                .discountPrice((BigDecimal) row[2])
                                                .price((BigDecimal) row[3])
                                                .categoryId((Long) row[4])
                                                .description((String) row[5])
                                                .imageUrl((String) row[6])
                                                .build()
                                )
                                .quantity((Long) row[7])
                                .build())
                .collect(Collectors.toList());
    }

    private List<ProfitReport> getProfitGroupBy(LocalDateTime timeBefore, String groupBy) {
        String query = """
                SELECT sum(sales.total_amout),
                       extract(years from sales.updated_at) as year,
                       extract(months from sales.updated_at) as month,
                       extract(weeks from sales.updated_at) as week,
                       extract(days from sales.updated_at) as day,
                       extract(hours from sales.updated_at) as hour
                FROM (SELECT sum(oi.price_at_purchase * oi.quantity) as total_amout, o.updated_at
                      FROM orders o
                               JOIN order_items oi
                                    on o.order_id = oi.order_id AND o.status = 'DELIVERED' AND o.updated_at > :timeBefore
                      GROUP BY o.order_id
                      ORDER BY o.order_id) as sales
                GROUP BY year, month, week, day, hour
                ORDER BY month;
                """;
        Query nativeQuery = entityManager.createNativeQuery(query);
        nativeQuery.setParameter("timeBefore", timeBefore);
        List<Object[]> resultList = (List<Object[]>) nativeQuery.getResultList();
        Map<String, BigDecimal> groupedMap = switch (groupBy) {
            case "month" -> groupByMonths(resultList);
            case "week" -> groupByWeeks(resultList);
            case "day" -> groupByDays(resultList);
            default -> groupByHours(resultList);
        };
        return groupedMap.entrySet().stream()
                .map((entry) ->
                        ProfitReport.builder()
                                .period(entry.getKey())
                                .amountPerPeriod(entry.getValue())
                                .build())
                .sorted(Comparator.comparing(ProfitReport::getPeriod))
                .collect(Collectors.toList());
    }

    private Map<String, BigDecimal> groupByMonths(List<Object[]> resultList) {
        return resultList.stream().collect(Collectors.groupingBy(raw ->
                        String.format("%s-%02d",
                                raw[1],
                                ((BigDecimal) raw[2]).intValue()),
                Collectors.reducing(BigDecimal.ZERO, raw -> (BigDecimal) raw[0], BigDecimal::add)));
    }

    private Map<String, BigDecimal> groupByWeeks(List<Object[]> resultList) {
        return resultList.stream().collect(Collectors.groupingBy(raw ->
                        String.format("%s-%02d week %02d",
                                raw[1],
                                ((BigDecimal) raw[2]).intValue(),
                                ((BigDecimal) raw[3]).intValue()),
                Collectors.reducing(BigDecimal.ZERO, raw -> (BigDecimal) raw[0], BigDecimal::add)));
    }

    private Map<String, BigDecimal> groupByDays(List<Object[]> resultList) {
        return resultList.stream().collect(Collectors.groupingBy(raw ->
                        String.format("%s-%02d-%02d",
                                raw[1],
                                ((BigDecimal) raw[2]).intValue(),
                                ((BigDecimal) raw[4]).intValue()),
                Collectors.reducing(BigDecimal.ZERO, raw -> (BigDecimal) raw[0], BigDecimal::add)));
    }

    private Map<String, BigDecimal> groupByHours(List<Object[]> resultList) {
        return resultList.stream().collect(Collectors.groupingBy(raw ->
                        String.format("%s-%02d-%02d %02d:00",
                                raw[1],
                                ((BigDecimal) raw[2]).intValue(),
                                ((BigDecimal) raw[4]).intValue(),
                                ((BigDecimal) raw[5]).intValue()),
                Collectors.reducing(BigDecimal.ZERO, raw -> (BigDecimal) raw[0], BigDecimal::add)));
    }
}
