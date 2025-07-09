package de.telran.gardenStore.service;

import de.telran.gardenStore.entity.AppUser;
import de.telran.gardenStore.entity.Favorite;
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
        return favoriteRepository.getAllByUser(user);
    }

    @Override
    public Favorite getFavoriteById(Long favoriteId) {
        return favoriteRepository.findById(favoriteId).orElseThrow(()
                -> new FavoriteNotFoundException("Favorite with id " + favoriteId + " not found"));
    }

    @Override
    public Favorite createFavorite(Long userId, Long productId) {
        if (favoriteRepository.findByUserIdAndProductId(userId, productId).isPresent()) {
            throw new FavoriteAlreadyExistsException("Favorite with userId " + userId + " and productId " + productId + " already exists");
        }

        return favoriteRepository.save(Favorite.builder()
                .user(userService.getUserById(userId))
                .product(productService.getProductById(productId))
                .build());
    }

    @Override
    public void deleteFavoriteById(Long favoriteId) {
        favoriteRepository.delete(getFavoriteById(favoriteId));
    }
}
