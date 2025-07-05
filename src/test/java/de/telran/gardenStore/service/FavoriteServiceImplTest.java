package de.telran.gardenStore.service;

import de.telran.gardenStore.entity.AppUser;
import de.telran.gardenStore.entity.Favorite;
import de.telran.gardenStore.entity.Product;
import de.telran.gardenStore.exception.FavoriteAlreadyExistsException;
import de.telran.gardenStore.exception.FavoriteNotFoundException;
import de.telran.gardenStore.repository.FavoriteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FavoriteServiceImplTest {

    @Mock
    private FavoriteRepository favoriteRepository;

    @Mock
    private ProductService productService;

    @Mock
    private UserService userService;

    @InjectMocks
    private FavoriteServiceImpl favoriteService;

    private Favorite favorite1;
    private Favorite favorite2;

    private Favorite favoriteToCreate;
    private Favorite favoriteCreated;

    @BeforeEach
    void setUp() {

        favorite1 = Favorite.builder()
                .favoriteId(1L)
                .userId(1L)
                .productId(5L)
                .build();

        favorite2 = Favorite.builder()
                .favoriteId(2L)
                .userId(1L)
                .productId(10L)
                .build();

        favoriteToCreate = Favorite.builder()
                .userId(1L)
                .productId(1L)
                .build();

        favoriteCreated = favoriteToCreate.toBuilder()
                .favoriteId(3L)
                .build();
    }

    @Test
    @DisplayName("Get all categories by userId")
    void getAllFavoritesPositiveCase() {
        List<Favorite> expected = Arrays.asList(favorite1, favorite2);
        Long userId = 1L;

        when(favoriteRepository.getAllByUserId(userId)).thenReturn(expected);

        List<Favorite> actual = favoriteService.getAllFavoritesByUser(userId);

        assertNotNull(actual);
        assertEquals(2, actual.size());
        assertEquals(expected, actual);
        verify(favoriteRepository).getAllByUserId(userId);
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

        AppUser user = AppUser.builder()
                .userId(1L)
                .name("Alice Johnson")
                .email("alice.johnson@example.com")
                .phoneNumber("+1234567890")
                .passwordHash("12345")
                .build();
        Product product = Product.builder()
                .productId(1L)
                .name("All-Purpose Plant Fertilizer")
                .discountPrice(new BigDecimal("8.99"))
                .price(new BigDecimal("11.99"))
                .categoryId(1L)
                .description("Balanced NPK formula for all types of plants")
                .build();

        when(productService.getProductById(favoriteCreated.getProductId())).thenReturn(product);
        when(userService.getUserById(favoriteCreated.getUserId())).thenReturn(user);

        when(favoriteRepository.findByUserIdAndProductId(favoriteToCreate.getUserId(), favoriteToCreate.getProductId())).thenReturn(Optional.empty());
        when(favoriteRepository.save(favoriteToCreate)).thenReturn(favoriteCreated);

        Favorite actual = favoriteService.createFavorite(favoriteToCreate);

        assertNotNull(actual);
        assertEquals(expected, actual);
        assertEquals(expected.getUserId(), actual.getUserId());
        assertEquals(expected.getProductId(), actual.getProductId());
        verify(favoriteRepository).save(favoriteToCreate);
    }

    @Test
    @DisplayName("Create new favorite : negative case")
    void testCreateFavoriteNegativeCase() {

        AppUser user = AppUser.builder()
                .userId(1L)
                .name("Alice Johnson")
                .email("alice.johnson@example.com")
                .phoneNumber("+1234567890")
                .passwordHash("12345")
                .build();
        Product product = Product.builder()
                .productId(1L)
                .name("All-Purpose Plant Fertilizer")
                .discountPrice(new BigDecimal("8.99"))
                .price(new BigDecimal("11.99"))
                .categoryId(1L)
                .description("Balanced NPK formula for all types of plants")
                .build();

        when(productService.getProductById(favoriteCreated.getProductId())).thenReturn(product);
        when(userService.getUserById(favoriteCreated.getUserId())).thenReturn(user);
        when(favoriteRepository.findByUserIdAndProductId(favoriteToCreate.getUserId(), favoriteToCreate.getProductId())).thenReturn(Optional.of(favoriteCreated));

        RuntimeException runtimeException = assertThrows(FavoriteAlreadyExistsException.class, () -> favoriteService.createFavorite(favoriteToCreate));
        assertEquals("Favorite with userId " + favoriteToCreate.getUserId() + " and productId " + favoriteToCreate.getProductId() + " already exists", runtimeException.getMessage());
        verify(favoriteRepository).findByUserIdAndProductId(favoriteToCreate.getUserId(), favoriteToCreate.getProductId());
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

