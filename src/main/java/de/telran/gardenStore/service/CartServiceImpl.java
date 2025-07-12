package de.telran.gardenStore.service;

import de.telran.gardenStore.entity.AppUser;
import de.telran.gardenStore.entity.Cart;
import de.telran.gardenStore.entity.CartItem;
import de.telran.gardenStore.exception.CartAlreadyExistsException;
import de.telran.gardenStore.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final UserService userService;
    private final CartItemService cartItemService;

    @Override
    public Cart create(Long userId) {
        AppUser user = userService.getUserById(userId);

        if (cartRepository.findByUserId(userId).isPresent()) {
            throw new CartAlreadyExistsException("Cart already exists for this user");
        }

        return cartRepository.save(
                Cart.builder()
                        .user(user)
                        .build()
        );
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



