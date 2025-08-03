package de.telran.gardenStore.controller;

import de.telran.gardenStore.dto.report.ProductReport;
import de.telran.gardenStore.dto.report.ProfitReport;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface ReportController {

    List<ProductReport> getTopOrderedProducts(@PathVariable @Positive Integer limit);

    List<ProductReport> getTopCanceledProducts(@PathVariable @Positive Integer limit);

    List<ProductReport> getProductsAwaitingPaymentForDays(@RequestParam @Positive Integer days, @RequestParam @Positive Integer limit);

    List<ProfitReport> getProfitOverPeriod(@Pattern(regexp = "days|months|years") String timeUnit,
                                           @Positive Integer timeAmount,
                                           @Pattern(regexp = "hour|day|week|month") String groupBy);
}
