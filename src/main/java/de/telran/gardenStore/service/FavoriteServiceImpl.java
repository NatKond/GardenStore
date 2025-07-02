package de.telran.gardenStore.service;

import de.telran.gardenStore.entity.Favorite;
import de.telran.gardenStore.exception.FavoriteNotFoundException;
import de.telran.gardenStore.repository.FavoriteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FavoriteServiceImpl implements FavoriteService {

    private final FavoriteRepository favoriteRepository;

    @Override
    public List<Favorite> getAllFavorites() {
        return favoriteRepository.findAll();
    }

    @Override
    public Favorite getFavoriteById(Long favoriteId) {
        return favoriteRepository.findById(favoriteId).orElseThrow(()
                -> new FavoriteNotFoundException("Favorite with id " + favoriteId + " not found"));
    }

    @Override
    public Favorite createFavorite(Favorite favorite) {
        return favoriteRepository.save(favorite);
    }

    @Override
    public void deleteFavoriteById(Long favoriteId) {
        favoriteRepository.delete(getFavoriteById(favoriteId));
    }
}
