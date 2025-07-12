package de.telran.gardenStore.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderItemCreateDto {
    @NotNull
    private Long productId;

    @Min(1)
    private Integer quantity;
}