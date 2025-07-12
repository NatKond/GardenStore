package de.telran.gardenStore.service;

import de.telran.gardenStore.entity.Cart;
import de.telran.gardenStore.entity.CartItem;

public interface CartItemService {

    CartItem getById(Long cartItemId);

    CartItem addCartItem(Cart cart, Long productId);

    CartItem updateCartItem(Long cartItemId, Integer quantity);

    void deleteCartItem(Long cartItemId);
}







