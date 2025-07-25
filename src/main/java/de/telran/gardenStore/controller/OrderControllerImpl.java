package de.telran.gardenStore.controller;

import de.telran.gardenStore.converter.Converter;
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

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/orders")
@Validated
public class OrderControllerImpl implements OrderController {

    private final OrderService orderService;

    private final Converter<Order, OrderCreateRequestDto, OrderResponseDto, OrderShortResponseDto> orderConverter;

    @Override
    @GetMapping("/history")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public List<OrderShortResponseDto> getAll() {
        return orderConverter.convertEntityListToDtoList(orderService.getAllForCurrentUser());
    }

    @Override
    @GetMapping("/{orderId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public OrderResponseDto getById(@PathVariable @Positive Long orderId) {
        OrderResponseDto orderResponseDto = orderConverter.convertEntityToDto(
                orderService.getById(orderId)
        );
        orderResponseDto.setTotalAmount(orderService.getTotalAmount(orderId));
        return orderResponseDto;
    }

    @Override
    @PostMapping()
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponseDto create(@RequestBody @Valid OrderCreateRequestDto orderCreateRequestDto) {
        OrderResponseDto orderResponseDto = orderConverter.convertEntityToDto(
                orderService.create(
                        orderConverter.convertDtoToEntity(orderCreateRequestDto))
        );
        orderResponseDto.setTotalAmount(orderService.getTotalAmount(orderResponseDto.getOrderId()));

        return orderResponseDto;
    }

    @Override
    @PostMapping("/items")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponseDto addOrderItem(@RequestParam @Positive Long orderId,
                                         @RequestParam @Positive Long productId,
                                         @RequestParam @Positive Integer quantity){
        OrderResponseDto orderResponseDto = orderConverter.convertEntityToDto(
                orderService.addOrderItem(orderId, productId, quantity)
        );
        orderResponseDto.setTotalAmount(orderService.getTotalAmount(orderId));
        return orderResponseDto;
    }

    @Override
    @PutMapping("/items")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public OrderResponseDto updateOrderItem(@RequestParam @Positive Long orderItemId,
                                            @RequestParam @Positive Integer quantity){
        OrderResponseDto orderResponseDto = orderConverter.convertEntityToDto(
                orderService.updateOrderItem(orderItemId, quantity)
        );
        orderResponseDto.setTotalAmount(orderService.getTotalAmount(orderResponseDto.getOrderId()));
        return orderResponseDto;
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @DeleteMapping("/items/{orderItemId}")
    @Override
    public OrderResponseDto removeOrderItem(@PathVariable @Positive Long orderItemId){
        OrderResponseDto orderResponseDto = orderConverter.convertEntityToDto(
                orderService.removeOrderItem(orderItemId)
        );
        orderResponseDto.setTotalAmount(orderService.getTotalAmount(orderResponseDto.getOrderId()));
        return orderResponseDto;
    }

    @DeleteMapping("/{orderId}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @Override
    public OrderResponseDto delete(@PathVariable @Positive Long orderId) {
        return orderConverter.convertEntityToDto(
                orderService.cancel(orderId));
    }
}