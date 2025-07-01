package de.telran.gardenStore.service;

import de.telran.gardenStore.dto.FavoriteResponseDto;
import de.telran.gardenStore.entity.AppUser;
import de.telran.gardenStore.entity.Favorite;

import java.util.List;

public interface FavoriteService {
    List<FavoriteResponseDto> getAllFavorites();
    FavoriteResponseDto getFavoriteById(Long id);
    FavoriteResponseDto createFavorite(AppUser user);
    void deleteFavoriteById(Long id);
}
