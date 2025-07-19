package de.telran.gardenStore.controller;

import de.telran.gardenStore.converter.CartConverter;
import de.telran.gardenStore.converter.CartItemConverter;
import de.telran.gardenStore.dto.CartItemResponseDto;
import de.telran.gardenStore.dto.CartResponseDto;
import de.telran.gardenStore.entity.Cart;
import de.telran.gardenStore.entity.CartItem;
import de.telran.gardenStore.service.CartItemService;
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
    private final CartItemConverter cartItemConverter;
    private final CartItemService cartItemService;
    private final CartService cartService;
    private final CartConverter cartConverter;
    private final UserService userService;

    @Override
    public CartResponseDto getCartByUserId(@Positive Long userId) {
        Cart userCart = cartService.getByUser(userService.getUserById(userId));
        return cartConverter.convertEntityToDto(userCart);
    }

    @Override
    public CartResponseDto addCartItem(@Positive Long userId, @Positive Long productId) {
        Cart cart = cartService.addCartItem(userId, productId);
        return cartConverter.convertEntityToDto(cart);
    }

    @Override
    public CartResponseDto updateCartItem(@Positive Long userId, @Positive Long productId, Integer quantity) {
        Cart userCart = cartService.getByUser(userService.getUserById(userId));
        Cart cart = cartService.updateCartItem(productId, quantity);
        return cartConverter.convertEntityToDto(cart);
    }

    @Override
    public void deleteCartItem(@Positive Long cartItemId) {
        cartService.deleteCartItem(cartItemId);
    }
}
