package de.telran.gardenStore.controller;

import de.telran.gardenStore.dto.*;
import de.telran.gardenStore.entity.*;
import de.telran.gardenStore.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

@RequiredArgsConstructor
public class OrderControllerImpl implements OrderController {

    private final OrderService orderService;


    public OrderResponseDto createOrder(
            @AuthenticationPrincipal AppUser currentUser,
            @Valid OrderCreateRequestDto orderCreateRequestDto) {

        return null;
    }

//    private BigDecimal calculateTotalPrice(Long orderId) {
//        return orderService.getById(orderId).getItems().stream()
//                .map(item -> item.getPriceAtPurchase()
//                        .multiply(BigDecimal.valueOf(item.getQuantity())))
//                .reduce(BigDecimal.ZERO, BigDecimal::add);
//    }
}