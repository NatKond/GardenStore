package de.telran.gardenStore.controller;

import de.telran.gardenStore.dto.CartItemResponseDto;
import de.telran.gardenStore.dto.CartResponseDto;


public interface CartController {
    CartResponseDto getCartByUserId(Long userId);
    CartResponseDto addCartItem(Long userId, Long productId);
    CartResponseDto updateCartItem(Long userId, Long productId, Integer quantity);
    void deleteCartItem(Long cartItemId);
}
