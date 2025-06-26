package de.telran.gardenStore.service;

import de.telran.gardenStore.entity.Favorite;
import de.telran.gardenStore.entity.User;

import java.util.List;

public interface FavoriteService {
    List<Favorite> getAllFavorites();
    Favorite getFavoriteById(Long id);
    Favorite createFavorite(User user);
    void deleteFavoriteById(Long id);
}
