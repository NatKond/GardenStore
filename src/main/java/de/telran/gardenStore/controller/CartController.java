package de.telran.gardenStore.controller;
import de.telran.gardenStore.dto.CartResponseDto;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/v1/cart")
public interface CartController {

    @GetMapping
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    CartResponseDto getCartForCurrentUser();

    @PostMapping("/items/{productId}")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    CartResponseDto addCartItem(@PathVariable @Positive Long productId);

    @ResponseStatus(HttpStatus.ACCEPTED)
    @PutMapping("/items/{cartItemId}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    CartResponseDto updateCartItem(@PathVariable @Positive Long cartItemId,
                                   @RequestParam @Positive Integer quantity);

    @DeleteMapping("/items/{cartItemId}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    CartResponseDto deleteCartItem(@PathVariable @Positive Long cartItemId);
}