package de.telran.gardenStore.controller;

import de.telran.gardenStore.dto.OrderCreateRequestDto;
import de.telran.gardenStore.dto.OrderResponseDto;
import de.telran.gardenStore.dto.OrderShortResponseDto;
import de.telran.gardenStore.dto.ProductShortResponseDto;
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
    @PreAuthorize("hasRole('USER')")
    OrderResponseDto getById(@PathVariable @Positive Long orderId);

    @PostMapping()
    @PreAuthorize("hasRole('USER')")
    @ResponseStatus(HttpStatus.CREATED)
    OrderResponseDto create(@RequestBody @Valid OrderCreateRequestDto orderCreateRequestDto);

    @PostMapping("/items")
    @PreAuthorize("hasRole('USER')")
    @ResponseStatus(HttpStatus.CREATED)
    OrderResponseDto addItem(@RequestParam @Positive Long orderId, @RequestParam @Positive Long productId, @RequestParam @Positive Integer quantity);

    @PutMapping("/items")
    @PreAuthorize("hasRole('USER')")
    @ResponseStatus(HttpStatus.ACCEPTED)
    OrderResponseDto updateItem(@RequestParam @Positive Long orderItemId, @RequestParam @Positive Integer quantity);

    @GetMapping("/purchased-products")
    @PreAuthorize("isAuthenticated()")
    List<ProductShortResponseDto> getAllPurchasedProducts();

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/items/{orderItemId}")
    OrderResponseDto removeItem(@PathVariable @Positive Long orderItemId);

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/{orderId}")
    OrderResponseDto delete(@PathVariable @Positive Long orderId);
}
