package de.telran.gardenStore.controller;

import de.telran.gardenStore.converter.Converter;
import de.telran.gardenStore.dto.*;
import de.telran.gardenStore.entity.*;
import de.telran.gardenStore.service.OrderService;
import de.telran.gardenStore.service.UserService;
import jakarta.validation.Valid;
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
    public OrderResponseDto getById(Long orderId) {
        return orderConverter.convertEntityToDto(orderService.getById(orderId));
    }

    @Override
    public OrderResponseDto create(Long userId, @Valid OrderCreateRequestDto orderCreateRequestDto) {
        Order order = orderConverter.convertDtoToEntity(orderCreateRequestDto);
        AppUser user = userService.getUserById(userId);
        order.setUser(user);
        order.setContactPhone(user.getPhoneNumber());
        return orderConverter.convertEntityToDto(
                orderService.create(order));
    }

    @Override
    public void deleteOrder(Long orderId) {
        orderService.cancel(orderId);
    }
}