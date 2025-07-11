package de.telran.gardenStore.service;

import de.telran.gardenStore.entity.Cart;
import de.telran.gardenStore.entity.CartItem;
import de.telran.gardenStore.entity.Product;
import de.telran.gardenStore.exception.CartItemNotFoundException;
import de.telran.gardenStore.repository.CartItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartItemServiceImpl implements CartItemService {

    private final CartItemRepository cartItemRepository;
    private final ProductService productService;

    @Override
    public CartItem addCartItem(Cart cart, Long productId) {
        CartItem existingItem = cartItemRepository
                .findByCartIdAndProductId(cart.getId(), productId)
                .orElse(null);

        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + 1);
            return cartItemRepository.save(existingItem);
        } else {
            Product product = productService.getProductById(productId);

            CartItem newItem = new CartItem();
            newItem.setCart(cart);
            newItem.setProduct(product);
            newItem.setQuantity(1);

            return cartItemRepository.save(newItem);
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

    @Override
    public CartItem getById(Long cartItemId) {
        return cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new CartItemNotFoundException(
                        "CartItem with id " + cartItemId + " not found"
                ));
    }
}




