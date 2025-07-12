package de.telran.gardenStore.controller;

import de.telran.gardenStore.dto.*;
import de.telran.gardenStore.entity.*;
import de.telran.gardenStore.enums.OrderStatus;
import de.telran.gardenStore.service.OrderService;
import de.telran.gardenStore.service.ProductService;
import de.telran.gardenStore.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final UserService userService;
    private final ProductService productService;

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public OrderResponseDto createOrder(
            @AuthenticationPrincipal AppUser currentUser,
            @Valid @RequestBody OrderCreateRequestDto dto) {

        AppUser user = userService.getUserById(currentUser.getUserId());

        Order order = Order.builder()
                .user(user)
                .deliveryAddress(dto.getDeliveryAddress())
                .contactPhone(dto.getContactPhone())
                .deliveryMethod(dto.getDeliveryMethod())
                .status(OrderStatus.CREATED)
                .createdAt(LocalDateTime.now())
                .build();

        List<OrderItem> orderItems = dto.getItems().stream()
                .map(itemDto -> {
                    Product product = productService.getProductById(itemDto.getProductId());

                    // Определяем цену (если есть скидка, берем скидочную цену)
                    BigDecimal price = product.getDiscountPrice() != null ?
                            product.getDiscountPrice() : product.getPrice();

                    return OrderItem.builder()
                            .order(order)
                            .product(product)
                            .quantity(itemDto.getQuantity())
                            .priceAtPurchase(price)
                            .build();
                })
                .collect(Collectors.toList());

        order.setItems(orderItems);

        Order savedOrder = orderService.create(order);

        BigDecimal totalPrice = calculateTotalPrice(savedOrder.getItems());

        return buildOrderResponseDto(savedOrder, totalPrice);
    }

    private BigDecimal calculateTotalPrice(List<OrderItem> items) {
        return items.stream()
                .map(item -> item.getPriceAtPurchase()
                        .multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private OrderResponseDto buildOrderResponseDto(Order order, BigDecimal totalPrice) {
        return OrderResponseDto.builder()
                .orderId(order.getOrderId())
                .status(order.getStatus().name())
                .deliveryAddress(order.getDeliveryAddress())
                .contactPhone(order.getContactPhone())
                .deliveryMethod(order.getDeliveryMethod())
                .createdAt(order.getCreatedAt())
                .items(order.getItems().stream()
                        .map(this::convertToOrderItemResponseDto)
                        .collect(Collectors.toList()))
                .totalPrice(totalPrice)
                .build();
    }

    private OrderItemResponseDto convertToOrderItemResponseDto(OrderItem item) {
        return OrderItemResponseDto.builder()
                .productId(item.getProduct().getProductId())
                .productName(item.getProduct().getName())
                .quantity(item.getQuantity())
                .priceAtPurchase(item.getPriceAtPurchase())
                .subtotal(item.getPriceAtPurchase()
                        .multiply(BigDecimal.valueOf(item.getQuantity())))
                .build();
    }
}