package de.telran.gardenStore.controller;

import de.telran.gardenStore.converter.Converter;
import de.telran.gardenStore.dto.ProductCreateRequestDto;
import de.telran.gardenStore.dto.ProductResponseDto;
import de.telran.gardenStore.dto.ProductShortResponseDto;
import de.telran.gardenStore.dto.report.ProductReport;
import de.telran.gardenStore.dto.report.ProfitReport;
import de.telran.gardenStore.entity.Product;
import de.telran.gardenStore.service.ReportService;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/v1/report")
@RequiredArgsConstructor
@RestController
@Validated
public class ReportControllerImpl implements ReportController {

    private final ReportService reportService;

    private final Converter<Product, ProductCreateRequestDto, ProductResponseDto, ProductShortResponseDto> productConverter;

    @GetMapping("/top-purchased-products/{limit}")
    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public List<ProductReport> getTopOrderedProducts(@Positive Integer limit) {
        return reportService.getTopOrderedProducts(limit);
    }

    @GetMapping("/top-canceled-products/{limit}")
    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public List<ProductReport> getTopCanceledProducts(@Positive Integer limit) {
        return reportService.getTopCanceledProducts(limit);
    }

    @GetMapping("/awaiting-payment-products")
    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public List<ProductReport> getProductsAwaitingPaymentForDays(@Positive Integer days, @Positive Integer limit) {
        return reportService.getProductsAwaitingPaymentForMoreDays(days, limit);
    }

    @GetMapping("/profit")
    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public List<ProfitReport> getProfitOverPeriod(@RequestParam @Pattern(regexp = "days|months|years") String timeUnit,
                                                  @RequestParam @Positive Integer timeAmount,
                                                  @RequestParam @Pattern(regexp = "hour|day|week|month") String groupBy) {
        return reportService.getProfitOverPeriod(timeUnit, timeAmount, groupBy);
    }
}
