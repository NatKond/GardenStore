package de.telran.gardenStore.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class OrderRequestCreateDto {
    @NotNull
    private Long userId;

    @NotNull
    private List<OrderItemRequestDto> items;
}