package de.telran.gardenStore.converter;

import de.telran.gardenStore.dto.ProductShortResponseDto;
import de.telran.gardenStore.dto.report.ProductReport;
import de.telran.gardenStore.dto.report.ProfitReport;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ReportMapper {

    public List<ProductReport> mapToProductReportsList(List<Object[]> resultList) {
        return resultList
                .stream()
                .map(this::mapToProductReport)
                .collect(Collectors.toList());
    }

    public List<ProfitReport> mapToProfitReportsList(List<Object[]> resultList, Integer rowNumber) {
        Map<String, BigDecimal> groupedMap = resultList.stream()
                .collect(Collectors.groupingBy(raw -> String.valueOf(raw[rowNumber]),
                        LinkedHashMap::new,
                        Collectors.reducing(BigDecimal.ZERO, raw -> (BigDecimal) raw[0], BigDecimal::add)));

        return groupedMap.entrySet().stream()
                .map(this::mapToProfitReport)
                .collect(Collectors.toList());
    }

    private ProfitReport mapToProfitReport(Map.Entry<String, BigDecimal> entry) {
        return ProfitReport.builder()
                .period(entry.getKey())
                .amountPerPeriod(entry.getValue())
                .build();
    }

    private ProductReport mapToProductReport(Object[] row){
        return ProductReport.builder()
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
                .build();
    }
}
