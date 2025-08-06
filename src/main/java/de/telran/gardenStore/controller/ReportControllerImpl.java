package de.telran.gardenStore.controller;

import de.telran.gardenStore.dto.report.ProductReport;
import de.telran.gardenStore.dto.report.ProfitReport;
import de.telran.gardenStore.service.report.ReportService;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@Validated
public class ReportControllerImpl implements ReportController {

    private final ReportService reportService;

    @Override
    public List<ProductReport> getTopOrderedProducts(@Positive Integer limit) {
        return reportService.getTopOrderedProducts(limit);
    }

    @Override
    public List<ProductReport> getTopCanceledProducts(@Positive Integer limit) {
        return reportService.getTopCanceledProducts(limit);
    }

    @Override
    public List<ProductReport> getProductsAwaitingPaymentForDays(@Positive Integer days, @Positive Integer limit) {
        return reportService.getProductsAwaitingPaymentForMoreDays(days, limit);
    }

    @Override
    public List<ProfitReport> getProfitOverPeriod(@Pattern(regexp = "days|months|years") String timeUnit,
                                                  @Positive Integer timeAmount,
                                                  @Pattern(regexp = "hour|day|week|month") String groupBy) {
        return reportService.getProfitOverPeriod(timeUnit, timeAmount, groupBy);
    }
}
