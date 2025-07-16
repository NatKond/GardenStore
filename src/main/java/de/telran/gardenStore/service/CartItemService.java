package de.telran.gardenStore.service;

import de.telran.gardenStore.entity.Cart;
import de.telran.gardenStore.entity.CartItem;

public interface CartItemService {

    CartItem getById(Long cartItemId);

    CartItem add(Cart cart, Long productId);

    CartItem update(Long cartItemId, Integer quantity);

    void delete(Long cartItemId);
}







