package de.telran.gardenStore.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.telran.gardenStore.AbstractTest;
import de.telran.gardenStore.dto.UserCreateRequestDto;
import de.telran.gardenStore.dto.UserResponseDto;
import de.telran.gardenStore.dto.UserShortResponseDto;
import de.telran.gardenStore.dto.security.LoginRequest;
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

import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(printOnlyOnFailure = false)
@Transactional
public class UserIntegrationTest extends AbstractTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("GET /v1/users - Get all users")
    void getAll() throws Exception {
        List<UserShortResponseDto> expected = List.of(userShortResponseDto1, userShortResponseDto2);

        mockMvc.perform(get("/v1/users")
                        .with(httpBasic("alice.johnson@example.com", "12345")))
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json(objectMapper.writeValueAsString(expected)));
    }

    @Test
    @DisplayName("GET /v1/user/me - Get current user")
    void getCurrent() throws Exception {

        mockMvc.perform(get("/v1/users/me")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(httpBasic("alice.johnson@example.com", "12345")))
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json(objectMapper.writeValueAsString(userResponseDto1)));
    }

    @Test
    @DisplayName("POST /v1/users/login - Login register user : positive case")
    void loginPositiveCase() throws Exception {
        LoginRequest loginRequest = LoginRequest.builder()
                .email(user1.getEmail())
                .password("12345")
                .build();

        mockMvc.perform(post("/v1/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andDo(print())
                .andExpectAll(
                        status().isAccepted(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().string(containsString("token")));
    }

    @Test
    @DisplayName("POST /v1/users/login - Login register user : negative case")
    void loginNegativeCase() throws Exception {
        LoginRequest loginRequest = LoginRequest.builder()
                .email(userCreated.getEmail())
                .password("12345")
                .build();
        String email = loginRequest.getEmail();

        mockMvc.perform(post("/v1/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andDo(print())
                .andExpectAll(
                        status().isNotFound(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.exception").value("UserNotFoundException"),
                        jsonPath("$.message").value("User with email " + email + " not found"),
                        jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()));
    }

    @Test
    @DisplayName("GET /v1/user/{userId} - Get current user by ID : positive case")
    void getByIdPositiveCase() throws Exception {
        Long userId = user1.getUserId();

        mockMvc.perform(get("/v1/users/{userId}", userId)
                        .with(httpBasic("alice.johnson@example.com", "12345")))
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json(objectMapper.writeValueAsString(userResponseDto1)));
    }

    @Test
    @DisplayName("GET /v1/user/{userId} - Get user by ID : negative case")
    void getByIdNegativeCase() throws Exception {
        Long userId = 99L;

        mockMvc.perform(get("/v1/users/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(httpBasic("alice.johnson@example.com", "12345")))
                .andDo(print())
                .andExpectAll(
                        status().isNotFound(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.exception").value("UserNotFoundException"),
                        jsonPath("$.message").value("User with id " + userId + " not found"),
                        jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()));
    }

    @Test
    @DisplayName("POST /v1/users/register - Create new user : positive case")
    void createPositiveCase() throws Exception {

        mockMvc.perform(post("/v1/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userCreateRequestDto)))
                .andDo(print())
                .andExpectAll(
                        status().isCreated(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json(objectMapper.writeValueAsString(userResponseCreatedDto)));
    }

    @Test
    @DisplayName("POST /v1/users/register - Create new user : negative case")
    void createNegativeCase() throws Exception {
        UserCreateRequestDto userToCreateInvalid = userCreateRequestDto.toBuilder().email(user1.getEmail()).build();

        mockMvc.perform(post("/v1/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userToCreateInvalid)))
                .andDo(print())
                .andExpectAll(
                        status().isConflict(),
                        jsonPath("$.exception").value("UserWithEmailAlreadyExistsException"),
                        jsonPath("$.message").value("User with email " + userToCreateInvalid.getEmail() + " already exists"),
                        jsonPath("$.status").value(HttpStatus.CONFLICT.value()));
    }

    @Test
    @DisplayName("PUT /v1/users - Update current user")
    void update() throws Exception {
        String phoneToUpdate = "+1234567890";

        UserCreateRequestDto userUpdateRequestDto = UserCreateRequestDto.builder()
                .name(user1.getName())
                .email(user1.getEmail())
                .phone(phoneToUpdate)
                .password("12345")
                .build();

        UserResponseDto userResponseUpdatedDto = userResponseDto1.toBuilder()
                .phone(phoneToUpdate)
                .build();

        mockMvc.perform(put("/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userUpdateRequestDto))
                        .with(httpBasic("alice.johnson@example.com", "12345")))
                .andDo(print())
                .andExpectAll(
                        status().isAccepted(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json(objectMapper.writeValueAsString(userResponseUpdatedDto)));
    }

    @Test
    @DisplayName("DELETE /v1/users/{userId} - Delete current user : negative case")
    void deleteUserByI() throws Exception {

        mockMvc.perform(delete("/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(httpBasic("alice.johnson@example.com", "12345")))
                .andDo(print())
                .andExpectAll(
                        status().isConflict(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.exception").value("UserDeletionNotAllowedException"),
                        jsonPath("$.message").value("User cannot be deleted because they have placed orders"),
                        jsonPath("$.status").value(HttpStatus.CONFLICT.value()));
    }
}
