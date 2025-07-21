package de.telran.gardenStore.controller;

import de.telran.gardenStore.dto.OrderCreateRequestDto;
import de.telran.gardenStore.dto.OrderResponseDto;
import de.telran.gardenStore.dto.OrderShortResponseDto;
import de.telran.gardenStore.dto.OrderUpdateRequestDto;
import de.telran.gardenStore.enums.OrderStatus;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
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
    OrderResponseDto getById(@PathVariable Long orderId);

    @PostMapping("/{userId}")
    @PreAuthorize("hasRole('USER')")
    @ResponseStatus(HttpStatus.CREATED)
    OrderResponseDto create(@PathVariable Long userId, @Valid @RequestBody OrderCreateRequestDto orderCreateRequestDto);

    @PutMapping("/{orderId}")
    @PreAuthorize("hasRole('USER')")
    OrderResponseDto update(@PathVariable Long orderId, @Valid @RequestBody OrderUpdateRequestDto orderUpdateRequestDto);

    @DeleteMapping("/{orderId}")
    @PreAuthorize("hasRole('USER')")
    @ResponseBody
    OrderResponseDto delete(@PathVariable Long orderId);

    @GetMapping
    List<OrderShortResponseDto> getAllOrders(
            @RequestParam(required = false) OrderStatus status,
            @RequestParam(required = false) LocalDateTime startDate,
            @RequestParam(required = false) LocalDateTime endDate
    );
}
