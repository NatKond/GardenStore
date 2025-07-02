package de.telran.gardenStore.service;

import de.telran.gardenStore.entity.Favorite;
import de.telran.gardenStore.exception.FavoriteNotFoundException;
import de.telran.gardenStore.repository.FavoriteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class FavoriteServiceImplTest {

    @Mock
    private FavoriteRepository favoriteRepository;

    @InjectMocks
    private FavoriteServiceImpl favoriteService;

    private Favorite favorite1;
    private Favorite favorite2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        favorite1 = Favorite.builder()
                .userId(1L)
                .userId(100L)
                .build();

        favorite2 = Favorite.builder()
                .userId(2L)
                .userId(101L)
                .build();
    }

    @Test
    @DisplayName("Should return all favorites")
    void testGetAllFavorites() {
        List<Favorite> favorites = Arrays.asList(favorite1, favorite2);
        when(favoriteRepository.findAll()).thenReturn(favorites);

        List<Favorite> result = favoriteService.getAllFavorites();

        assertThat(result).hasSize(2).contains(favorite1, favorite2);
        verify(favoriteRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should return favorite by ID")
    void testGetFavoriteById() {
        when(favoriteRepository.findById(1L)).thenReturn(Optional.of(favorite1));

        Favorite result = favoriteService.getFavoriteById(1L);

        assertThat(result).isEqualTo(favorite1);
        verify(favoriteRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should throw exception if favorite not found by ID")
    void testGetFavoriteByIdNotFound() {
        when(favoriteRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> favoriteService.getFavoriteById(99L))
                .isInstanceOf(FavoriteNotFoundException.class)
                .hasMessageContaining("Favorite with id 99 not found");

        verify(favoriteRepository, times(1)).findById(99L);
    }

    @Test
    @DisplayName("Should save and return favorite")
    void testCreateFavorite() {
        when(favoriteRepository.save(favorite1)).thenReturn(favorite1);

        Favorite result = favoriteService.createFavorite(favorite1);

        assertThat(result).isEqualTo(favorite1);
        verify(favoriteRepository, times(1)).save(favorite1);
    }

    @Test
    @DisplayName("Should delete favorite by ID")
    void testDeleteFavoriteById() {
        when(favoriteRepository.findById(1L)).thenReturn(Optional.of(favorite1));

        favoriteService.deleteFavoriteById(1L);

        verify(favoriteRepository, times(1)).findById(1L);
        verify(favoriteRepository, times(1)).delete(favorite1);
    }

    @Test
    @DisplayName("Should throw exception when deleting non-existent favorite")
    void testDeleteFavoriteByIdNotFound() {
        when(favoriteRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> favoriteService.deleteFavoriteById(99L))
                .isInstanceOf(FavoriteNotFoundException.class)
                .hasMessageContaining("Favorite with id 99 not found");

        verify(favoriteRepository, times(1)).findById(99L);
        verify(favoriteRepository, never()).delete(any());
    }
}

