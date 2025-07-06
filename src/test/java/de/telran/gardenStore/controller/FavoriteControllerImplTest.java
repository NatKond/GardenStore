package de.telran.gardenStore.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.telran.gardenStore.dto.FavoriteCreateRequestDto;
import de.telran.gardenStore.dto.FavoriteResponseDto;
import de.telran.gardenStore.entity.AppUser;
import de.telran.gardenStore.entity.Category;
import de.telran.gardenStore.entity.Favorite;
import de.telran.gardenStore.entity.Product;
import de.telran.gardenStore.exception.FavoriteAlreadyExistsException;
import de.telran.gardenStore.exception.FavoriteNotFoundException;
import de.telran.gardenStore.service.FavoriteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.Mockito.*;
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

    private Category category1;
    private Category category2;

    private Product product1;
    private Product product2;
    private Product product3;

    private AppUser user1;

    private Favorite favorite1;
    private Favorite favorite2;

    private Favorite favoriteToCreate;
    private Favorite favoriteCreated;

    private FavoriteCreateRequestDto favoriteCreateRequestDto;
    private FavoriteResponseDto favoriteResponseDto1;
    private FavoriteResponseDto favoriteResponseDto2;
    private FavoriteResponseDto favoriteResponseCreatedDto;

    @BeforeEach
    void setUp() {
        category1 = Category.builder()
                .categoryId(1L)
                .name("Fertilizer")
                .build();

        category2 = Category.builder()
                .categoryId(2L)
                .name("Protective products and septic tanks")
                .build();

        product1 = Product.builder()
                .productId(1L)
                .name("All-Purpose Plant Fertilizer")
                .discountPrice(new BigDecimal("8.99"))
                .price(new BigDecimal("11.99"))
                .category(category1)
                .build();

        product2 = Product.builder()
                .productId(2L)
                .name("Organic Tomato Feed")
                .discountPrice(new BigDecimal("10.49"))
                .price(new BigDecimal("13.99"))
                .category(category1)
                .build();

        product3 = Product.builder()
                .productId(3L)
                .name("Slug & Snail Barrier Pellets")
                .discountPrice(new BigDecimal("5.75"))
                .price(new BigDecimal("7.50"))
                .category(category2)
                .build();

        user1 = AppUser.builder()
                .userId(1L)
                .name("Alice Johnson")
                .email("alice.johnson@example.com")
                .build();

        favorite1 = Favorite.builder()
                .favoriteId(1L)
                .user(user1)
                .product(product1)
                .build();

        favorite2 = Favorite.builder()
                .favoriteId(2L)
                .user(user1)
                .product(product2)
                .build();

        favoriteToCreate = Favorite.builder()
                .user(user1)
                .product(product3)
                .build();

        favoriteCreated = favoriteToCreate.toBuilder()
                .favoriteId(3L)
                .build();

        favoriteResponseDto1 = FavoriteResponseDto.builder()
                .favoriteId(favorite1.getFavoriteId())
                .productId(favorite1.getProduct().getProductId())
                .userId(favorite1.getUser().getUserId())
                .build();

        favoriteResponseDto2 = FavoriteResponseDto.builder()
                .favoriteId(favorite2.getFavoriteId())
                .productId(favorite2.getProduct().getProductId())
                .userId(favorite2.getUser().getUserId())
                .build();

        favoriteCreateRequestDto = FavoriteCreateRequestDto.builder()
                .userId(favoriteToCreate.getUser().getUserId())
                .productId(favoriteToCreate.getProduct().getProductId())
                .build();

        favoriteResponseCreatedDto = FavoriteResponseDto.builder()
                .productId(favoriteCreated.getProduct().getProductId())
                .userId(favoriteCreated.getUser().getUserId())
                .productId(favoriteCreated.getProduct().getProductId())
                .build();

    }

    @Test
    @DisplayName("GET /v1/favorites/{userId} - Get all favorites by userId")
    void getAllFavorites() throws Exception {

        Long userId = 1L;

        List<Favorite> favorites = List.of(favorite1, favorite2);

        List<FavoriteResponseDto> expected = List.of(favoriteResponseDto1, favoriteResponseDto2);

        when(favoriteService.getAllFavoritesByUser(userId)).thenReturn(favorites);
        when(modelMapper.map(favorite1, FavoriteResponseDto.class)).thenReturn(favoriteResponseDto1);
        when(modelMapper.map(favorite2, FavoriteResponseDto.class)).thenReturn(favoriteResponseDto2);

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

        when(modelMapper.map(favoriteCreateRequestDto, Favorite.class)).thenReturn(favoriteToCreate);
        when(favoriteService.createFavorite(favoriteToCreate)).thenReturn(favoriteCreated);
        when(modelMapper.map(favoriteCreated, FavoriteResponseDto.class)).thenReturn(favoriteResponseCreatedDto);

        mockMvc.perform(post("/v1/favorites/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(favoriteCreateRequestDto)))
                .andExpectAll(
                        status().isCreated(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json(objectMapper.writeValueAsString(favoriteCreateRequestDto)));


    }

    @Test
    @DisplayName("POST /v1/favorites - Create new favorite : negative Case")
    void createFavoriteNegativeCase() throws Exception {

        Long userId = favoriteToCreate.getUser().getUserId();

        when(modelMapper.map(favoriteCreateRequestDto, Favorite.class)).thenReturn(favoriteToCreate);
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