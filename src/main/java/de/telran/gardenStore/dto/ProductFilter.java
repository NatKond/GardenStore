package de.telran.gardenStore.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class ProductFilter {

    Long categoryId;

    Boolean discount;

    BigDecimal minPrice;

    BigDecimal maxPrice;

    String sortBy;

    Boolean sortDirection;
}
