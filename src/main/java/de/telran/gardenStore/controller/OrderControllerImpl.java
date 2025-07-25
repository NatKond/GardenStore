package de.telran.gardenStore.controller;

import de.telran.gardenStore.converter.Converter;
import de.telran.gardenStore.dto.*;
import de.telran.gardenStore.entity.*;
import de.telran.gardenStore.service.OrderService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@Validated
public class OrderControllerImpl implements OrderController {

    private final OrderService orderService;

    private final Converter<Order, OrderCreateRequestDto, OrderResponseDto, OrderShortResponseDto> orderConverter;

    @Override
    public List<OrderShortResponseDto> getAll() {
        return orderConverter.convertEntityListToDtoList(orderService.getAllForCurrentUser());
    }

    @Override
    public OrderResponseDto getById(@Positive Long orderId) {
        OrderResponseDto orderResponseDto = orderConverter.convertEntityToDto(
                orderService.getById(orderId)
        );
        orderResponseDto.setTotalAmount(orderService.getTotalAmount(orderId));
        return orderResponseDto;
    }

    @Override
    public OrderResponseDto create(@Valid OrderCreateRequestDto orderCreateRequestDto) {
        OrderResponseDto orderResponseDto = orderConverter.convertEntityToDto(
                orderService.create(
                        orderConverter.convertDtoToEntity(orderCreateRequestDto))
        );
        orderResponseDto.setTotalAmount(orderService.getTotalAmount(orderResponseDto.getOrderId()));

        return orderResponseDto;
    }

    @Override
    public OrderResponseDto addOrderItem(@Positive Long orderId, @Positive Long productId, @Positive Integer quantity){
        OrderResponseDto orderResponseDto = orderConverter.convertEntityToDto(
                orderService.addOrderItem(orderId, productId, quantity)
        );
        orderResponseDto.setTotalAmount(orderService.getTotalAmount(orderId));
        return orderResponseDto;
    }

    @Override
    public OrderResponseDto updateOrderItem(@Positive Long orderItemId, @Positive Integer quantity){
        OrderResponseDto orderResponseDto = orderConverter.convertEntityToDto(
                orderService.updateOrderItem(orderItemId, quantity)
        );
        orderResponseDto.setTotalAmount(orderService.getTotalAmount(orderResponseDto.getOrderId()));
        return orderResponseDto;
    }

    @Override
    public OrderResponseDto removeOrderItem(@Positive Long orderItemId){
        OrderResponseDto orderResponseDto = orderConverter.convertEntityToDto(
                orderService.removeOrderItem(orderItemId)
        );
        orderResponseDto.setTotalAmount(orderService.getTotalAmount(orderResponseDto.getOrderId()));
        return orderResponseDto;
    }

    @Override
    public OrderResponseDto delete(@Positive Long orderId) {
        return orderConverter.convertEntityToDto(
                orderService.cancel(orderId));
    }
}