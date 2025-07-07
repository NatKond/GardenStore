package de.telran.gardenStore.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.telran.gardenStore.AbstractTest;
import de.telran.gardenStore.converter.Converter;
import de.telran.gardenStore.dto.FavoriteCreateRequestDto;
import de.telran.gardenStore.dto.FavoriteResponseDto;
import de.telran.gardenStore.entity.Favorite;
import de.telran.gardenStore.exception.FavoriteAlreadyExistsException;
import de.telran.gardenStore.exception.FavoriteNotFoundException;
import de.telran.gardenStore.service.FavoriteService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FavoriteControllerImpl.class)
@AutoConfigureMockMvc(addFilters = false)
class FavoriteControllerImplTest  extends AbstractTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private FavoriteService favoriteService;

    @MockitoBean
    private Converter<Favorite, FavoriteCreateRequestDto, FavoriteResponseDto,FavoriteResponseDto> favoriteConverter;

    @Test
    @DisplayName("GET /v1/favorites/{userId} - Get all favorites by userId")
    void getAllFavorites() throws Exception {

        Long userId = 1L;

        List<Favorite> favorites = List.of(favorite1, favorite2);

        List<FavoriteResponseDto> expected = List.of(favoriteResponseDto1, favoriteResponseDto2);

        when(favoriteService.getAllFavoritesByUser(userId)).thenReturn(favorites);
        when(favoriteConverter.convertEntityListToDtoList(favorites)).thenReturn(expected);

        mockMvc.perform(get("/v1/favorites/{userId}", userId))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json(objectMapper.writeValueAsString(expected)));
    }

    @Test
    @DisplayName("POST /v1/favorites - Create new favorite : positive Case")
    void createFavoritePositiveCase() throws Exception {

        Long userId = favoriteToCreate.getUser().getUserId();

        when(favoriteConverter.convertDtoToEntity(favoriteCreateRequestDto)).thenReturn(favoriteToCreate);
        when(favoriteService.createFavorite(favoriteToCreate)).thenReturn(favoriteCreated);
        when(favoriteConverter.convertEntityToDto(favoriteCreated)).thenReturn(favoriteResponseCreatedDto);

        mockMvc.perform(post("/v1/favorites/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(favoriteCreateRequestDto)))
                .andExpectAll(
                        status().isCreated(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json(objectMapper.writeValueAsString(favoriteResponseCreatedDto)));


    }

    @Test
    @DisplayName("POST /v1/favorites - Create new favorite : negative Case")
    void createFavoriteNegativeCase() throws Exception {

        Long userId = favoriteToCreate.getUser().getUserId();

        when(favoriteConverter.convertDtoToEntity(favoriteCreateRequestDto)).thenReturn(favoriteToCreate);
        when(favoriteService.createFavorite(favoriteToCreate))
                .thenThrow(new FavoriteAlreadyExistsException("Favorite with userId " + favoriteToCreate.getUser().getUserId() + " and productId " + favoriteToCreate.getProduct().getProductId() + " already exists"));

        mockMvc.perform(post("/v1/favorites/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(favoriteCreateRequestDto)))
                .andExpectAll(
                        status().isConflict(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.exception").value("FavoriteAlreadyExistsException"),
                        jsonPath("$.message").value("Favorite with userId " + favoriteToCreate.getUser().getUserId() + " and productId " + favoriteToCreate.getProduct().getProductId() + " already exists"),
                        jsonPath("$.status").value(HttpStatus.CONFLICT.value()));
    }

    @Test
    @DisplayName("DELETE /v1/favorites/{favoriteId} - Delete favorite by ID : positive case")
    void deleteFavoritePositiveCase() throws Exception {

        Long favoriteId = favorite1.getFavoriteId();

        mockMvc.perform(delete("/v1/favorites/{favoriteId}", favoriteId))
                .andExpect(status().isOk());

        verify(favoriteService).deleteFavoriteById(favoriteId);
    }

    @Test
    @DisplayName("DELETE /v1/favorites/{favoriteId} - Delete favorite by ID : negative case")
    void deleteFavoriteNegativeCase() throws Exception {

        Long favoriteId = 999L;

        doThrow(new FavoriteNotFoundException("Favorite with id " + favoriteId + " not found")).when(favoriteService).deleteFavoriteById(favoriteId);

        mockMvc.perform(delete("/v1/favorites/{favoriteId}", favoriteId))
                .andExpectAll(
                        status().isNotFound(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.exception").value("FavoriteNotFoundException"),
                        jsonPath("$.message").value("Favorite with id " + favoriteId + " not found"),
                        jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()));

        verify(favoriteService).deleteFavoriteById(favoriteId);
    }
}