package de.telran.gardenStore.dto;

import de.telran.gardenStore.enums.DeliveryMethod;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder(toBuilder = true)
public class OrderCreateRequestDto {

    @NotBlank(message = "Delivery address cannot be blank")
    private String deliveryAddress;

    @Pattern(regexp = "^\\+?[0-9\\s-]{10,}$")
    private String contactPhone;

    @NotNull(message = "Delivery method is required")
    @Enumerated(EnumType.STRING)
    private DeliveryMethod deliveryMethod;

    @Valid
    @NotEmpty(message = "Order should contain at least one item")
    @Size(max = 1000, message = "Order has too many items")
    private List<OrderItemCreateRequestDto> items;
}