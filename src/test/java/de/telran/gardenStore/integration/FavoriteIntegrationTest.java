package de.telran.gardenStore.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.telran.gardenStore.AbstractTest;
import de.telran.gardenStore.dto.FavoriteResponseDto;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc(printOnlyOnFailure = false)
@Transactional
public class FavoriteIntegrationTest extends AbstractTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    @DisplayName("GET /v1/favorites - Get all favorites for current user")
    void getAllFavorites() throws Exception {
        List<FavoriteResponseDto> expected = List.of(favoriteResponseDto1, favoriteResponseDto2);
        mockMvc.perform(get("/v1/favorites")
                        .with(httpBasic("alice.johnson@example.com", "12345")))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json(objectMapper.writeValueAsString(expected)));
    }

    @Test
    @DisplayName("POST /v1/favorites/{productId} - Create new favorite : positive case")
    void createPositiveCase() throws Exception {
        Long productId = product3.getProductId();

        mockMvc.perform(post("/v1/favorites/{productId}", productId)
                        .with(httpBasic("alice.johnson@example.com", "12345")))
                .andExpectAll(
                        status().isCreated(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json(objectMapper.writeValueAsString(favoriteResponseCreatedDto)));
    }

    @Test
    @DisplayName("POST /v1/favorites/{productId} - Create new favorite : negative Case")
    void createNegativeCase() throws Exception {
        Long productId = product1.getProductId();

        mockMvc.perform(post("/v1/favorites/{productId}", productId)
                        .with(httpBasic("alice.johnson@example.com", "12345")))
                .andExpectAll(
                        status().isConflict(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.exception").value("FavoriteAlreadyExistsException"),
                        jsonPath("$.message").value("Favorite with userId " + favoriteToCreate.getUser().getUserId() + " and productId " + productId + " already exists"),
                        jsonPath("$.status").value(HttpStatus.CONFLICT.value()));
    }

    @Test
    @DisplayName("DELETE /v1/favorites/{favoriteId} - Delete favorite by ID : positive case")
    void deletePositiveCase() throws Exception {
        Long favoriteId = favorite1.getFavoriteId();

        mockMvc.perform(delete("/v1/favorites/{favoriteId}", favoriteId)
                        .with(httpBasic("alice.johnson@example.com", "12345")))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("DELETE /v1/favorites/{favoriteId} - Delete favorite by ID : negative case")
    void deleteNegativeCase() throws Exception {
        Long favoriteId = 999L;

        mockMvc.perform(delete("/v1/favorites/{favoriteId}", favoriteId)
                        .with(httpBasic("alice.johnson@example.com", "12345")))
                .andExpectAll(
                        status().isNotFound(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.exception").value("FavoriteNotFoundException"),
                        jsonPath("$.message").value("Favorite with id " + favoriteId + " not found"),
                        jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()));
    }
}
