package de.telran.gardenStore.controller;

import de.telran.gardenStore.converter.Converter;
import de.telran.gardenStore.dto.*;
import de.telran.gardenStore.entity.*;
import de.telran.gardenStore.enums.DeliveryMethod;
import de.telran.gardenStore.enums.OrderStatus;
import de.telran.gardenStore.service.OrderService;
import de.telran.gardenStore.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
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
    public List<OrderShortResponseDto> getAllOrders(
            OrderStatus status,
            LocalDateTime startDate,
            LocalDateTime endDate
    ) {
        List<Order> orders = orderService.getAllOrders(status, startDate, endDate);
        return orderConverter.convertEntityListToDtoList(orders);
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
        return orderConverter.convertEntityToDto(orderService.create(order));
    }

    @Override
    public OrderResponseDto update(Long orderId, @Valid OrderUpdateRequestDto orderUpdateRequestDto) {
        Order existingOrder = orderService.getById(orderId);

        // Обновляем только те поля, которые пришли в DTO
        if (orderUpdateRequestDto.getDeliveryAddress() != null) {
            existingOrder.setDeliveryAddress(orderUpdateRequestDto.getDeliveryAddress());
        }
        if (orderUpdateRequestDto.getContactPhone() != null) {
            existingOrder.setContactPhone(orderUpdateRequestDto.getContactPhone());
        }
        if (orderUpdateRequestDto.getDeliveryMethod() != null) {
            existingOrder.setDeliveryMethod(DeliveryMethod.valueOf(orderUpdateRequestDto.getDeliveryMethod()));
        }

        Order updatedOrder = orderService.update(orderId, existingOrder);
        return orderConverter.convertEntityToDto(updatedOrder);
    }

    @Override
    public OrderResponseDto delete(Long orderId) {
        Order cancelledOrder = orderService.cancel(orderId);
        return orderConverter.convertEntityToDto(cancelledOrder);
    }
}