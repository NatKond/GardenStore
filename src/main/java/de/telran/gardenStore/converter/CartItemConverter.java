package de.telran.gardenStore.converter;

import de.telran.gardenStore.dto.CartItemResponseDto;
import de.telran.gardenStore.dto.FavoriteResponseDto;
import de.telran.gardenStore.entity.CartItem;
import de.telran.gardenStore.entity.Favorite;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class CartItemConverter extends AbstractConverter implements ConverterEntityToDto<CartItem, CartItemResponseDto, CartItemResponseDto> {
    private final ModelMapper modelMapper;
    private final CartConverter cartConverter;

    @Override
    public CartItemResponseDto convertEntityToDto(CartItem cartItem) {
        return modelMapper.map(cartItem, CartItemResponseDto.class);
    }
    @Override
    public List<CartItemResponseDto> convertEntityListToDtoList(List<CartItem> products) {
        return ConverterEntityToDto.convertList(products, this::convertEntityToDto);
    }
}
