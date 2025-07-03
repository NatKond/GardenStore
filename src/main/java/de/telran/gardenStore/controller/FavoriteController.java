package de.telran.gardenStore.controller;

import de.telran.gardenStore.dto.FavoriteCreateRequestDto;
import de.telran.gardenStore.dto.FavoriteResponseDto;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RequestMapping("/v1/favorites")
public interface FavoriteController {

    @GetMapping("/{userId}")
    List<FavoriteResponseDto> getAllFavoritesByUser(@PathVariable Long userId);

    @PostMapping("/{userId}")
    FavoriteResponseDto createFavorite(@PathVariable Long userId, @RequestBody FavoriteCreateRequestDto favoriteCreateRequestDto);

    @DeleteMapping("/{favoriteId}")
    void deleteFavorite(@PathVariable Long favoriteId);
}
