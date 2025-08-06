package de.telran.gardenStore.controller;
import de.telran.gardenStore.dto.CartResponseDto;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/v1/cart")
@PreAuthorize("hasRole('USER')")
public interface CartController {

    @GetMapping
    CartResponseDto getForCurrentUser();

    @PostMapping("/items/{productId}")
    @ResponseStatus(HttpStatus.CREATED)
    CartResponseDto addItem(@PathVariable @Positive Long productId);

    @ResponseStatus(HttpStatus.ACCEPTED)
    @PutMapping("/items/{cartItemId}")
    CartResponseDto updateItem(@PathVariable @Positive Long cartItemId,
                               @RequestParam @Positive Integer quantity);

    @DeleteMapping("/items/{cartItemId}")
    CartResponseDto deleteItem(@PathVariable @Positive Long cartItemId);
}