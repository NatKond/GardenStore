package de.telran.gardenStore.service;

import de.telran.gardenStore.AbstractTest;
import de.telran.gardenStore.entity.AppUser;
import de.telran.gardenStore.exception.UserNotFoundException;
import de.telran.gardenStore.exception.UserWithEmailAlreadyExistsException;
import de.telran.gardenStore.repository.UserRepository;
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
class UserServiceImplTest extends AbstractTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @DisplayName("Get all users")
    @Test
    void getAll() {

        List<AppUser> expected = List.of(user1, user2);

        when(userRepository.findAll()).thenReturn(List.of(user1, user2));

        List<AppUser> actual = userService.getAll();

        assertNotNull(actual);
        assertEquals(2, actual.size());
        assertTrue(actual.containsAll(expected));

        verify(userRepository).findAll();
    }

    @DisplayName("Get user by ID : positive case")
    @Test
    void getByIdPositiveCase() {

        Long userId = user1.getUserId();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user1));

        AppUser actual = userService.getById(userId);

        assertNotNull(actual);
        assertEquals(user1, actual);
        assertEquals(user1.getName(), actual.getName());
        assertEquals(user1.getEmail(), actual.getEmail());

        verify(userRepository).findById(userId);

    }

    @DisplayName("Get user by ID : negative case")
    @Test
    void getByIdNegativeCase() {

        Long userId = 99L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(UserNotFoundException.class, () -> userService.getById(userId));
        assertEquals("User with id " + userId + " not found", exception.getMessage());

        verify(userRepository).findById(userId);
    }

    @DisplayName("Create new user : positive case")
    @Test
    void createPositiveCase() {
        when(userRepository.save(userToCreate)).thenReturn(userCreated);
        when(userRepository.findByEmail(userToCreate.getEmail())).thenReturn(Optional.empty());

        AppUser actual = userService.create(userToCreate);

        assertNotNull(actual);
        assertEquals(userCreated.getUserId(), actual.getUserId());
        assertEquals(userCreated.getName(), actual.getName());
        assertEquals(userCreated.getEmail(), actual.getEmail());

        verify(userRepository).save(userToCreate);
    }

    @DisplayName("Create new user : negative case")
    @Test
    void createNegativeCase() {

        AppUser userToCreate = this.userToCreate.toBuilder()
                .email(user1.getEmail())
                .build();

        when(userRepository.findByEmail(userToCreate.getEmail())).thenReturn(Optional.of(user1));

        RuntimeException exception = assertThrows(UserWithEmailAlreadyExistsException.class, () -> userService.create(userToCreate));
        assertEquals("User with email " + userToCreate.getEmail() + " already exists", exception.getMessage());
    }

    @DisplayName("Update user : positive case")
    @Test
    void updatePositiveCase() {

        String emailToUpdate = "charlie.brown777@example.com";

        Long userId = user1.getUserId();
        String userEmail = user1.getEmail();

        AppUser userToUpdate = user1.toBuilder()
                .email(emailToUpdate)
                .build();

        AppUser userUpdated = userToUpdate.toBuilder()
                .userId(userId)
                .build();

        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(user1));
        when(userRepository.findByEmail(emailToUpdate)).thenReturn(Optional.empty());
        when(userRepository.save(userUpdated)).thenReturn(userUpdated);

        AppUser actual = userService.update(userToUpdate);

        assertNotNull(actual);
        assertEquals(userUpdated, actual);
        assertEquals(userUpdated.getName(), actual.getName());
        assertEquals(userUpdated.getEmail(), actual.getEmail());

        verify(userRepository).save(userUpdated);
    }

    @DisplayName("Update user : negative case")
    @Test
    void updateNegativeCase() {
        String userEmail = user1.getEmail();
        String emailToUpdate = user2.getEmail();

        AppUser userToUpdate = user1.toBuilder()
                .email(emailToUpdate)
                .build();

        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(user1));
        when(userRepository.findByEmail(emailToUpdate)).thenReturn(Optional.of(user2));

        RuntimeException exception = assertThrows(UserWithEmailAlreadyExistsException.class, () -> userService.update(userToUpdate));
        assertEquals("User with email " + emailToUpdate + " already exists", exception.getMessage());
    }

    @DisplayName("Delete user : positive case")
    @Test
    void delete() {
        String userEmail = user1.getEmail();

        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(user1));
        doNothing().when(userRepository).delete(user1);

        userService.delete();

        verify(userRepository).findByEmail(userEmail);
        verify(userRepository).delete(user1);
    }
}
