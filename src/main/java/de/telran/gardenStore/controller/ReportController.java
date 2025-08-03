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
public interface ReportController {

    @GetMapping("/top-purchased-products/{limit}")
    @PreAuthorize("hasRole('ADMIN')")
    List<ProductReport> getTopOrderedProducts(@PathVariable @Positive Integer limit);

    @GetMapping("/top-canceled-products/{limit}")
    @PreAuthorize("hasRole('ADMIN')")
    List<ProductReport> getTopCanceledProducts(@PathVariable @Positive Integer limit);

    @GetMapping("/awaiting-payment-products")
    @PreAuthorize("hasRole('ADMIN')")
    List<ProductReport> getProductsAwaitingPaymentForDays(@RequestParam @Positive Integer days, @RequestParam @Positive Integer limit);

    @GetMapping("/profit")
    @PreAuthorize("hasRole('ADMIN')")
    List<ProfitReport> getProfitOverPeriod(@RequestParam @Pattern(regexp = "days|months|years") String timeUnit,
                                           @RequestParam @Positive Integer timeAmount,
                                           @RequestParam @Pattern(regexp = "hour|day|week|month") String groupBy);
}
