package de.telran.gardenStore.controller;
import de.telran.gardenStore.dto.CartResponseDto;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/v1/cart")
public interface CartController {

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    CartResponseDto getCartForCurrentUser();

    @PostMapping("/items/{productId}")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('USER')")
    CartResponseDto addCartItem(@PathVariable @Positive Long productId);

    @ResponseStatus(HttpStatus.ACCEPTED)
    @PutMapping("/items/{cartItemId}")
    @PreAuthorize("hasRole('USER')")
    CartResponseDto updateCartItem(@PathVariable @Positive Long cartItemId,
                                   @RequestParam @Positive Integer quantity);

    @DeleteMapping("/items/{cartItemId}")
    @PreAuthorize("hasRole('USER')")
    CartResponseDto deleteCartItem(@PathVariable @Positive Long cartItemId);
}