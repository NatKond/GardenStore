package de.telran.gardenStore.controller;

import de.telran.gardenStore.converter.ConverterEntityToDtoShort;
import de.telran.gardenStore.dto.CartResponseDto;
import de.telran.gardenStore.entity.Cart;
import de.telran.gardenStore.service.CartService;
import de.telran.gardenStore.service.UserService;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/v1/cart")
public class CartControllerImpl implements CartController {

    private final CartService cartService;

    private final ConverterEntityToDtoShort<Cart, CartResponseDto> cartConverter;

    private final UserService userService;

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    @Override
    public CartResponseDto getForCurrentUser() {
        return cartConverter.convertEntityToDto(
                cartService.getByUser(
                        userService.getCurrent()));
    }

    @PostMapping("/items/{productId}")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('USER')")
    @Override
    public CartResponseDto addItem(@PathVariable @Positive Long productId) {
        return cartConverter.convertEntityToDto(
                cartService.addItem(productId));
    }

    @ResponseStatus(HttpStatus.ACCEPTED)
    @PutMapping("/items/{cartItemId}")
    @PreAuthorize("hasRole('USER')")
    @Override
    public CartResponseDto updateItem(@PathVariable @Positive Long cartItemId,
                                          @RequestParam @Positive Integer quantity) {
        return cartConverter.convertEntityToDto(
                cartService.updateItem(cartItemId, quantity));
    }

    @Override
    @DeleteMapping("/items/{cartItemId}")
    @PreAuthorize("hasRole('USER')")
    public CartResponseDto deleteItem(@PathVariable @Positive Long cartItemId) {
        return cartConverter.convertEntityToDto(cartService.deleteItem(cartItemId));
    }
}
