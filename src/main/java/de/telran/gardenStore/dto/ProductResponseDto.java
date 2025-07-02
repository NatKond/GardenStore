package de.telran.gardenStore.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder
public class ProductResponseDto {

    @EqualsAndHashCode.Include
    private Long productId;

    private String name;

    private String description;

    private BigDecimal price;

    private BigDecimal discountPrice;

    private Long categoryId;

    private String imageUrl;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}


