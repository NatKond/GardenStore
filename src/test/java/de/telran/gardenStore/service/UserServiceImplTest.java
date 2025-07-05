package de.telran.gardenStore.service;

import de.telran.gardenStore.entity.AppUser;
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
    private AppUser userToCreate;
    private AppUser userCreated;

    @BeforeEach
    void setUp() {
        user1 = AppUser.builder()
                .userId(1L)
                .name("Alice Johnson")
                .email("alice.johnson@example.com")
                .phoneNumber("+1234567890")
                .passwordHash("12345")
                .build();

        user2 = AppUser.builder()
                .userId(2L)
                .name("Bob Smith")
                .email("bob.smith@example.com")
                .phoneNumber("+1987654321")
                .passwordHash("12345")
                .build();

        userToCreate = AppUser.builder()
                .name("Charlie Brown")
                .email("charlie.brown@example.com")
                .phoneNumber("+1122334455")
                .passwordHash("12345")
                .build();

        userCreated = AppUser.builder()
                .userId(3L)
                .name(userToCreate.getName())
                .email(userToCreate.getEmail())
                .phoneNumber(userToCreate.getPhoneNumber())
                .passwordHash(userToCreate.getPasswordHash())
                .role(userToCreate.getRole())
                .build();
    }

    @DisplayName("Get all users")
    @Test
    void getAllUsers() {

        List<AppUser> expected = List.of(user1, user2);

        when(userRepository.findAll()).thenReturn(List.of(user1, user2));

        List<AppUser> actual = userService.getAllUsers();

        assertNotNull(actual);
        assertEquals(2, actual.size());
        assertTrue(actual.containsAll(expected));

        verify(userRepository).findAll();
    }

    @DisplayName("Get user by ID : positive case")
    @Test
    void getUserById() {

        Long userId = user1.getUserId();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user1));

        AppUser actual = userService.getUserById(userId);

        assertNotNull(actual);
        assertEquals(user1, actual);

        verify(userRepository).findById(userId);

    }

    @DisplayName("Get user by ID : negative case")
    @Test
    void getUserByIdNegativeCase() {

        Long userId = 99L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(UserNotFoundException.class, () -> userService.getUserById(userId));
        assertEquals("User with id = " + userId + " not found", exception.getMessage());

        verify(userRepository).findById(userId);
    }

    @DisplayName("Create user : positive case")
    @Test
    void createUser() {

        when(userRepository.save(userToCreate)).thenReturn(userCreated);

        AppUser actualUser = userService.createUser(userToCreate);

        assertNotNull(actualUser);
        assertEquals(userCreated.getUserId(), actualUser.getUserId());
        assertEquals(userCreated.getName(), actualUser.getName());

        verify(userRepository).save(userToCreate);
    }

    @DisplayName("Create user : negative case")
    @Test
    void updateUser() {

        when(userRepository.save(userToCreate)).thenReturn(userCreated);

        AppUser actualUser = userService.createUser(userToCreate);

        assertNotNull(actualUser);
        assertEquals(userCreated.getUserId(), actualUser.getUserId());
        assertEquals(userCreated.getName(), actualUser.getName());

        verify(userRepository).save(userToCreate);
    }

    @DisplayName("Delete user by ID : positive case")
    @Test
    void deleteUserById() {

        when(userRepository.findById(1L)).thenReturn(Optional.of(user1));
        doNothing().when(userRepository).delete(user1);

        userService.deleteUserById(1L);

        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).delete(user1);
        verifyNoMoreInteractions(userRepository);
    }

    @DisplayName("Delete user by ID : negative case")
    @Test
    void deleteUserById_NonExistingUser_ThrowsException() {
        Long userId = 99L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(UserNotFoundException.class, () -> userService.deleteUserById(userId));
        assertEquals("User with id = " + userId + " not found", exception.getMessage());

        verify(userRepository).findById(userId);
        verify(userRepository, never()).delete(any());
    }
}
