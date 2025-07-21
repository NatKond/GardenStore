package de.telran.gardenStore.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
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

    @NotBlank(message = "Delivery method cannot be blank")
    @Pattern(regexp = "DELIVERY|PICKUP", message = "Delivery method must be either DELIVERY or PICKUP")
    private String deliveryMethod;

    @NotEmpty(message = "must not be empty")
    private List<OrderItemCreateRequestDto> items;
}