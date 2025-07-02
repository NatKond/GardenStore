package de.telran.gardenStore.controller;

import de.telran.gardenStore.dto.FavoriteCreateRequestDto;
import de.telran.gardenStore.dto.FavoriteResponseDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.validation.annotation.Validated;

import java.util.List;
@Validated
public interface FavoriteController {

    List<FavoriteResponseDto> getAllFavorites();

    FavoriteResponseDto getFavoriteById(@Positive Long favoriteId);

    FavoriteResponseDto createFavorite(@Valid FavoriteCreateRequestDto favoriteCreateRequestDto);

    void deleteFavorite(@Positive Long favoriteId);
}
