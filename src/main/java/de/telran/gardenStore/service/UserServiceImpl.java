package de.telran.gardenStore.service;

import de.telran.gardenStore.entity.AppUser;
import de.telran.gardenStore.exception.UserNotFoundException;
import de.telran.gardenStore.exception.UserWithEmailAlreadyExistsException;
import de.telran.gardenStore.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public List<AppUser> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public AppUser getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User with id " + userId + " not found"));
    }

    @Override
    public AppUser getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User with email " + email + " not found"));
    }

    @Override
    public AppUser createUser(AppUser newUser) {
        emailCheck(newUser.getEmail());
        return userRepository.save(newUser);
    }

    @Override
    public AppUser updateUser(Long userId, AppUser updatedUser) {
        AppUser existing = getUserById(userId);

        if (!existing.getEmail().equals(updatedUser.getEmail())) {
            emailCheck(updatedUser.getEmail());
        }

        existing.setName(updatedUser.getName());
        existing.setEmail(updatedUser.getEmail());
        existing.setPhoneNumber(updatedUser.getPhoneNumber());
        existing.setPasswordHash(updatedUser.getPasswordHash());
        existing.setRole(updatedUser.getRole());

        return userRepository.save(existing);
    }

    @Override
    public void deleteUserById(Long userId) {
        userRepository.delete(getUserById(userId));
    }

    private void emailCheck(String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new UserWithEmailAlreadyExistsException("User with email " + email + " already exists.");
        }
    }
}
