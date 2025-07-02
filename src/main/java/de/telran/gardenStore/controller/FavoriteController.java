package de.telran.gardenStore.controller;

import de.telran.gardenStore.dto.FavoriteCreateRequestDto;
import de.telran.gardenStore.dto.FavoriteResponseDto;
import java.util.List;

public interface FavoriteController {

    List<FavoriteResponseDto> getAllFavoritesByUser(Long userId);

    FavoriteResponseDto createFavorite(Long userId, FavoriteCreateRequestDto favoriteCreateRequestDto);

    void deleteFavorite(Long favoriteId);
}
