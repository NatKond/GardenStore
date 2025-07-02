package de.telran.gardenStore.service;
import de.telran.gardenStore.entity.AppUser;
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
    public Favorite getFavoriteById(Long id) {
        return favoriteRepository.findById(id)
                .orElseThrow(() -> new FavoriteNotFoundException("Favorite with id " + id + " not found"));
    }

    @Override
    public Favorite createFavorite(AppUser user) {
        Favorite favorite = new Favorite();
        favorite.setUserId(user.getUserId());
        return favoriteRepository.save(favorite);
    }

    @Override
    public void deleteFavoriteById(Long id) {
        favoriteRepository.deleteById(id);
    }
}