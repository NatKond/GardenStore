package de.telran.gardenStore.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.telran.gardenStore.dto.UserCreateRequestDto;
import de.telran.gardenStore.dto.UserResponseDto;
import de.telran.gardenStore.entity.AppUser;
import de.telran.gardenStore.enums.Role;
import de.telran.gardenStore.exception.UserNotFoundException;
import de.telran.gardenStore.service.UserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@WebMvcTest(UserControllerImpl.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerImplTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @MockBean
    private ModelMapper modelMapper;

    private static AppUser user1;
    private static AppUser user2;
    private static AppUser newUser;
    private static AppUser createdUser;

    private static UserResponseDto userResponseDto1;
    private static UserResponseDto userResponseDto2;
    private static UserCreateRequestDto userCreateRequestDto;
    private static UserResponseDto userResponseCreatedDto;

    @BeforeAll
    static void setUp() {
        user1 = AppUser.builder()
                .userId(1L)
                .name("Alice Johnson")
                .email("alice.johnson@example.com")
                .phoneNumber("+1234567890")
                .passwordHash("12345")
                .role(Role.ROLE_USER)
                .build();

        user2 = AppUser.builder()
                .userId(2L)
                .name("Bob Smith")
                .email("bob.smith@example.com")
                .phoneNumber("+1987654321")
                .passwordHash("12345")
                .role(Role.ROLE_USER)
                .build();

        newUser = AppUser.builder()
                .name("Charlie Brown")
                .email("charlie.brown@example.com")
                .phoneNumber("+1122334455")
                .passwordHash("12345")
                .role(Role.ROLE_USER)
                .build();

        createdUser = AppUser.builder()
                .userId(3L)
                .name(newUser.getName())
                .email(newUser.getEmail())
                .phoneNumber(newUser.getPhoneNumber())
                .passwordHash(newUser.getPasswordHash())
                .role(newUser.getRole())
                .build();

        userResponseDto1 = UserResponseDto.builder()
                .userId(user1.getUserId())
                .name(user1.getName())
                .email(user1.getEmail())
                .phoneNumber(user1.getPhoneNumber())
                .role(user1.getRole().name())
                .build();

        userResponseDto2 = UserResponseDto.builder()
                .userId(user2.getUserId())
                .name(user2.getName())
                .email(user2.getEmail())
                .phoneNumber(user2.getPhoneNumber())
                .role(user2.getRole().name())
                .build();

        userCreateRequestDto = UserCreateRequestDto.builder()
                .name(newUser.getName())
                .email(newUser.getEmail())
                .phoneNumber(newUser.getPhoneNumber())
                .password(newUser.getPasswordHash())
                .build();

        userResponseCreatedDto = UserResponseDto.builder()
                .userId(createdUser.getUserId())
                .name(createdUser.getName())
                .email(createdUser.getEmail())
                .phoneNumber(createdUser.getPhoneNumber())
                .role(createdUser.getRole().name())
                .build();
    }

    @Test
    @DisplayName("GET /users - возвращает всех пользователей")
    void getAllUsers_ReturnsAllUsers() throws Exception {

        List<AppUser> users = List.of(user1, user2);
        when(userService.getAllUsers()).thenReturn(users);
        when(modelMapper.map(user1, UserResponseDto.class)).thenReturn(userResponseDto1);
        when(modelMapper.map(user2, UserResponseDto.class)).thenReturn(userResponseDto2);

        mockMvc.perform(get("/user")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].userId").value(user1.getUserId()))
                .andExpect(jsonPath("$[0].name").value(user1.getName()));
    }

    @Test
    @DisplayName("GET /user{id} - возвращает пользователя по ID")
    void getUserById_ExistingUser_ReturnsUser() throws Exception {
        // Подготовка
        when(userService.getUserById(1L)).thenReturn(user1);
        when(modelMapper.map(user1, UserResponseDto.class)).thenReturn(userResponseDto1);

        // Выполнение и проверка
        mockMvc.perform(get("/user/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(user1.getUserId()))
                .andExpect(jsonPath("$.name").value(user1.getName()))
                .andExpect(jsonPath("$.email").value(user1.getEmail()));
    }

    @Test
    @DisplayName("GET /user{id} - несуществующий пользователь - возвращает 404")
    void getUserById_NonExistingUser_Returns404() throws Exception {

        Long userId = 99L;
        when(userService.getUserById(userId)).thenThrow(new UserNotFoundException("User not found"));

        mockMvc.perform(get("/user/" + userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST /user/register - создает нового пользователя")
    void createUser_ValidRequest_ReturnsCreatedUser() throws Exception {

        when(modelMapper.map(userCreateRequestDto, AppUser.class)).thenReturn(newUser);
        when(userService.createUser(newUser)).thenReturn(createdUser);
        when(modelMapper.map(createdUser, UserResponseDto.class)).thenReturn(userResponseCreatedDto);

        mockMvc.perform(post("/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userCreateRequestDto)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userId").value(createdUser.getUserId()))
                .andExpect(jsonPath("$.name").value(createdUser.getName()))
                .andExpect(jsonPath("$.email").value(createdUser.getEmail()));
    }

    @Test
    @DisplayName("DELETE /user{id} - удаляет пользователя")
    void deleteUserById_ExistingUser_Returns200() throws Exception {
        // Подготовка
        Long userId = 1L;
        doNothing().when(userService).deleteUserById(userId);

        // Выполнение и проверка
        mockMvc.perform(delete("/user/" + userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }
}