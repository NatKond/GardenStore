package de.telran.gardenStore.dto;

import jakarta.validation.Valid;
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

    @NotBlank(message = "Delivery method cannot be blank")
    private String deliveryMethod;

    @Pattern(regexp = "^\\+?[0-9\\s-]{10,}$")
    private String contactPhone;

    @Valid
    @NotEmpty(message = "Order should contain at least one item")
    private List<OrderItemCreateRequestDto> items;
}