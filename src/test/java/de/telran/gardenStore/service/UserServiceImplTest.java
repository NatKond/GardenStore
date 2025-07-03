package de.telran.gardenStore.service;

import de.telran.gardenStore.entity.AppUser;
import de.telran.gardenStore.enums.Role;
import de.telran.gardenStore.exception.UserNotFoundException;
import de.telran.gardenStore.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private AppUser user1;
    private AppUser user2;
    private AppUser newUser;

    @BeforeEach
    void setUp() {
        // Инициализация тестовых данных перед каждым тестом
        user1 = AppUser.builder()
                .userId(1L)
                .name("Alice Johnson")
                .email("alice.johnson@example.com")
                .phoneNumber("+1234567890")
                .passwordHash("12345")
                .role(Role.valueOf("ROLE_USER"))
                .build();

        user2 = AppUser.builder()
                .userId(2L)
                .name("Bob Smith")
                .email("bob.smith@example.com")
                .phoneNumber("+1987654321")
                .passwordHash("12345")
                .role(Role.valueOf("ROLE_USER"))
                .build();

        newUser = AppUser.builder()
                .name("Charlie Brown")
                .email("charlie.brown@example.com")
                .phoneNumber("+1122334455")
                .passwordHash("12345")
                .role(Role.valueOf("ROLE_USER"))
                .build();
    }

    @DisplayName("Получение всех пользователей - успешный сценарий")
    @Test
    void getAllUsers_ReturnsAllUsers() {
        // Подготовка: задаем поведение мок-репозитория
        when(userRepository.findAll()).thenReturn(List.of(user1, user2));

        // Действие: вызываем тестируемый метод
        List<AppUser> actualUsers = userService.getAllUsers();

        // Проверки:
        assertNotNull(actualUsers, "Список пользователей не должен быть null");
        assertEquals(2, actualUsers.size(), "Должно вернуться 2 пользователя");
        assertTrue(actualUsers.containsAll(List.of(user1, user2)), "Должны вернуться все пользователи");

        // Проверка вызова репозитория
        verify(userRepository, times(1)).findAll();
        verifyNoMoreInteractions(userRepository);
    }

    @DisplayName("Получение пользователя по ID - пользователь существует")
    @Test
    void getUserById_ExistingUser_ReturnsUser() {
        // Подготовка
        when(userRepository.findById(1L)).thenReturn(Optional.of(user1));

        // Действие
        AppUser actualUser = userService.getUserById(1L);

        // Проверки
        assertNotNull(actualUser, "Пользователь не должен быть null");
        assertEquals(user1, actualUser, "Должен вернуться ожидаемый пользователь");

        // Проверка вызова репозитория
        verify(userRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(userRepository);
    }

    @DisplayName("Получение пользователя по ID - пользователь не существует")
    @Test
    void getUserById_NonExistingUser_ThrowsException() {
        // Подготовка
        Long userId = 99L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Действие и проверка исключения
        assertThrows(UserNotFoundException.class, () -> userService.getUserById(userId),
                "Должно быть выброшено UserNotFoundException");

        // Проверка вызова репозитория - ИЗМЕНЕНО НА findById
        verify(userRepository).findById(userId);
    }
    @DisplayName("Создание пользователя - успешный сценарий")
    @Test
    void createUser_ValidUser_ReturnsSavedUser() {
        // Подготовка
        AppUser savedUser = AppUser.builder()
                .userId(3L)
                .name(newUser.getName())
                .email(newUser.getEmail())
                .phoneNumber(newUser.getPhoneNumber())
                .passwordHash(newUser.getPasswordHash())
                .role(newUser.getRole())
                .build();

        when(userRepository.save(newUser)).thenReturn(savedUser);

        // Действие
        AppUser actualUser = userService.createUser(newUser);

        // Проверки
        assertNotNull(actualUser, "Сохраненный пользователь не должен быть null");
        assertEquals(savedUser.getUserId(), actualUser.getUserId(), "ID сохраненного пользователя должен совпадать");
        assertEquals(savedUser.getName(), actualUser.getName(), "Имена должны совпадать");

        // Проверка вызова репозитория
        verify(userRepository, times(1)).save(newUser);
        verifyNoMoreInteractions(userRepository);
    }

    @DisplayName("Удаление пользователя по ID - успешный сценарий")
    @Test
    void deleteUserById_ExistingUser_DeletesUser() {
        // Подготовка
        when(userRepository.findById(1L)).thenReturn(Optional.of(user1));
        doNothing().when(userRepository).delete(user1);

        // Действие
        userService.deleteUserById(1L);

        // Проверка вызовов репозитория
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).delete(user1);
        verifyNoMoreInteractions(userRepository);
    }

    @DisplayName("Test deleteUserById - non-existing user - throws exception")
    @Test
    void deleteUserById_NonExistingUser_ThrowsException() {
        Long userId = 99L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.deleteUserById(userId));
        verify(userRepository).findById(userId);
        verify(userRepository, never()).delete(any());
    }
}