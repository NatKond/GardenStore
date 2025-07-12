package de.telran.gardenStore.service;

import de.telran.gardenStore.entity.*;
import de.telran.gardenStore.entity.AppUser;
import de.telran.gardenStore.entity.Cart;
import de.telran.gardenStore.entity.CartItem;
import de.telran.gardenStore.repository.CartRepository;
import de.telran.gardenStore.repository.CartItemRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import de.telran.gardenStore.exception.CartAlreadyExistsException;
import de.telran.gardenStore.exception.CartNotFoundException;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;

    private final CartItemRepository cartItemRepository;

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
    public Cart getCartByUser(AppUser user) {
        return cartRepository.findByUserId(user.getUserId())
                .orElseThrow(() -> new CartNotFoundException("Cart for user " + user.getUserId() + " not found"));
    }

    @Override
    @Transactional
    public void processOrderItem(Cart cart, Long productId, Integer orderedQuantity) {
        Optional<CartItem> cartItemOpt = cartItemRepository.findByCartIdAndProductId(cart.getCartId(), productId);

        if (cartItemOpt.isPresent()) {
            CartItem cartItem = cartItemOpt.get();
            if (orderedQuantity >= cartItem.getQuantity()) {
                // Удаляем товар из корзины, если заказанное количество >= количеству в корзине
                cartItemRepository.delete(cartItem);
                cart.getItems().remove(cartItem);
            } else {
                // Уменьшаем количество в корзине
                cartItem.setQuantity(cartItem.getQuantity() - orderedQuantity);
                cartItemRepository.save(cartItem);
            }
        }
    }
    @Override
    @Transactional
    public Cart addCartItem(Long userId, Long productId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseGet(() -> create(userId));

        Optional<CartItem> existingItem = cartItemRepository.findByCartIdAndProductId(cart.getCartId(), productId);

        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + 1);
            cartItemRepository.save(item);
        } else {
            Product product = productService.getProductById(productId);
            CartItem newItem = CartItem.builder()
                    .cart(cart)
                    .product(product)
                    .quantity(1)
                    .build();
            cartItemRepository.save(newItem);
            cart.getItems().add(newItem);
        }

        return cartRepository.save(cart);
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


