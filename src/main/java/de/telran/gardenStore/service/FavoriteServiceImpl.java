package de.telran.gardenStore.service;

import de.telran.gardenStore.entity.AppUser;
import de.telran.gardenStore.entity.Favorite;
import de.telran.gardenStore.exception.FavoriteAlreadyExistsException;
import de.telran.gardenStore.exception.FavoriteNotFoundException;
import de.telran.gardenStore.repository.FavoriteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
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
        return favoriteRepository.findByUserAndFavoriteId(userService.getCurrent(), favoriteId).orElseThrow(
                () -> new FavoriteNotFoundException("Favorite with id " + favoriteId + " not found"));
    }

    @Override
    public Favorite create(Long productId) {
        AppUser user = userService.getCurrent();
        if (favoriteRepository.findByUserAndProductId(user, productId).isPresent()) {
            throw new FavoriteAlreadyExistsException("Favorite with userId " + user.getUserId() + " and productId " + productId + " already exists");
        }

        Favorite favorite = Favorite.builder()
                .user(user)
                .product(productService.getById(productId))
                .build();

        log.debug("Attempt to save favorite by user {}\nwith product = {}", user.getEmail(), favorite.getProduct());

        return favoriteRepository.save(favorite);
    }

    @Override
    public void deleteById(Long favoriteId) {
        favoriteRepository.delete(getById(favoriteId));
    }
}
