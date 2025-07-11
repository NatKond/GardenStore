package de.telran.gardenStore.service;

import de.telran.gardenStore.entity.AppUser;
import de.telran.gardenStore.entity.Cart;
import de.telran.gardenStore.entity.CartItem;
import de.telran.gardenStore.repository.CartRepository;
import de.telran.gardenStore.repository.UserRepository;   // ⚡ твой UserRepository
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final CartItemService cartItemService;

    public Cart create(Long userId) {
        AppUser user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (cartRepository.findByUserId(userId).isPresent()) {
            throw new RuntimeException("Cart already exists for this user");
        }

        Cart cart = new Cart();
        cart.setUser(user);
        return cartRepository.save(cart);
    }

    public CartItem addCartItem(Long userId, Long productId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseGet(() -> create(userId));

        return cartItemService.addCartItem(cart, productId);
    }

    public CartItem updateCartItem(Long cartItemId, Integer quantity) {
        return cartItemService.updateCartItem(cartItemId, quantity);
    }

    public void deleteCartItem(Long cartItemId) {
        cartItemService.deleteCartItem(cartItemId);
    }
}






