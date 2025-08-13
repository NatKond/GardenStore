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

@Service
@RequiredArgsConstructor
@Slf4j
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
        Cart savedCart = cartRepository.save(
                Cart.builder()
                        .user(user)
                        .build());
        log.debug("UserId = {}: Cart created", user.getUserId());
        return savedCart;
    }

    @Override
    public Cart update(Cart cart) {
        Cart existingCart = getByUser(cart.getUser());
        existingCart.setItems(cart.getItems());
        Cart savedCart = cartRepository.save(existingCart);
        log.debug("CartId = {}: Cart updated", savedCart.getCartId());
        return savedCart;
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
        log.info("Attempt to save cart to carts table :{}", cart);

        Cart savedCart = cartRepository.save(cart);
        log.debug("CartId = {}, ProductId = {}: Item added", savedCart.getCartId(), productId);
        return savedCart;
    }

    @Override
    public Cart updateItem(Long cartItemId, Integer quantity) {
        Cart cart = getByUser(userService.getCurrent());
        CartItem cartItem = findItemInCart(cart.getItems(), cartItemId);
        cartItem.setQuantity(quantity);
        Cart savedCart = cartRepository.save(cart);
        log.debug("CartId = {}, CartItemId = {}: Item updated", savedCart.getCartId(), cartItemId);
        return savedCart;
    }

    @Override
    public Cart deleteItem(Long cartItemId) {
        Cart cart = getByUser(userService.getCurrent());
        CartItem cartItem = findItemInCart(cart.getItems(), cartItemId);
        cart.getItems().remove(cartItem);
        Cart savedCart = cartRepository.save(cart);
        log.debug("CartId = {}, CartItemId = {}: Item removed", savedCart.getCartId(), cartItemId);
        return savedCart;
    }

    private CartItem findItemInCart(List<CartItem> cartItems, Long cartItemId) {
        return cartItems.stream()
                .filter(item -> item.getCartItemId().equals(cartItemId))
                .findFirst()
                .orElseThrow(() -> new CartItemNotFoundException("CartItem with id " + cartItemId + " not found"));
    }
}


