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
    public List<Favorite> getAllForCurrentUser() {
        return favoriteRepository.getAllByUser(userService.getCurrent());
    }

    @Override
    public Favorite getById(Long favoriteId) {
        return favoriteRepository.findByUserAndFavoriteId(userService.getCurrent(),favoriteId).orElseThrow(()
                -> new FavoriteNotFoundException("Favorite with id " + favoriteId + " not found"));
    }

    @Override
    public Favorite create(Long productId) {
        AppUser user = userService.getCurrent();
        Long userId = user.getUserId();
        if (favoriteRepository.findByUserIdAndProductId(userId, productId).isPresent()) {
            throw new FavoriteAlreadyExistsException("Favorite with userId " + userId + " and productId " + productId + " already exists");
        }

        return favoriteRepository.save(Favorite.builder()
                .user(user)
                .product(productService.getById(productId))
                .build());
    }

    @Override
    public void deleteById(Long favoriteId) {
        favoriteRepository.delete(getById(favoriteId));
    }
}
