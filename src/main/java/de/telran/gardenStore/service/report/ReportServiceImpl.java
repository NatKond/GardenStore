package de.telran.gardenStore.service.report;

import de.telran.gardenStore.dto.report.ProductReport;
import de.telran.gardenStore.dto.report.ProfitReport;

import de.telran.gardenStore.enums.OrderStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final ReportQueryService reportQueryService;

    @Override
    public List<ProductReport> getTopOrderedProducts(Integer limit) {
        return reportQueryService.getTopProductsByStatus(OrderStatus.DELIVERED, limit);
    }

    @Override
    public List<ProductReport> getTopCanceledProducts(Integer limit) {
        return reportQueryService.getTopProductsByStatus(OrderStatus.CANCELLED, limit);
    }

    @Override
    public List<ProductReport> getProductsAwaitingPaymentForMoreDays(Integer days, Integer limit) {
        return reportQueryService.getProductsAwaitingPaymentByTime(LocalDateTime.now().minusDays(days), limit);
    }

    @Override
    public List<ProfitReport> getProfitOverPeriod(String timeUnit, Integer timeAmount, String groupBy) {
        LocalDateTime start = switch (timeUnit) {
            case "days" -> LocalDateTime.now().minusDays(timeAmount);
            case "months" -> LocalDateTime.now().minusMonths(timeAmount);
            case "years" -> LocalDateTime.now().minusYears(timeAmount);
            default -> LocalDateTime.now().minusYears(1);
        };
        return reportQueryService.getProfitGroupBy(start, groupBy);
    }
}
