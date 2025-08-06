package de.telran.gardenStore.controller;

import de.telran.gardenStore.dto.report.ProductReport;
import de.telran.gardenStore.dto.report.ProfitReport;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequestMapping("/v1/report")
@PreAuthorize("hasRole('ADMIN')")
public interface ReportController {

    @GetMapping("/top-purchased-products/{limit}")
    List<ProductReport> getTopOrderedProducts(@PathVariable @Positive Integer limit);

    @GetMapping("/top-canceled-products/{limit}")
    List<ProductReport> getTopCanceledProducts(@PathVariable @Positive Integer limit);

    @GetMapping("/awaiting-payment-products")
    List<ProductReport> getProductsAwaitingPaymentForDays(@RequestParam @Positive Integer days, @RequestParam @Positive Integer limit);

    @GetMapping("/profit")
    List<ProfitReport> getProfitOverPeriod(@RequestParam @Pattern(regexp = "days|months|years") String timeUnit,
                                           @RequestParam @Positive Integer timeAmount,
                                           @RequestParam @Pattern(regexp = "hour|day|week|month") String groupBy);
}
