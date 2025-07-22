package de.telran.gardenStore.controller;

import de.telran.gardenStore.converter.Converter;
import de.telran.gardenStore.dto.*;
import de.telran.gardenStore.entity.*;
import de.telran.gardenStore.service.OrderService;
import de.telran.gardenStore.service.UserService;
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

    private final UserService userService;

    private final Converter<Order, OrderCreateRequestDto, OrderResponseDto, OrderShortResponseDto> orderConverter;

    @Override
    public List<OrderShortResponseDto> getAll(Long userId) {
        return orderConverter.convertEntityListToDtoList(orderService.getAllByUserId(userId));
    }

    @Override
    public OrderResponseDto getById(@Positive Long orderId) {
        OrderResponseDto orderResponseDto = orderConverter.convertEntityToDto(orderService.getById(orderId));
        orderResponseDto.setTotalAmount(orderService.getTotalAmount(orderId));
        return orderResponseDto;
    }

    @Override
    public OrderResponseDto create(Long userId, @Valid OrderCreateRequestDto orderCreateRequestDto) {
        Order order = orderConverter.convertDtoToEntity(orderCreateRequestDto);

        AppUser user = userService.getUserById(userId);
        order.setUser(user);

        if (orderCreateRequestDto.getContactPhone() != null) {
            order.setContactPhone(user.getPhoneNumber());
        }

        OrderResponseDto orderResponseDto = orderConverter.convertEntityToDto(orderService.create(order));
        orderResponseDto.setTotalAmount(orderService.getTotalAmount(orderResponseDto.getOrderId()));

        return orderResponseDto;
    }

    @Override
    public OrderResponseDto addOrderItem(@Positive Long orderId, @Positive Long productId, @Positive Integer quantity){
        OrderResponseDto orderResponseDto = orderConverter.convertEntityToDto(orderService.addOrderItem(orderId, productId, quantity));
        orderResponseDto.setTotalAmount(orderService.getTotalAmount(orderId));
        return orderResponseDto;
    }

    @Override
    public OrderResponseDto updateOrderItem(@Positive Long orderItemId, @Positive Integer quantity){
        OrderResponseDto orderResponseDto = orderConverter.convertEntityToDto(orderService.updateOrderItem(orderItemId, quantity));
        orderResponseDto.setTotalAmount(orderService.getTotalAmount(orderResponseDto.getOrderId()));
        return orderResponseDto;
    }

    @Override
    public OrderResponseDto removeOrderItem(@Positive Long orderItemId){
        OrderResponseDto orderResponseDto = orderConverter.convertEntityToDto(orderService.removeOrderItem(orderItemId));
        orderResponseDto.setTotalAmount(orderService.getTotalAmount(orderResponseDto.getOrderId()));
        return orderResponseDto;
    }

    @Override
    public OrderResponseDto delete(@Positive Long orderId) {
        return orderConverter.convertEntityToDto(
                orderService.cancel(orderId));
    }
}