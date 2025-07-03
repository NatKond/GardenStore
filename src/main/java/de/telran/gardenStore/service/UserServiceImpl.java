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
    public AppUser createUser(AppUser appUser) {
        if (userRepository.findByEmail(appUser.getEmail()).isPresent()) {
            throw new UserWithEmailAlreadyExistsException("User with email " + appUser.getEmail() + " already exists.");
        }
        return userRepository.save(appUser);
    }

    @Override
    public AppUser updateUser(Long userId, AppUser appUser) {

        AppUser existing = getUserById(userId);

        existing.setName(appUser.getName());
        existing.setEmail(appUser.getEmail());
        existing.setPhoneNumber(appUser.getPhoneNumber());
        existing.setPasswordHash(appUser.getPasswordHash());
        existing.setRole(appUser.getRole());

        return userRepository.save(existing);
    }

    @Override
    public void deleteUserById(Long userId) {
        userRepository.delete(getUserById(userId));
    }
}
