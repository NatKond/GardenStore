package de.telran.gardenStore.dto;

import java.util.List;

public class OrderRequestCreateDto {
    private Long userId;
    private List<OrderItemRequestDto> items;
}