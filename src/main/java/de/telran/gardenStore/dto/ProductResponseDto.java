package de.telran.gardenStore.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductResponseDto {

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


