package de.telran.gardenStore.dto;

import lombok.Data;
//import org.hibernate.mapping.List;
import java.util.List;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class OrderResponseDto {
    private Long orderId;
    private Long userId;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List items;
    private BigDecimal totalPrice;
}