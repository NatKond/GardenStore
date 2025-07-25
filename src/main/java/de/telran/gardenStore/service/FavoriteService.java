package de.telran.gardenStore.service;

import de.telran.gardenStore.entity.Favorite;

import java.util.List;

public interface FavoriteService {

    List<Favorite> getAllForCurrentUser();

    Favorite getById(Long favoriteId);

    Favorite create(Long productId);

    void deleteById(Long favoriteId);
}
