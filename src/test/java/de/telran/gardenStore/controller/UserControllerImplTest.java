package de.telran.gardenStore.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.telran.gardenStore.AbstractTest;
import de.telran.gardenStore.converter.Converter;
import de.telran.gardenStore.dto.UserCreateRequestDto;
import de.telran.gardenStore.dto.UserResponseDto;
import de.telran.gardenStore.dto.UserShortResponseDto;
import de.telran.gardenStore.entity.AppUser;
import de.telran.gardenStore.exception.UserNotFoundException;
import de.telran.gardenStore.exception.UserWithEmailAlreadyExistsException;
import de.telran.gardenStore.service.UserService;
import de.telran.gardenStore.service.security.AuthenticationService;
import de.telran.gardenStore.service.security.JwtService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserControllerImpl.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerImplTest extends AbstractTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private AuthenticationService authenticationService;

    @MockitoBean
    private Converter<AppUser, UserCreateRequestDto, UserResponseDto, UserShortResponseDto> userConverter;

    @Test
    @DisplayName("GET /v1/users - Get all users")
    void getAll() throws Exception {

        List<AppUser> users = List.of(user1, user2);
        List<UserShortResponseDto> expected = List.of(userShortResponseDto1, userShortResponseDto2);

        when(userService.getAll()).thenReturn(users);
        when(userConverter.convertEntityListToDtoList(users)).thenReturn(expected);

        mockMvc.perform(get("/v1/users"))
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json(objectMapper.writeValueAsString(expected)));
    }

    @Test
    @DisplayName("GET /v1/user/me - Get current user")
    void getCurrent() throws Exception {
        when(userService.getCurrent()).thenReturn(user1);
        when(userConverter.convertEntityToDto(user1)).thenReturn(userResponseDto1);

        mockMvc.perform(get("/v1/users/me")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json(objectMapper.writeValueAsString(userResponseDto1)));
    }

    @Test
    @DisplayName("GET /v1/user/{userId} - Get current user by ID : positive case")
    void getByIdPositiveCase() throws Exception {
        Long userId = user1.getUserId();

        when(userService.getById(userId)).thenReturn(user1);
        when(userConverter.convertEntityToDto(user1)).thenReturn(userResponseDto1);

        mockMvc.perform(get("/v1/users/{userId}", userId))
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
        when(userService.getById(userId)).thenThrow(new UserNotFoundException("User with id " + userId + " not found"));

        mockMvc.perform(get("/v1/users/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
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

        when(userConverter.convertDtoToEntity(userCreateRequestDto)).thenReturn(userToCreate);
        when(userService.create(userToCreate)).thenReturn(userCreated);
        when(userConverter.convertEntityToDto(userCreated)).thenReturn(userResponseCreatedDto);

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

        when(userConverter.convertDtoToEntity(userCreateRequestDto)).thenReturn(userToCreate);
        when(userService.create(userToCreate)).thenThrow(new UserWithEmailAlreadyExistsException("User with email " + userToCreate.getEmail() + " already exists"));

        mockMvc.perform(post("/v1/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userCreateRequestDto)))
                .andDo(print())
                .andExpectAll(
                        status().isConflict(),
                        jsonPath("$.exception").value("UserWithEmailAlreadyExistsException"),
                        jsonPath("$.message").value("User with email " + userToCreate.getEmail() + " already exists"),
                        jsonPath("$.status").value(HttpStatus.CONFLICT.value()));
    }

    @Test
    @DisplayName("PUT /v1/users - Update current user")
    void update() throws Exception {

        String emailToUpdate = "charlie.brown777@example.com";

        Long userId = userCreated.getUserId();

        AppUser userToUpdate = userToCreate.toBuilder()
                .email(emailToUpdate)
                .build();

        AppUser userUpdated = userToUpdate.toBuilder()
                .userId(userId)
                .build();

        UserCreateRequestDto userUpdateRequestDto = userCreateRequestDto.toBuilder()
                .email(emailToUpdate)
                .build();

        UserResponseDto userResponseUpdatedDto = userResponseCreatedDto.toBuilder()
                .email(emailToUpdate)
                .build();


        when(userConverter.convertDtoToEntity(userUpdateRequestDto)).thenReturn(userToUpdate);
        when(userService.update(userToUpdate)).thenReturn(userUpdated);
        when(userService.getCurrent()).thenReturn(userUpdated);
        when(userConverter.convertEntityToDto(userUpdated)).thenReturn(userResponseUpdatedDto);

        mockMvc.perform(put("/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userUpdateRequestDto)))
                .andDo(print())
                .andExpectAll(
                        status().isAccepted(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json(objectMapper.writeValueAsString(userResponseUpdatedDto)));
    }

    @Test
    @DisplayName("DELETE /v1/users/{userId} - Delete current user")
    void deleteUserByI() throws Exception {

        doNothing().when(userService).delete();

        mockMvc.perform(delete("/v1/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
        verify(userService).delete();
    }
}