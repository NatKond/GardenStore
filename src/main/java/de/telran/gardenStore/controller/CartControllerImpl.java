package de.telran.gardenStore.controller;

import de.telran.gardenStore.annotation.Loggable;
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

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/cart")
@PreAuthorize("hasRole('USER')")
public class CartControllerImpl implements CartController {

    private final CartService cartService;

    private final ConverterEntityToDtoShort<Cart, CartResponseDto> cartConverter;

    private final UserService userService;

    @Override
    @GetMapping
    public CartResponseDto getForCurrentUser() {
        return cartConverter.convertEntityToDto(
                cartService.getByUser(
                        userService.getCurrent()));
    }

    @Override
    @Loggable
    @PostMapping("/items/{productId}")
    @ResponseStatus(HttpStatus.CREATED)
    public CartResponseDto addItem(@PathVariable @Positive Long productId) {
        return cartConverter.convertEntityToDto(
                cartService.addItem(productId));
    }

    @Override
    @Loggable
    @ResponseStatus(HttpStatus.ACCEPTED)
    @PutMapping("/items/{cartItemId}")
    public CartResponseDto updateItem(@PathVariable @Positive Long cartItemId,
                                          @RequestParam @Positive Integer quantity) {
        return cartConverter.convertEntityToDto(
                cartService.updateItem(cartItemId, quantity));
    }

    @Override
    @Loggable
    @DeleteMapping("/items/{cartItemId}")
    public CartResponseDto deleteItem(@PathVariable @Positive Long cartItemId) {
        return cartConverter.convertEntityToDto(cartService.deleteItem(cartItemId));
    }
}
