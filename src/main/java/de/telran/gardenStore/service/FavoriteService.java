package de.telran.gardenStore.service;

import de.telran.gardenStore.entity.AppUser;
import de.telran.gardenStore.entity.Favorite;

import java.util.List;

public interface FavoriteService {
    List<Favorite> getAllFavorites();
    Favorite getFavoriteById(Long id);
    Favorite createFavorite(AppUser user);
    void deleteFavoriteById(Long id);
}
