package de.telran.gardenStore.dto;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class OrderItemCreateRequestDto {
    @NotNull
    private Long productId;

    @Positive
    private Integer quantity;
}