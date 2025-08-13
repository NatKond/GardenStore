package de.telran.gardenStore.service;

import de.telran.gardenStore.entity.AppUser;
import de.telran.gardenStore.entity.Cart;
import de.telran.gardenStore.entity.CartItem;
import de.telran.gardenStore.exception.CartItemNotFoundException;
import de.telran.gardenStore.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import de.telran.gardenStore.exception.CartNotFoundException;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;

    private final UserService userService;

    private final ProductService productService;

    @Override
    public Cart getByUser(AppUser user) {
        return cartRepository.findByUser(user)
                .orElseThrow(() -> new CartNotFoundException(
                        "Cart for user " + user.getUserId() + " not found"
                ));
    }

    @Override
    public Cart create(AppUser user) {

        return cartRepository.save(Cart.builder()
                .user(user)
                .build());
    }

    @Override
    public Cart update(Cart cart) {
        Cart existingCart = getByUser(cart.getUser());

        existingCart.setItems(cart.getItems());

        return cartRepository.save(existingCart);
    }

    @Override
    public Cart addItem(Long productId) {
        AppUser user = userService.getCurrent();

        Cart cart;
        try {
            cart = getByUser(user);
        } catch (CartNotFoundException exception) {
            cart = create(user);
        }

        List<CartItem> items = cart.getItems();

        Optional<CartItem> existingItem = items.stream()
                .filter(item -> item.getProduct().getProductId().equals(productId))
                .findFirst();

        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + 1);
        } else {
            CartItem newItem = CartItem.builder()
                    .product(productService.getById(productId))
                    .quantity(1)
                    .build();
            items.add(newItem);
        }
        log.debug("Attempt to save cart to carts table :{}", cart);

        return cartRepository.save(cart);
    }

    @Override
    public Cart updateItem(Long cartItemId, Integer quantity) {
        Cart cart = getByUser(userService.getCurrent());
        CartItem cartItem = findItemInCart(cart.getItems(), cartItemId);
        cartItem.setQuantity(quantity);
        return cartRepository.save(cart);
    }

    @Override
    public Cart deleteItem(Long cartItemId) {
        Cart cart = getByUser(userService.getCurrent());
        CartItem cartItem = findItemInCart(cart.getItems(), cartItemId);
        cart.getItems().remove(cartItem);
        return cartRepository.save(cart);
    }

    private CartItem findItemInCart(List<CartItem> cartItems, Long cartItemId) {
        return cartItems.stream()
                .filter(item -> item.getCartItemId().equals(cartItemId))
                .findFirst()
                .orElseThrow(() -> new CartItemNotFoundException("CartItem with id " + cartItemId + " not found"));
    }
}


