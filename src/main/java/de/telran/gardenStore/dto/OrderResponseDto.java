package de.telran.gardenStore.dto;

import org.hibernate.mapping.List;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class OrderResponseDto {
    private Long orderId;
    private Long userId;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List items;
    private BigDecimal totalPrice;
}