package de.telran.gardenStore.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
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


