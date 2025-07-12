package de.telran.gardenStore.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class OrderCreateRequestDto {
    @NotBlank
    private String deliveryAddress;

    @Pattern(regexp = "^\\+?[0-9\\s-]{10,}$")
    private String contactPhone;

    @NotBlank
    private String deliveryMethod;

    @NotEmpty
    private List<OrderItemCreateDto> items; // Теперь с валидацией
}