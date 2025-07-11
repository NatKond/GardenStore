package de.telran.gardenStore.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductShortResponseDto {
    @EqualsAndHashCode.Include
    private Long productId;

    private String name;

    private String description;

    private BigDecimal price;

    private BigDecimal discountPrice;

    private Long categoryId;

}
