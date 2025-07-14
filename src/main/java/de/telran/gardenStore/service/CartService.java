package de.telran.gardenStore.service;

import de.telran.gardenStore.entity.AppUser;
import de.telran.gardenStore.entity.Cart;

public interface CartService {

    Cart getCartByUser(AppUser user);

    Cart create(AppUser user);

    Cart update(Cart cart);

    Cart addCartItem(Long userId, Long productId);

    Cart updateCartItem(Long cartItemId, Integer quantity);

    void deleteCartItem(Long cartItemId);

    //void processOrderItem(Cart cart, Long productId, Integer orderedQuantity);
}








