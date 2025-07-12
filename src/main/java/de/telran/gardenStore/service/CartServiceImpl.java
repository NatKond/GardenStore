package de.telran.gardenStore.service;

import de.telran.gardenStore.entity.AppUser;
import de.telran.gardenStore.entity.Cart;
import de.telran.gardenStore.entity.CartItem;
import de.telran.gardenStore.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import de.telran.gardenStore.exception.CartAlreadyExistsException;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;

    private final CartItemService cartItemService;

    private final UserService userService;

    private final ProductService productService;

    @Override
    public Cart create(Long userId) {
        AppUser user = userService.getUserById(userId);

        if (cartRepository.findByUserId(userId).isPresent()) {
            throw new CartAlreadyExistsException("Cart already exists for this user");
        }

        return cartRepository.save(Cart.builder()
                .user(user)
                .build());
    }


    @Override
    public Cart addCartItem(Long userId, Long productId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseGet(() -> create(userId));

        List<CartItem> items = cart.getItems();

        Optional<CartItem> existingItem = items.stream()
                .filter(item -> item.getProduct().getProductId().equals(productId))
                .findFirst();

        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + 1);
        } else {
            CartItem newItem = CartItem.builder()
                    .cart(cart)
                    .product(productService.getProductById(productId))
                    .quantity(1)
                    .build();
            items.add(newItem);
        }

        return cartRepository.save(cart);

//        CartItem cartItem = cartItemService.addCartItem(cart, productId);
//        return cartItem.getCart();
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


