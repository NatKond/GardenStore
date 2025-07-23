package de.telran.gardenStore.controller;
import de.telran.gardenStore.dto.CartResponseDto;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/v1/cart")
public interface CartController {

    @GetMapping("/{userId}")
    CartResponseDto getCartByUserId(@PathVariable @Positive Long userId);

    @PostMapping("/items")
    @ResponseStatus(HttpStatus.CREATED)
    CartResponseDto addCartItem(@RequestParam @Positive Long userId, @RequestParam @Positive Long productId);

    @ResponseStatus(HttpStatus.ACCEPTED)
    @PutMapping("/items/{cartItemId}")
    CartResponseDto updateCartItem(@PathVariable @Positive Long cartItemId,
                                   @RequestParam @Positive Integer quantity);

    @DeleteMapping("/items/{cartItemId}")
    CartResponseDto deleteCartItem(@PathVariable @Positive Long cartItemId);
}