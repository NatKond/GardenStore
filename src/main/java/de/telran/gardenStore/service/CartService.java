package de.telran.gardenStore.service;

import de.telran.gardenStore.entity.Cart;

public interface CartService {

    Cart create(Long userId);

    Cart addCartItem(Long userId, Long productId);

    Cart updateCartItem(Long cartItemId, Integer quantity);

    void deleteCartItem(Long cartItemId);
}








