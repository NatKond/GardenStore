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

    @DisplayName("Get all users - successful scenario")
    @Test
    void getAllUsers_ReturnsAllUsers() {
        when(userRepository.findAll()).thenReturn(List.of(user1, user2));

        List<AppUser> actualUsers = userService.getAllUsers();

        assertNotNull(actualUsers, "User list should not be null");
        assertEquals(2, actualUsers.size(), "Should return 2 users");
        assertTrue(actualUsers.containsAll(List.of(user1, user2)), "All users should be returned");

        verify(userRepository, times(1)).findAll();
        verifyNoMoreInteractions(userRepository);
    }

    @DisplayName("Get user by ID - user exists")
    @Test
    void getUserById_ExistingUser_ReturnsUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user1));

        AppUser actualUser = userService.getUserById(1L);

        assertNotNull(actualUser, "User should not be null");
        assertEquals(user1, actualUser, "Returned user should match the expected one");

        verify(userRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(userRepository);
    }

    @DisplayName("Get user by ID - user does not exist")
    @Test
    void getUserById_NonExistingUser_ThrowsException() {

        Long userId = 99L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getUserById(userId),
                "UserNotFoundException should be thrown");

        verify(userRepository).findById(userId);
    }

    @DisplayName("Create user - successful scenario")
    @Test
    void createUser_ValidUser_ReturnsSavedUser() {
        AppUser savedUser = AppUser.builder()
                .userId(3L)
                .name(newUser.getName())
                .email(newUser.getEmail())
                .phoneNumber(newUser.getPhoneNumber())
                .passwordHash(newUser.getPasswordHash())
                .role(newUser.getRole())
                .build();

        when(userRepository.save(newUser)).thenReturn(savedUser);

        AppUser actualUser = userService.createUser(newUser);

        assertNotNull(actualUser, "Saved user should not be null");
        assertEquals(savedUser.getUserId(), actualUser.getUserId(), "Saved user ID should match");
        assertEquals(savedUser.getName(), actualUser.getName(), "User names should match");

        verify(userRepository, times(1)).save(newUser);
        verifyNoMoreInteractions(userRepository);
    }

    @DisplayName("Delete user by ID - successful scenario")
    @Test
    void deleteUserById_ExistingUser_DeletesUser() {

        when(userRepository.findById(1L)).thenReturn(Optional.of(user1));
        doNothing().when(userRepository).delete(user1);

        userService.deleteUserById(1L);

        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).delete(user1);
        verifyNoMoreInteractions(userRepository);
    }

    @DisplayName("Delete user by ID - non-existing user - throws exception")
    @Test
    void deleteUserById_NonExistingUser_ThrowsException() {
        Long userId = 99L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.deleteUserById(userId));
        verify(userRepository).findById(userId);
        verify(userRepository, never()).delete(any());
    }
}
