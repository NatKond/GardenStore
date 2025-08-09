package de.telran.gardenStore.service.report;
import de.telran.gardenStore.dto.report.ProductReport;
import de.telran.gardenStore.dto.report.ProfitReport;

import java.util.List;

public interface ReportService {

    List<ProductReport> getTopPurchasedProducts(Integer limit);

    List<ProductReport> getTopCanceledProducts(Integer limit);

    List<ProductReport> getProductsAwaitingPaymentForMoreDays(Integer days, Integer limit);

    List<ProfitReport> getProfitOverPeriod(String period, Integer limit, String groupBy);
}
