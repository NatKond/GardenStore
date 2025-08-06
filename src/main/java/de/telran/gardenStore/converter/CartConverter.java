package de.telran.gardenStore.converter;

import de.telran.gardenStore.dto.CartResponseDto;
import de.telran.gardenStore.entity.Cart;
import de.telran.gardenStore.entity.CartItem;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CartConverter implements ConverterEntityToDtoShort<Cart, CartResponseDto> {

    private final ModelMapper modelMapper;

    private final CartItemConverter cartItemConverter;

    @Override
    public CartResponseDto convertEntityToDto(Cart cart) {
        modelMapper.typeMap(Cart.class, CartResponseDto.class).addMappings(mapper -> {
                    mapper.map(cart1 -> cart1.getUser().getUserId(), CartResponseDto::setUserId);
                    mapper
                            .using(context -> cartItemConverter.convertEntityListToDtoList((List<CartItem>) context.getSource()))
                            .map(Cart::getItems, CartResponseDto::setItems);
                });

        return modelMapper.map(cart, CartResponseDto.class);
    }
}