package de.telran.gardenStore.controller;

import de.telran.gardenStore.dto.OrderCreateRequestDto;
import de.telran.gardenStore.dto.OrderResponseDto;
import de.telran.gardenStore.dto.OrderShortResponseDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/v1/orders")
public interface OrderController {

    @GetMapping("/history")
    @PreAuthorize("hasRole('USER')")
    List<OrderShortResponseDto> getAll();

    @GetMapping("/{orderId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    OrderResponseDto getById(@PathVariable @Positive Long orderId);

    @PostMapping()
    @PreAuthorize("hasRole('USER')")
    @ResponseStatus(HttpStatus.CREATED)
    OrderResponseDto create(@RequestBody @Valid OrderCreateRequestDto orderCreateRequestDto);

    @PostMapping("/items")
    @PreAuthorize("hasRole('USER')")
    @ResponseStatus(HttpStatus.CREATED)
    OrderResponseDto addOrderItem(@RequestParam @Positive Long orderId, @RequestParam @Positive Long productId, @RequestParam @Positive Integer quantity);

    @PutMapping("/items")
    @PreAuthorize("hasRole('USER')")
    @ResponseStatus(HttpStatus.ACCEPTED)
    OrderResponseDto updateOrderItem(@RequestParam @Positive Long orderItemId, @RequestParam @Positive Integer quantity);

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/items/{orderItemId}")
    OrderResponseDto removeOrderItem(@PathVariable @Positive Long orderItemId);

    @DeleteMapping("/{orderId}")
    @PreAuthorize("hasRole('USER')")
    void delete(@PathVariable @Positive Long orderId);
}
