package de.telran.gardenStore.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.telran.gardenStore.dto.FavoriteCreateRequestDto;
import de.telran.gardenStore.dto.FavoriteResponseDto;
import de.telran.gardenStore.entity.Favorite;
import de.telran.gardenStore.service.FavoriteService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FavoriteControllerImpl.class)
@AutoConfigureMockMvc(addFilters = false)
class FavoriteControllerImplTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private FavoriteService favoriteService;

    @MockitoBean
    private ModelMapper modelMapper;

    private static Favorite favorite1;
    private static Favorite favorite2;
    private static FavoriteCreateRequestDto favoriteCreateRequestDto;
    private static FavoriteResponseDto favoriteResponseDto1;
    private static FavoriteResponseDto favoriteResponseDto2;

    @BeforeAll
    static void setUp() {
        favorite1 = Favorite.builder().userId(1L).productId(5L).userId(100L).build();
        favorite2 = Favorite.builder().userId(2L).productId(5L).userId(101L).build();

        favoriteCreateRequestDto = FavoriteCreateRequestDto.builder().userId(100L).build();

        favoriteResponseDto1 = FavoriteResponseDto.builder().productId(1L).userId(100L).build();
        favoriteResponseDto2 = FavoriteResponseDto.builder().productId(2L).userId(101L).build();
    }

    @Test
    @DisplayName("GET /api/favorites - should return all favorites")
    void getAllFavorites() throws Exception {
        when(favoriteService.getAllFavorites()).thenReturn(List.of(favorite1, favorite2));
        when(modelMapper.map(favorite1, FavoriteResponseDto.class)).thenReturn(favoriteResponseDto1);
        when(modelMapper.map(favorite2, FavoriteResponseDto.class)).thenReturn(favoriteResponseDto2);

        mockMvc.perform(get("/api/favorites"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(favorite1.getUserId()))
                .andExpect(jsonPath("$[0].name").value(favorite1.getProductId()))
                .andExpect(jsonPath("$[1].id").value(favorite2.getUserId()))
                .andExpect(jsonPath("$[1].name").value(favorite2.getProductId()));
    }

    @Test
    @DisplayName("GET /api/favorites/{id} - should return favorite by ID")
    void getFavoriteById() throws Exception {
        when(favoriteService.getFavoriteById(1L)).thenReturn(favorite1);
        when(modelMapper.map(favorite1, FavoriteResponseDto.class)).thenReturn(favoriteResponseDto1);

        mockMvc.perform(get("/api/favorites/{favoriteId}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(favorite1.getUserId()))
                .andExpect(jsonPath("$.name").value(favorite1.getProductId()));
    }

    @Test
    @DisplayName("POST /api/favorites - should create favorite")
    void createFavorite() throws Exception {
        when(modelMapper.map(favoriteCreateRequestDto, Favorite.class)).thenReturn(favorite1);
        when(favoriteService.createFavorite(favorite1)).thenReturn(favorite1);
        when(modelMapper.map(favorite1, FavoriteResponseDto.class)).thenReturn(favoriteResponseDto1);

        mockMvc.perform(post("/api/favorites")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(favoriteCreateRequestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(favorite1.getUserId()))
                .andExpect(jsonPath("$.name").value(favorite1.getProductId()));
    }

    @Test
    @DisplayName("DELETE /api/favorites/{id} - should delete favorite")
    void deleteFavorite() throws Exception {
        mockMvc.perform(delete("/api/favorites/{favoriteId}", 1L))
                .andExpect(status().isOk());
    }
}

