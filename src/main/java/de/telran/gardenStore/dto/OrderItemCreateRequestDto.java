package de.telran.gardenStore.dto;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class OrderItemCreateRequestDto {

    @Positive
    @NotNull(message = "Product is required")
    private Long productId;

    @Positive
    @NotNull(message = "Quantity is required")
    private Integer quantity;
}