package de.telran.gardenStore.service;

import de.telran.gardenStore.entity.AppUser;
import de.telran.gardenStore.entity.Cart;
import de.telran.gardenStore.entity.CartItem;
import de.telran.gardenStore.exception.CartAccessDeniedException;
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
    public Cart addCartItem(Long productId) {
        AppUser user = userService.getCurrent();

        Cart cart;
        try {
            cart = getByUser(user);
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
                    .product(productService.getById(productId))
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
        CartItem cartItem = cartItemService.getById(cartItemId);
        Cart cart = cartItem.getCart();
        checkCartOwnership(cart);
        cartItem.setQuantity(quantity);
        //CartItem updatedItem = cartItemService.update(cartItemId, quantity);
        return cartRepository.save(cart);
    }

    @Override
    public Cart deleteCartItem(Long cartItemId) {
        CartItem cartItem = cartItemService.getById(cartItemId);
        Cart cart = cartItem.getCart();
        checkCartOwnership(cart);
        cart.getItems().remove(cartItem);
        return cartRepository.save(cart);
    }

    private void checkCartOwnership(Cart cart) {
        if (cart.getUser() != userService.getCurrent()) {
            throw new CartAccessDeniedException("Access denied");
        }
    }
}


