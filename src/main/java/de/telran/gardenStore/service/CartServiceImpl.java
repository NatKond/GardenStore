package de.telran.gardenStore.service;

import de.telran.gardenStore.entity.AppUser;
import de.telran.gardenStore.entity.Cart;
import de.telran.gardenStore.entity.CartItem;
import de.telran.gardenStore.repository.CartRepository;
import de.telran.gardenStore.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import de.telran.gardenStore.exception.CartAlreadyExistsException;


@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final CartItemService cartItemService;

    @Override
    public Cart create(Long userId) {
        AppUser user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (cartRepository.findByUserId(userId).isPresent()) {
            throw new CartAlreadyExistsException("Cart already exists for this user");
        }

        Cart cart = Cart.builder()
                .user(user)
                .build();

        return cartRepository.save(cart);
    }


    @Override
    public CartItem addCartItem(Long userId, Long productId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseGet(() -> create(userId));

        return cartItemService.addCartItem(cart, productId);
    }

    @Override
    public Cart updateCartItem(Long cartItemId, Integer quantity) {
        CartItem updatedItem = cartItemService.updateCartItem(cartItemId, quantity);
        return updatedItem.getCart();
    }

    @Override
    public void deleteCartItem(Long cartItemId) {
        cartItemService.deleteCartItem(cartItemId);
    }
}


