package de.telran.gardenStore.service;

import de.telran.gardenStore.entity.CartItem;
import de.telran.gardenStore.exception.CartItemNotFoundException;
import de.telran.gardenStore.repository.CartItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartItemServiceImpl implements CartItemService {

    private final CartItemRepository cartItemRepository;

    private final UserService userService;

    @Override
    public CartItem getById(Long cartItemId) {
        return cartItemRepository.findByUserAndId(userService.getCurrent(), cartItemId)
                .orElseThrow(() -> new CartItemNotFoundException(
                        "CartItem with id " + cartItemId + " not found"
                ));
    }
}




