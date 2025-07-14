package de.telran.gardenStore.service;

import de.telran.gardenStore.entity.AppUser;
import de.telran.gardenStore.entity.Cart;
import de.telran.gardenStore.entity.CartItem;
import de.telran.gardenStore.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import de.telran.gardenStore.exception.CartNotFoundException;

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
    public Cart getCartByUser(AppUser user) {
        return cartRepository.findByUser(user)
                .orElseThrow(() -> new CartNotFoundException("Cart for user " + user.getUserId() + " not found"));
    }

    @Override
    public Cart create(AppUser user) {
        return cartRepository.save(Cart.builder()
                .user(user)
                .build());
    }

    @Override
    public Cart update(Cart cart) {
        Cart existingCart = getCartByUser(cart.getUser());

        existingCart.setItems(cart.getItems());

        return cartRepository.save(existingCart);
    }


    @Override
    public Cart addCartItem(Long userId, Long productId) {
        AppUser user = userService.getUserById(userId);

        Cart cart;
        try {
            cart = getCartByUser(user);
        } catch (CartNotFoundException exception) {
            cart = create(user);
        }

//        Cart cart = cartRepository.findByUserId(userId)
//                .orElseGet(() -> create(userService.getUserById(userId)));

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


