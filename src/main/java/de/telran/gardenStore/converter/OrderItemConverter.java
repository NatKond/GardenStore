package de.telran.gardenStore.converter;

import de.telran.gardenStore.dto.OrderItemResponseDto;
import de.telran.gardenStore.entity.OrderItem;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class OrderItemConverter {
    public OrderItemResponseDto convertToDto(OrderItem orderItem) {
        return OrderItemResponseDto.builder()
                .orderItemId(orderItem.getOrderItemId())
                .productId(orderItem.getProduct().getProductId())
                .productName(orderItem.getProduct().getName())
                .quantity(orderItem.getQuantity())
                .priceAtOrder(orderItem.getPriceAtOrder())
                .subtotal(orderItem.getPriceAtOrder().multiply(BigDecimal.valueOf(orderItem.getQuantity())))
                .build();
    }
}