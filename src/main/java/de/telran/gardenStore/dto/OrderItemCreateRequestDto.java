package de.telran.gardenStore.dto;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class OrderItemCreateRequestDto {

    @NotNull(message = "Product is required")
    @Positive
    private Long productId;

    @NotNull(message = "Quantity is required")
    @Positive(message = "Quantity should be at least 1")
    private int quantity;
}