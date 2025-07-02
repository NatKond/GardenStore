package de.telran.gardenStore.dto;

import jakarta.validation.constraints.Positive;
import lombok.*;

@Data
@Builder
public class FavoriteCreateRequestDto {
    @Positive(message = "UserId should be positive")
    private Long userId;
    @Positive(message = "ProductId should be positive")
    private Long productId;
}
