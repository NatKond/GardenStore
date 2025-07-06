package de.telran.gardenStore.service;

import de.telran.gardenStore.entity.AppUser;
import de.telran.gardenStore.enums.Role;
import de.telran.gardenStore.exception.UserNotFoundException;
import de.telran.gardenStore.exception.UserWithEmailAlreadyExistsException;
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

        userToCreate = AppUser.builder()
                .name("Charlie Brown")
                .email("charlie.brown@example.com")
                .phoneNumber("+1122334455")
                .passwordHash("12345")
                .role(Role.ROLE_USER)
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
        assertEquals(user1.getName(), actual.getName());
        assertEquals(user1.getEmail(), actual.getEmail());

        verify(userRepository).findById(userId);

    }

    @DisplayName("Get user by ID : negative case")
    @Test
    void getUserByIdNegativeCase() {

        Long userId = 99L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(UserNotFoundException.class, () -> userService.getUserById(userId));
        assertEquals("User with id " + userId + " not found", exception.getMessage());

        verify(userRepository).findById(userId);
    }

    @DisplayName("Create new user : positive case")
    @Test
    void createUserPositiveCase() {
        when(userRepository.save(userToCreate)).thenReturn(userCreated);
        when(userRepository.findByEmail(userToCreate.getEmail())).thenReturn(Optional.empty());

        AppUser actual = userService.createUser(userToCreate);

        assertNotNull(actual);
        assertEquals(userCreated.getUserId(), actual.getUserId());
        assertEquals(userCreated.getName(), actual.getName());
        assertEquals(userCreated.getEmail(), actual.getEmail());

        verify(userRepository).save(userToCreate);
    }

    @DisplayName("Create new user : negative case")
    @Test
    void createUserNegativeCase() {

        AppUser userToCreate = this.userToCreate.toBuilder()
                .email(user1.getEmail())
                .build();

        when(userRepository.findByEmail(userToCreate.getEmail())).thenReturn(Optional.of(user1));

        RuntimeException exception = assertThrows(UserWithEmailAlreadyExistsException.class, () -> userService.createUser(userToCreate));
        assertEquals("User with email " + userToCreate.getEmail() + " already exists", exception.getMessage());
    }

    @DisplayName("Update user : positive case")
    @Test
    void updateUserPositiveCase() {

        String emailToUpdate = "charlie.brown777@example.com";

        Long userId = userCreated.getUserId();

        AppUser userToUpdate = userToCreate.toBuilder()
                .email(emailToUpdate)
                .build();

        AppUser userUpdated = userToUpdate.toBuilder()
                .userId(userId)
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(userCreated));
        when(userRepository.findByEmail(emailToUpdate)).thenReturn(Optional.empty());
        when(userRepository.save(userUpdated)).thenReturn(userUpdated);

        AppUser actual = userService.updateUser(userId, userToUpdate);

        assertNotNull(actual);
        assertEquals(userUpdated, actual);
        assertEquals(userUpdated.getName(), actual.getName());
        assertEquals(userUpdated.getEmail(), actual.getEmail());

        verify(userRepository).save(userUpdated);
    }

    @DisplayName("Update user : negative case")
    @Test
    void updateUserNegativeCase() {

        String emailToUpdate = "alice.johnson@example.com";

        Long userId = userCreated.getUserId();

        AppUser userToUpdate = userToCreate.toBuilder()
                .email(emailToUpdate)
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(userCreated));
        when(userRepository.findByEmail(emailToUpdate)).thenReturn(Optional.of(user1));

        RuntimeException exception = assertThrows(UserWithEmailAlreadyExistsException.class, () -> userService.updateUser(userId, userToUpdate));
        assertEquals("User with email " + emailToUpdate + " already exists", exception.getMessage());
    }

    @DisplayName("Delete user by ID : positive case")
    @Test
    void deleteUserById() {

        when(userRepository.findById(1L)).thenReturn(Optional.of(user1));
        doNothing().when(userRepository).delete(user1);

        userService.deleteUserById(1L);

        verify(userRepository).findById(1L);
        verify(userRepository).delete(user1);
    }

    @DisplayName("Delete user by ID : negative case")
    @Test
    void deleteUserById_NonExistingUser_ThrowsException() {
        Long userId = 99L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(UserNotFoundException.class, () -> userService.deleteUserById(userId));
        assertEquals("User with id " + userId + " not found", exception.getMessage());

        verify(userRepository).findById(userId);
        verify(userRepository, never()).delete(any());
    }
}
