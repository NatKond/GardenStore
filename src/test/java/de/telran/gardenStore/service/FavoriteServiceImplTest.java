package de.telran.gardenStore.service;

import de.telran.gardenStore.AbstractTest;
import de.telran.gardenStore.entity.Favorite;
import de.telran.gardenStore.exception.FavoriteAlreadyExistsException;
import de.telran.gardenStore.exception.FavoriteNotFoundException;
import de.telran.gardenStore.repository.FavoriteRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FavoriteServiceImplTest extends AbstractTest {

    @Mock
    private FavoriteRepository favoriteRepository;

    @Mock
    private ProductService productService;

    @Mock
    private UserService userService;

    @InjectMocks
    private FavoriteServiceImpl favoriteService;

    @Test
    @DisplayName("Get all favorites for current user")
    void getAllFavoritesPositiveCase() {
        List<Favorite> expected = Arrays.asList(favorite1, favorite2);

        when(userService.getCurrent()).thenReturn(user1);
        when(favoriteRepository.getAllByUser(user1)).thenReturn(expected);

        List<Favorite> actual = favoriteService.getAllForCurrentUser();

        assertNotNull(actual);
        assertEquals(2, actual.size());
        assertEquals(expected, actual);
        verify(favoriteRepository).getAllByUser(user1);
    }

    @Test
    @DisplayName("Get favorite by ID : positive case")
    void getByIdPositiveCase() {
        Long favoriteId = favorite1.getFavoriteId();

        Favorite expected = favorite1;

        when(userService.getCurrent()).thenReturn(user1);
        when(favoriteRepository.findByUserAndFavoriteId(user1, favoriteId)).thenReturn(Optional.of(favorite1));

        Favorite actual = favoriteService.getById(favoriteId);

        assertNotNull(actual);
        assertEquals(expected, actual);
        verify(favoriteRepository).findByUserAndFavoriteId(user1, favoriteId);
    }

    @Test
    @DisplayName("Get favorite by ID : negative case")
    void getByIdNegativeCase() {
        Long favoriteId = 9999L;

        when(userService.getCurrent()).thenReturn(user1);
        when(favoriteRepository.findByUserAndFavoriteId(user1, favoriteId)).thenReturn(Optional.empty());

        RuntimeException runtimeException = assertThrows(FavoriteNotFoundException.class, () -> favoriteService.getById(favoriteId));
        assertEquals("Favorite with id " + favoriteId + " not found", runtimeException.getMessage());
        verify(favoriteRepository).findByUserAndFavoriteId(user1, favoriteId);
    }

    @Test
    @DisplayName("Create new favorite : positive case")
    void createFavoriteSuccess() {
        Favorite expected = favoriteCreated;
        Long userId = favoriteToCreate.getUser().getUserId();
        Long productId = favoriteToCreate.getUser().getUserId();

        when(userService.getCurrent()).thenReturn(user1);
        when(favoriteRepository.findByUserIdAndProductId(userId, productId)).thenReturn(Optional.empty());
        when(productService.getById(productId)).thenReturn(product3);
        when(favoriteRepository.save(favoriteToCreate)).thenReturn(favoriteCreated);

        Favorite actual = favoriteService.create(productId);

        assertNotNull(actual);
        assertEquals(expected, actual);
        assertEquals(expected.getUser(), actual.getUser());
        assertEquals(expected.getProduct(), actual.getProduct());
        verify(favoriteRepository).save(favoriteToCreate);
    }

    @Test
    @DisplayName("Create new favorite : negative case")
    void createFavoriteFailure() {
        Long userId = favoriteToCreate.getUser().getUserId();
        Long productId = product3.getProductId();

        when(userService.getCurrent()).thenReturn(user1);
        when(favoriteRepository.findByUserIdAndProductId(userId, productId)).thenReturn(Optional.of(favoriteCreated));

        RuntimeException runtimeException = assertThrows(FavoriteAlreadyExistsException.class, () -> favoriteService.create(productId));
        assertEquals("Favorite with userId " + userId + " and productId " + productId + " already exists", runtimeException.getMessage());
        verify(favoriteRepository).findByUserIdAndProductId(userId, productId);
    }

    @Test
    @DisplayName("Delete favorite by ID : positive case")
    void deleteFavoriteSuccess() {
        Long favoriteId = favorite1.getFavoriteId();

        when(userService.getCurrent()).thenReturn(user1);
        when(favoriteRepository.findByUserAndFavoriteId(user1, favoriteId)).thenReturn(Optional.of(favorite1));

        favoriteService.deleteById(favoriteId);

        verify(favoriteRepository).findByUserAndFavoriteId(user1, favoriteId);
        verify(favoriteRepository).delete(favorite1);
    }

    @Test
    @DisplayName("Delete favorite by ID : negative case")
    void deleteFavoriteFailure() {
        Long favoriteId = 9999L;

        when(favoriteRepository.findByUserAndFavoriteId(user1, favoriteId)).thenReturn(Optional.empty());
        when(userService.getCurrent()).thenReturn(user1);

        RuntimeException runtimeException = assertThrows(FavoriteNotFoundException.class, () -> favoriteService.getById(favoriteId));
        assertEquals("Favorite with id " + favoriteId + " not found", runtimeException.getMessage());
        verify(favoriteRepository).findByUserAndFavoriteId(user1, favoriteId);
    }
}

