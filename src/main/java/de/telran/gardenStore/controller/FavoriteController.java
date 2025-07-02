package de.telran.gardenStore.controller;

import de.telran.gardenStore.dto.FavoriteCreateRequestDto;
import de.telran.gardenStore.dto.FavoriteResponseDto;
import java.util.List;

public interface FavoriteController {

    List<FavoriteResponseDto> getAllFavorites();

    FavoriteResponseDto getFavoriteById(Long favoriteId);

    FavoriteResponseDto createFavorite(FavoriteCreateRequestDto favoriteCreateRequestDto);

    void deleteFavorite(Long favoriteId);
}
