package de.telran.gardenStore.service;

import de.telran.gardenStore.entity.AppUser;
import de.telran.gardenStore.entity.Favorite;
import de.telran.gardenStore.entity.Product;
import de.telran.gardenStore.exception.FavoriteAlreadyExistsException;
import de.telran.gardenStore.exception.FavoriteNotFoundException;
import de.telran.gardenStore.repository.FavoriteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FavoriteServiceImpl implements FavoriteService {

    private final FavoriteRepository favoriteRepository;

    private final ProductService productService;

    private final UserService userService;

    @Override
    public List<Favorite> getAllFavoritesByUser(Long userId) {
        AppUser user = userService.getUserById(userId);
        return favoriteRepository.getAllByUserId(user.getUserId());
    }

    @Override
    public Favorite getFavoriteById(Long favoriteId) {
        return favoriteRepository.findById(favoriteId).orElseThrow(()
                -> new FavoriteNotFoundException("Favorite with id " + favoriteId + " not found"));
    }

    @Override
    public Favorite createFavorite(Favorite favorite) {

        AppUser user = userService.getUserById(favorite.getUserId());
        Product product = productService.getProductById(favorite.getProductId());
        if (favoriteRepository.findByUserIdAndProductId(user.getUserId(), product.getProductId()).isPresent()) {
            throw new FavoriteAlreadyExistsException("Favorite with userId " + user.getUserId() + " and productId " + product.getProductId() + " already exists");
        }

        return favoriteRepository.save(favorite);
    }

    @Override
    public void deleteFavoriteById(Long favoriteId) {
        favoriteRepository.delete(getFavoriteById(favoriteId));
    }
}
