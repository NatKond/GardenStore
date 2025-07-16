package de.telran.gardenStore.controller;

import de.telran.gardenStore.dto.OrderCreateRequestDto;
import de.telran.gardenStore.dto.OrderResponseDto;
import de.telran.gardenStore.dto.OrderShortResponseDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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


    @DeleteMapping("/{orderId}")
    @PreAuthorize("hasRole('USER')")
    void delete(@PathVariable Long orderId);
}
