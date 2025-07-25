package de.telran.gardenStore.converter;

import de.telran.gardenStore.dto.*;
import de.telran.gardenStore.entity.Order;
import de.telran.gardenStore.entity.OrderItem;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OrderConverter implements Converter<Order, OrderCreateRequestDto, OrderResponseDto, OrderShortResponseDto> {

    private final ModelMapper modelMapper;

    private final OrderItemConverter orderItemConverter;

    @Override
    public Order convertDtoToEntity(OrderCreateRequestDto orderCreateRequestDto) {
        modelMapper.typeMap(OrderCreateRequestDto.class, Order.class).addMappings(
                mapper -> {
                    mapper.skip(Order::setOrderId);
                    mapper
                            .using(context -> orderItemConverter.converDtoListToEntityList((List<OrderItemCreateRequestDto>) context.getSource()))
                            .map(OrderCreateRequestDto::getItems, Order::setItems);
                }
        );

        Order order = modelMapper.map(orderCreateRequestDto, Order.class);
        order.getItems().forEach(orderItem -> orderItem.setOrder(order));
        return order;
    }

    @Override
    public OrderResponseDto convertEntityToDto(Order order) {
        modelMapper.typeMap(Order.class, OrderResponseDto.class).addMappings(
                mapper -> {
                    mapper.map(order1 -> order1.getUser().getUserId(), OrderResponseDto::setUserId);
                    mapper
                        .using(context -> orderItemConverter.convertEntityListToDtoList((List<OrderItem>) context.getSource()))
                        .map(Order::getItems, OrderResponseDto::setItems);
                }
        );
        return modelMapper.map(order, OrderResponseDto.class);
    }

    @Override
    public List<OrderShortResponseDto> convertEntityListToDtoList(List<Order> orders) {
        modelMapper.typeMap(Order.class, OrderShortResponseDto.class).addMappings(
                mapper -> {
                    mapper.map(order1 -> order1.getUser().getUserId(), OrderShortResponseDto::setUserId);
                }
        );

        return ConverterEntityToDto.convertList(orders, (order) -> modelMapper.map(order, OrderShortResponseDto.class));
    }
}
