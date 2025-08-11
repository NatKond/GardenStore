package de.telran.gardenStore.converter;

import de.telran.gardenStore.dto.OrderItemCreateRequestDto;
import de.telran.gardenStore.dto.OrderItemResponseDto;
import de.telran.gardenStore.entity.OrderItem;
import de.telran.gardenStore.entity.Product;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OrderItemConverter implements Converter<OrderItem, OrderItemCreateRequestDto, OrderItemResponseDto, OrderItemResponseDto> {

    private final ModelMapper modelMapper;

    @Override
    public OrderItem convertDtoToEntity(OrderItemCreateRequestDto orderItemCreateRequestDto) {
        modelMapper.typeMap(OrderItemCreateRequestDto.class, OrderItem.class).addMappings(
                mapper -> {
                    mapper.skip(OrderItem::setOrderItemId);
                    mapper
                            .using(context -> Product.builder().productId((Long) context.getSource()).build())
                            .map(OrderItemCreateRequestDto::getProductId, OrderItem::setProduct);
                }
        );

        return modelMapper.map(orderItemCreateRequestDto, OrderItem.class);
    }

    @Override
    public OrderItemResponseDto convertEntityToDto(OrderItem orderItem) {
        modelMapper.typeMap(OrderItem.class, OrderItemResponseDto.class).addMappings(
                (mapper ->
                        mapper.map(orderItem1 ->
                                        orderItem1.getProduct().getCategory().getCategoryId(),
                                (orderItemResponseDto, o) -> orderItemResponseDto.getProduct().setCategoryId((Long)o))));
        return modelMapper.map(orderItem, OrderItemResponseDto.class);
    }

    @Override
    public List<OrderItemResponseDto> convertEntityListToDtoList(List<OrderItem> orderItems) {
        return ConverterEntityToDto.convertList(orderItems, this::convertEntityToDto);
    }
}
