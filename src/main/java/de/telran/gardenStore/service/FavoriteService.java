package de.telran.gardenStore.service;

import de.telran.gardenStore.entity.Favorite;

import java.util.List;

public interface FavoriteService {

    List<Favorite> getAllFavoritesByUser(Long userId);

    Favorite getFavoriteById(Long favoriteId);

    Favorite createFavorite(Favorite favorite);

    void deleteFavoriteById(Long favoriteId);
}
