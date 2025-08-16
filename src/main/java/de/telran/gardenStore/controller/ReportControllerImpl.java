package de.telran.gardenStore.controller;

import de.telran.gardenStore.dto.report.ProductReport;
import de.telran.gardenStore.dto.report.ProfitReport;
import de.telran.gardenStore.service.report.ReportService;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@RequestMapping("/v1/report")
public class ReportControllerImpl implements ReportController {

    private final ReportService reportService;

    @Override
    @GetMapping("/top-purchased-products/{limit}")
    public List<ProductReport> getTopPurchasedProducts(@PathVariable @Positive Integer limit) {
        return reportService.getTopPurchasedProducts(limit);
    }

    @Override
    @GetMapping("/top-canceled-products/{limit}")
    public List<ProductReport> getTopCanceledProducts(@PathVariable @Positive Integer limit) {
        return reportService.getTopCanceledProducts(limit);
    }

    @Override
    @GetMapping("/awaiting-payment-products")
    public List<ProductReport> getProductsAwaitingPaymentForDays(@RequestParam @Positive Integer days,
                                                                 @RequestParam @Positive Integer limit) {
        return reportService.getProductsAwaitingPaymentForMoreDays(days, limit);
    }

    @Override
    @GetMapping("/profit")
    public List<ProfitReport> getProfitOverPeriod(@RequestParam @Pattern(regexp = "days|months|years") String timeUnit,
                                                  @RequestParam @Positive Integer timeAmount,
                                                  @RequestParam @Pattern(regexp = "hour|day|week|month") String groupBy) {
        return reportService.getProfitOverPeriod(timeUnit, timeAmount, groupBy);
    }
}
