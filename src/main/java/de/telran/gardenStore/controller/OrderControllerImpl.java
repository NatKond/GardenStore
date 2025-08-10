package de.telran.gardenStore.controller;

import de.telran.gardenStore.converter.Converter;
import de.telran.gardenStore.converter.ConverterEntityToDto;
import de.telran.gardenStore.dto.*;
import de.telran.gardenStore.entity.*;
import de.telran.gardenStore.service.OrderService;
import de.telran.gardenStore.service.ProductService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@Validated
public class OrderControllerImpl implements OrderController {

    private final OrderService orderService;

    private final ConverterEntityToDto<Order, OrderResponseDto, OrderShortResponseDto> orderConverter;

    @Override
    public List<OrderShortResponseDto> getAllForCurrentUser() {
        return orderConverter.convertEntityListToDtoList(orderService.getAllForCurrentUser());
    }

    @Override
    public List<OrderResponseDto> getAllDeliveredForCurrentUser() {
        return orderService.getAllDeliveredForCurrentUser().stream().map(orderConverter::convertEntityToDto).toList();
    }

    @Override
    public List<OrderShortResponseDto> getAll(){
        return orderConverter.convertEntityListToDtoList(orderService.getAll());
    }

    @Override
    public OrderResponseDto getById(@Positive Long orderId) {
        return orderConverter.convertEntityToDto(
                orderService.getById(orderId));
    }

    @Override
    public OrderResponseDto create(@Valid OrderCreateRequestDto orderCreateRequestDto) {

        return orderConverter.convertEntityToDto(
                orderService.create(
                        orderCreateRequestDto.getDeliveryAddress(),
                        orderCreateRequestDto.getDeliveryMethod(),
                        orderCreateRequestDto.getContactPhone(),
                        orderCreateRequestDto.getItems().stream().collect(Collectors.toMap(OrderItemCreateRequestDto::getProductId, OrderItemCreateRequestDto::getQuantity, (newValue, oldValue) -> newValue))));
    }

    @Override
    public OrderResponseDto addItem(@Positive Long orderId, @Positive Long productId, @Positive Integer quantity){

        return orderConverter.convertEntityToDto(
                orderService.addItem(orderId, productId, quantity));
    }

    @Override
    public OrderResponseDto updateItem(@Positive Long orderItemId, @Positive Integer quantity){
        return orderConverter.convertEntityToDto(
                orderService.updateItem(orderItemId, quantity));
    }

    @Override
    public OrderResponseDto removeItem(@Positive Long orderItemId){
        return orderConverter.convertEntityToDto(
                orderService.removeItem(orderItemId));
    }

    @Override
    public OrderResponseDto delete(@Positive Long orderId) {
        return orderConverter.convertEntityToDto(
                orderService.cancel(orderId));
    }
}