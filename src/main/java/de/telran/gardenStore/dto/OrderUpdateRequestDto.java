package de.telran.gardenStore.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderUpdateRequestDto {
    @NotBlank
    private String deliveryAddress;

    @Pattern(regexp = "^\\+?[0-9\\s-]{10,}$")
    private String contactPhone;

    @NotBlank
    private String deliveryMethod;
}