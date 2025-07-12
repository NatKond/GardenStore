package de.telran.gardenStore.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderResponseDto {
    private Long orderId;
    private String status;
    private String deliveryAddress;
    private String contactPhone;
    private String deliveryMethod;
    private LocalDateTime createdAt;
    private List<OrderItemResponseDto> items;
    private BigDecimal totalPrice;
}
