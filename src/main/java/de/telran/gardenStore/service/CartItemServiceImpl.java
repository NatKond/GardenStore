package de.telran.gardenStore.service;

import de.telran.gardenStore.entity.Cart;
import de.telran.gardenStore.entity.CartItem;
import de.telran.gardenStore.entity.Product;
import de.telran.gardenStore.exception.CartItemNotFoundException;
import de.telran.gardenStore.repository.CartItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartItemServiceImpl implements CartItemService {

    private final CartItemRepository cartItemRepository;

    private final ProductService productService;

    @Override
    public CartItem getById(Long cartItemId) {
        return cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new CartItemNotFoundException(
                        "CartItem with id " + cartItemId + " not found"
                ));
    }

    @Override
    public CartItem addCartItem(Cart cart, Long productId) {
        Optional<CartItem> existingItemOptional = cartItemRepository
                .findByCartIdAndProductId(cart.getCartId(), productId);

        if (existingItemOptional.isPresent()) {
            CartItem existingItem = existingItemOptional.get();
            existingItem.setQuantity(existingItem.getQuantity() + 1);
            return cartItemRepository.save(existingItem);
        } else {
            Product product = productService.getProductById(productId);
            return cartItemRepository.save(CartItem.builder()
                    .cart(cart)
                    .product(product)
                    .quantity(1)
                    .build());
        }
    }

    @Override
    public CartItem updateCartItem(Long cartItemId, Integer quantity) {
        CartItem item = getById(cartItemId);
        item.setQuantity(quantity);
        return cartItemRepository.save(item);
    }

    @Override
    public void deleteCartItem(Long cartItemId) {
        cartItemRepository.delete(getById(cartItemId));
    }

}




