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
    @DisplayName("Get all favorites by userId")
    void getAllFavoritesPositiveCase() {
        List<Favorite> expected = Arrays.asList(favorite1, favorite2);
        Long userId = user1.getUserId();

        when(userService.getUserById(userId)).thenReturn(user1);
        when(favoriteRepository.getAllByUser(user1)).thenReturn(expected);

        List<Favorite> actual = favoriteService.getAllFavoritesByUser(userId);

        assertNotNull(actual);
        assertEquals(2, actual.size());
        assertEquals(expected, actual);
        verify(favoriteRepository).getAllByUser(user1);
    }

    @Test
    @DisplayName("Get favorite by ID : positive case")
    void getFavoriteByIdPositiveCase() {
        Long favoriteId = favorite1.getFavoriteId();

        Favorite expected = favorite1;

        when(favoriteRepository.findById(favoriteId)).thenReturn(Optional.of(favorite1));

        Favorite actual = favoriteService.getFavoriteById(favoriteId);

        assertNotNull(actual);
        assertEquals(expected, actual);
        verify(favoriteRepository).findById(favoriteId);
    }

    @Test
    @DisplayName("Get favorite by ID : negative case")
    void getFavoriteByIdNegativeCase() {
        Long favoriteId = 9999L;

        when(favoriteRepository.findById(favoriteId)).thenReturn(Optional.empty());

        RuntimeException runtimeException = assertThrows(FavoriteNotFoundException.class, () -> favoriteService.getFavoriteById(favoriteId));
        assertEquals("Favorite with id " + favoriteId + " not found", runtimeException.getMessage());
        verify(favoriteRepository).findById(favoriteId);
    }

    @Test
    @DisplayName("Create new favorite : positive case")
    void testCreateFavoritePositiveCase() {
        Favorite expected = favoriteCreated;

        when(productService.getProductById(favoriteCreated.getProduct().getProductId())).thenReturn(product3);
        when(userService.getUserById(favoriteCreated.getUser().getUserId())).thenReturn(user1);

        when(favoriteRepository.findByUserAndProduct(favoriteToCreate.getUser(), favoriteToCreate.getProduct())).thenReturn(Optional.empty());
        when(favoriteRepository.save(favoriteToCreate)).thenReturn(favoriteCreated);

        Favorite actual = favoriteService.createFavorite(favoriteToCreate);

        assertNotNull(actual);
        assertEquals(expected, actual);
        assertEquals(expected.getUser(), actual.getUser());
        assertEquals(expected.getProduct(), actual.getProduct());
        verify(favoriteRepository).save(favoriteToCreate);
    }

    @Test
    @DisplayName("Create new favorite : negative case")
    void testCreateFavoriteNegativeCase() {

        when(productService.getProductById(favoriteCreated.getProduct().getProductId())).thenReturn(product3);
        when(userService.getUserById(favoriteCreated.getUser().getUserId())).thenReturn(user1);
        when(favoriteRepository.findByUserAndProduct(favoriteToCreate.getUser(), favoriteToCreate.getProduct())).thenReturn(Optional.of(favoriteCreated));

        RuntimeException runtimeException = assertThrows(FavoriteAlreadyExistsException.class, () -> favoriteService.createFavorite(favoriteToCreate));
        assertEquals("Favorite with userId " + favoriteToCreate.getUser().getUserId() + " and productId " + favoriteToCreate.getProduct().getProductId() + " already exists", runtimeException.getMessage());
        verify(favoriteRepository).findByUserAndProduct(favoriteToCreate.getUser(), favoriteToCreate.getProduct());
    }

    @Test
    @DisplayName("Delete favorite by ID : positive case")
    void testDeleteFavoriteByIdPositiveCase() {
        Long favoriteId = favorite1.getFavoriteId();

        when(favoriteRepository.findById(favoriteId)).thenReturn(Optional.of(favorite1));

        favoriteService.deleteFavoriteById(favoriteId);

        verify(favoriteRepository).findById(favoriteId);
        verify(favoriteRepository).delete(favorite1);
    }

    @Test
    @DisplayName("Delete favorite by ID : negative case")
    void testDeleteFavoriteNegativeCase() {
        Long favoriteId = 9999L;

        when(favoriteRepository.findById(favoriteId)).thenReturn(Optional.empty());

        RuntimeException runtimeException = assertThrows(FavoriteNotFoundException.class, () -> favoriteService.getFavoriteById(favoriteId));
        assertEquals("Favorite with id " + favoriteId + " not found", runtimeException.getMessage());
        verify(favoriteRepository).findById(favoriteId);
    }
}

