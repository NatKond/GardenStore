package de.telran.gardenStore.controller;

import de.telran.gardenStore.dto.FavoriteResponseDto;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RequestMapping("/v1/favorites")
public interface FavoriteController {

    @GetMapping("/{userId}")
    List<FavoriteResponseDto> getAllFavoritesByUser(@PathVariable @Positive Long userId);

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    FavoriteResponseDto createFavorite(@RequestParam @Positive Long userId,
                                       @RequestParam @Positive Long productId);

    @DeleteMapping("/{favoriteId}")
    void deleteFavorite(@PathVariable @Positive Long favoriteId);
}
