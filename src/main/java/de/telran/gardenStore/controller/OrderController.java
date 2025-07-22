package de.telran.gardenStore.controller;

import de.telran.gardenStore.dto.OrderCreateRequestDto;
import de.telran.gardenStore.dto.OrderResponseDto;
import de.telran.gardenStore.dto.OrderShortResponseDto;
import de.telran.gardenStore.dto.OrderUpdateRequestDto;
import de.telran.gardenStore.enums.OrderStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RequestMapping("/v1/orders")
public interface OrderController {

    @GetMapping("/history/{userId}")
    List<OrderShortResponseDto> getAll(@PathVariable Long userId);

    @GetMapping("/{orderId}")
    @PreAuthorize("hasRole('USER')")
    OrderResponseDto getById(@PathVariable @Positive Long orderId);

    @GetMapping
    List<OrderShortResponseDto> getAllOrders(
            @RequestParam(required = false) OrderStatus status,
            @RequestParam(required = false) LocalDateTime startDate,
            @RequestParam(required = false) LocalDateTime endDate
    );

    @PutMapping("/{orderId}")
    OrderResponseDto updateOrder(@PathVariable Long orderId, @RequestBody OrderUpdateRequestDto updateRequest);

    @PostMapping("/{userId}")
    @PreAuthorize("hasRole('USER')")
    @ResponseStatus(HttpStatus.CREATED)
    OrderResponseDto create(@PathVariable Long userId, @RequestBody @Valid OrderCreateRequestDto orderCreateRequestDto);

    @PostMapping("/items")
    @ResponseStatus(HttpStatus.CREATED)
    OrderResponseDto addOrderItem(@RequestParam @Positive Long orderId, @RequestParam @Positive Long productId, @RequestParam @Positive Integer quantity);

    @PutMapping("/items")
    @ResponseStatus(HttpStatus.ACCEPTED)
    OrderResponseDto updateOrderItem(@RequestParam @Positive Long orderItemId, @RequestParam @Positive Integer quantity);

    @DeleteMapping("/items/{orderItemId}")
    OrderResponseDto removeOrderItem(@PathVariable @Positive Long orderItemId);

    @DeleteMapping("/{orderId}")
    @PreAuthorize("hasRole('USER')")
    ResponseEntity<OrderResponseDto> delete(@PathVariable @Positive Long orderId);
}
