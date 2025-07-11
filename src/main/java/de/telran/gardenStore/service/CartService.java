package de.telran.gardenStore.service;

import de.telran.gardenStore.entity.Cart;
import de.telran.gardenStore.entity.CartItem;

public interface CartService {

    Cart create(Long userId);

    CartItem addCartItem(Long userId, Long productId);

    Cart updateCartItem(Long cartItemId, Integer quantity);

    void deleteCartItem(Long cartItemId);
}








