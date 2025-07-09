package de.telran.gardenStore.service;

import de.telran.gardenStore.entity.Favorite;

import java.util.List;

public interface FavoriteService {

    List<Favorite> getAllFavoritesByUser(Long userId);

    Favorite getFavoriteById(Long favoriteId);

    Favorite createFavorite(Long userId, Long productId);

    void deleteFavoriteById(Long favoriteId);
}
