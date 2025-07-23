package de.telran.gardenStore.converter;

import de.telran.gardenStore.dto.CartItemResponseDto;
import de.telran.gardenStore.entity.CartItem;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class CartItemConverter implements ConverterEntityToDto<CartItem, CartItemResponseDto, CartItemResponseDto> {

    private final ModelMapper modelMapper;

    @Override
    public CartItemResponseDto convertEntityToDto(CartItem cartItem) {
        return modelMapper.map(cartItem, CartItemResponseDto.class);
    }
    @Override
    public List<CartItemResponseDto> convertEntityListToDtoList(List<CartItem> products) {
        return ConverterEntityToDto.convertList(products, this::convertEntityToDto);
    }
}
