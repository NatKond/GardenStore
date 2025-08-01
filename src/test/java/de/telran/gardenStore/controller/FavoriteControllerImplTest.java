package de.telran.gardenStore.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.telran.gardenStore.AbstractTest;
import de.telran.gardenStore.converter.ConverterEntityToDto;
import de.telran.gardenStore.dto.FavoriteResponseDto;
import de.telran.gardenStore.entity.Favorite;
import de.telran.gardenStore.exception.FavoriteAlreadyExistsException;
import de.telran.gardenStore.exception.FavoriteNotFoundException;
import de.telran.gardenStore.service.FavoriteService;
import de.telran.gardenStore.service.security.JwtService;
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
    private JwtService jwtService;

    @MockitoBean
    private FavoriteService favoriteService;

    @MockitoBean
    private ConverterEntityToDto<Favorite, FavoriteResponseDto, FavoriteResponseDto> favoriteConverter;

    @Test
    @DisplayName("GET /v1/favorites - Get all favorites for current user")
    void getAllFavorites() throws Exception {

        List<Favorite> favorites = List.of(favorite1, favorite2);

        List<FavoriteResponseDto> expected = List.of(favoriteResponseDto1, favoriteResponseDto2);

        when(favoriteService.getAllForCurrentUser()).thenReturn(favorites);
        when(favoriteConverter.convertEntityListToDtoList(favorites)).thenReturn(expected);

        mockMvc.perform(get("/v1/favorites"))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json(objectMapper.writeValueAsString(expected)));
    }

    @Test
    @DisplayName("POST /v1/favorites/{productId} - Create new favorite : positive case")
    void createPositiveCase() throws Exception {
        Long productId = favoriteToCreate.getProduct().getProductId();

        when(favoriteService.create(productId)).thenReturn(favoriteCreated);
        when(favoriteConverter.convertEntityToDto(favoriteCreated)).thenReturn(favoriteResponseCreatedDto);

        mockMvc.perform(post("/v1/favorites/{productId}",productId))
                .andExpectAll(
                        status().isCreated(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json(objectMapper.writeValueAsString(favoriteResponseCreatedDto)));
    }

    @Test
    @DisplayName("POST /v1/favorites/{productId} - Create new favorite : negative Case")
    void createNegativeCase() throws Exception {
        Long productId = favoriteToCreate.getProduct().getProductId();

        when(favoriteService.create(productId))
                .thenThrow(new FavoriteAlreadyExistsException("Favorite with userId " + favoriteToCreate.getUser().getUserId() + " and productId " + favoriteToCreate.getProduct().getProductId() + " already exists"));

        mockMvc.perform(post("/v1/favorites/{productId}", productId))
                .andExpectAll(
                        status().isConflict(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.exception").value("FavoriteAlreadyExistsException"),
                        jsonPath("$.message").value("Favorite with userId " + favoriteToCreate.getUser().getUserId() + " and productId " + favoriteToCreate.getProduct().getProductId() + " already exists"),
                        jsonPath("$.status").value(HttpStatus.CONFLICT.value()));
    }

    @Test
    @DisplayName("DELETE /v1/favorites/{favoriteId} - Delete favorite by ID : positive case")
    void deletePositiveCase() throws Exception {

        Long favoriteId = favorite1.getFavoriteId();

        mockMvc.perform(delete("/v1/favorites/{favoriteId}", favoriteId))
                .andExpect(status().isOk());

        verify(favoriteService).deleteById(favoriteId);
    }

    @Test
    @DisplayName("DELETE /v1/favorites/{favoriteId} - Delete favorite by ID : negative case")
    void deleteNegativeCase() throws Exception {

        Long favoriteId = 999L;

        doThrow(new FavoriteNotFoundException("Favorite with id " + favoriteId + " not found")).when(favoriteService).deleteById(favoriteId);

        mockMvc.perform(delete("/v1/favorites/{favoriteId}", favoriteId))
                .andExpectAll(
                        status().isNotFound(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.exception").value("FavoriteNotFoundException"),
                        jsonPath("$.message").value("Favorite with id " + favoriteId + " not found"),
                        jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()));

        verify(favoriteService).deleteById(favoriteId);
    }
}