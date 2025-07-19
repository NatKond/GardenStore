package de.telran.gardenStore.converter;

import de.telran.gardenStore.dto.CartItemCreateRequestDto;
import de.telran.gardenStore.dto.CartItemResponseDto;
import de.telran.gardenStore.dto.CartResponseDto;
import de.telran.gardenStore.entity.Cart;
import de.telran.gardenStore.entity.CartItem;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class CartConverter extends AbstractConverter implements ConverterEntityToDtoShort<Cart, CartResponseDto> {
    private final ModelMapper modelMapper;

    @Override
    public CartResponseDto convertEntityToDto(Cart cart) {
        return modelMapper.map(cart, CartResponseDto.class);
    }

}


