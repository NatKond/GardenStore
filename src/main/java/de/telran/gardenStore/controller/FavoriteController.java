package de.telran.gardenStore.controller;

import de.telran.gardenStore.dto.FavoriteCreateRequestDto;
import de.telran.gardenStore.dto.FavoriteResponseDto;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RequestMapping("/v1/favorites")
public interface FavoriteController {

    List<FavoriteResponseDto> getAllFavoritesByUser(Long userId);

    FavoriteResponseDto createFavorite(Long userId, FavoriteCreateRequestDto favoriteCreateRequestDto);

    @DeleteMapping("/{favoriteId}")
    void deleteFavorite(@PathVariable Long favoriteId);
}
