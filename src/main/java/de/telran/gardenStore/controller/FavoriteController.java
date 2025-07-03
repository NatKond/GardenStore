package de.telran.gardenStore.controller;

import de.telran.gardenStore.dto.FavoriteCreateRequestDto;
import de.telran.gardenStore.dto.FavoriteResponseDto;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RequestMapping("/v1/favorites")
public interface FavoriteController {

    @GetMapping
    List<FavoriteResponseDto> getAllFavorites();

    @GetMapping("/{favoriteId}")
    FavoriteResponseDto getFavoriteById(@PathVariable Long favoriteId);

    @PostMapping
    FavoriteResponseDto createFavorite(@RequestBody FavoriteCreateRequestDto favoriteCreateRequestDto);

    @DeleteMapping("/{favoriteId}")
    void deleteFavorite(@PathVariable Long favoriteId);
}
