package de.telran.gardenStore.controller;

import de.telran.gardenStore.annotation.Loggable;
import de.telran.gardenStore.converter.ConverterEntityToDto;
import de.telran.gardenStore.dto.*;
import de.telran.gardenStore.entity.*;
import de.telran.gardenStore.service.OrderService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/orders")
public class OrderControllerImpl implements OrderController {

    private final OrderService orderService;

    private final ConverterEntityToDto<Order, OrderResponseDto, OrderShortResponseDto> orderConverter;

    @Override
    @GetMapping("/history")
    @PreAuthorize("hasRole('USER')")
    public List<OrderShortResponseDto> getAll() {
        return orderConverter.convertEntityListToDtoList(orderService.getAll());
    }

    @GetMapping("/history/delivered")
    @PreAuthorize("hasRole('USER')")
    @Override
    public List<OrderResponseDto> getAllDelivered() {
        return orderService.getAllDelivered().stream().map(orderConverter::convertEntityToDto).toList();
    }

    @Override
    @GetMapping("/{orderId}")
    @PreAuthorize("hasRole('USER')")
    public OrderResponseDto getById(@PathVariable @Positive Long orderId) {
        return orderConverter.convertEntityToDto(
                orderService.getById(orderId));
    }

    @Override
    @Loggable
    @PostMapping()
    @PreAuthorize("hasRole('USER')")
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponseDto create(@RequestBody @Valid OrderCreateRequestDto orderCreateRequestDto) {
        return orderConverter.convertEntityToDto(
                orderService.create(
                        orderCreateRequestDto.getDeliveryAddress(),
                        orderCreateRequestDto.getDeliveryMethod(),
                        orderCreateRequestDto.getContactPhone(),
                        orderCreateRequestDto.getItems().stream().collect(Collectors.toMap(OrderItemCreateRequestDto::getProductId, OrderItemCreateRequestDto::getQuantity, (newValue, oldValue) -> newValue))));
    }

    @Override
    @Loggable
    @PostMapping("/items")
    @PreAuthorize("hasRole('USER')")
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponseDto addItem(@RequestParam @Positive Long orderId,
                                    @RequestParam @Positive Long productId,
                                    @RequestParam @Positive Integer quantity){
        return orderConverter.convertEntityToDto(
                orderService.addItem(orderId, productId, quantity));
    }

    @Override
    @Loggable
    @PutMapping("/items")
    @PreAuthorize("hasRole('USER')")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public OrderResponseDto updateItem(@RequestParam @Positive Long orderItemId,
                                       @RequestParam @Positive Integer quantity){
        return orderConverter.convertEntityToDto(
                orderService.updateItem(orderItemId, quantity));
    }

    @Override
    @Loggable
    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/items/{orderItemId}")
    public OrderResponseDto removeItem(@PathVariable @Positive Long orderItemId){
        return orderConverter.convertEntityToDto(
                orderService.removeItem(orderItemId));
    }

    @Override
    @Loggable
    @DeleteMapping("/{orderId}")
    @PreAuthorize("hasRole('USER')")
    public OrderResponseDto delete(@PathVariable @Positive Long orderId) {
        return orderConverter.convertEntityToDto(
                orderService.cancel(orderId));
    }
}