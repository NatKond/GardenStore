package de.telran.gardenStore.converter;

import de.telran.gardenStore.dto.CartItemResponseDto;
import de.telran.gardenStore.entity.CartItem;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CartItemConverter implements ConverterEntityToDto<CartItem, CartItemResponseDto, CartItemResponseDto> {

    private final ModelMapper modelMapper;

    @Override
    public CartItemResponseDto convertEntityToDto(CartItem cartItem) {
        modelMapper.typeMap(CartItem.class, CartItemResponseDto.class).addMappings(
                (mapper ->
                            mapper.map(cartItem1 ->
                                    cartItem1.getProduct().getCategory().getCategoryId(),
                                    (cartItemResponseDto, o) -> cartItemResponseDto.getProduct().setCategoryId((Long)o))));
        return modelMapper.map(cartItem, CartItemResponseDto.class);
    }
    @Override
    public List<CartItemResponseDto> convertEntityListToDtoList(List<CartItem> products) {
        return ConverterEntityToDto.convertList(products, this::convertEntityToDto);
    }
}
