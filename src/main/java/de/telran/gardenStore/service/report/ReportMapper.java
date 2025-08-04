package de.telran.gardenStore.service.report;

import de.telran.gardenStore.dto.ProductShortResponseDto;
import de.telran.gardenStore.dto.report.ProductReport;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class ReportMapper {

    public ProductReport mapToProductReport(Object[] row){
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
