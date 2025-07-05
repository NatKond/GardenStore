package de.telran.gardenStore.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Data
@Builder(toBuilder = true)
public class FavoriteCreateRequestDto {

    @Positive(message = "UserId should be positive")
    private Long userId;

    @NotNull
    @Positive(message = "ProductId should be positive")
    private Long productId;
}
