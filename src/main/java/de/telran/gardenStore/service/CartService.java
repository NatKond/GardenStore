package de.telran.gardenStore.service;

import de.telran.gardenStore.entity.AppUser;
import de.telran.gardenStore.entity.Cart;

public interface CartService {

    Cart getByUser(AppUser user);

    Cart create(AppUser user);

    Cart update(Cart cart);

    Cart addItem(Long productId);

    Cart updateItem(Long cartItemId, Integer quantity);

    Cart deleteItem(Long cartItemId);

    //void processOrderItem(Cart cart, Long productId, Integer orderedQuantity);
}