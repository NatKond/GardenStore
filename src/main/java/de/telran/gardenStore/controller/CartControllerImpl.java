package de.telran.gardenStore.controller;

import de.telran.gardenStore.converter.ConverterEntityToDtoShort;
import de.telran.gardenStore.dto.CartResponseDto;
import de.telran.gardenStore.entity.Cart;
import de.telran.gardenStore.service.CartService;
import de.telran.gardenStore.service.UserService;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Validated
public class CartControllerImpl implements CartController {

    private final CartService cartService;

    private final ConverterEntityToDtoShort<Cart, CartResponseDto> cartConverter;

    private final UserService userService;

    @Override
    public CartResponseDto getCartForCurrentUser() {
        return cartConverter.convertEntityToDto(
                cartService.getByUser(
                        userService.getCurrent()));
    }

    @Override
    public CartResponseDto addCartItem(@Positive Long productId) {
        return cartConverter.convertEntityToDto(
                cartService.addCartItem(productId));
    }

    @Override
    public CartResponseDto updateCartItem(@Positive Long cartItemId, @Positive Integer quantity) {
        return cartConverter.convertEntityToDto(
                cartService.updateCartItem(cartItemId, quantity));
    }

    @Override
    public CartResponseDto deleteCartItem(@Positive Long cartItemId) {
        return cartConverter.convertEntityToDto(cartService.deleteCartItem(cartItemId));
    }
}
