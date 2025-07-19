package de.telran.gardenStore.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder(toBuilder = true)
public class CartItemCreateRequestDto {
    @NotNull
    private List<Long> productList;
    @Min(1)
    private int quantity;
}
