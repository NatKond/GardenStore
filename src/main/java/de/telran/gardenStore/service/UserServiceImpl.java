package de.telran.gardenStore.service;

import de.telran.gardenStore.entity.AppUser;
import de.telran.gardenStore.exception.UserNotFoundException;
import de.telran.gardenStore.exception.UserWithEmailAlreadyExistsException;
import de.telran.gardenStore.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public List<AppUser> getAll() {
        return userRepository.findAll();
    }

    @Override
    public AppUser getById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User with id " + userId + " not found"));
    }

    @Override
    public AppUser getByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User with email " + email + " not found"));
    }

    public AppUser getCurrent() {
        return getByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
    }

    @Override
    public AppUser create(AppUser user) {
        checkUserEmailIsUnique(user.getEmail());
        AppUser savedUser = userRepository.save(user);
        log.debug("UserId =  {}: User created", savedUser.getUserId());
        return savedUser;
    }

    @Override
    public AppUser update(AppUser user) {
        AppUser existing = getCurrent();

        if (!existing.getEmail().equals(user.getEmail())) {
            checkUserEmailIsUnique(user.getEmail());
        }

        existing.setName(user.getName());
        existing.setEmail(user.getEmail());
        existing.setPhoneNumber(user.getPhoneNumber());
        existing.setPasswordHash(user.getPasswordHash());

        AppUser savedUser = userRepository.save(existing);
        log.debug("UserId =  {}: User updated", savedUser.getUserId());
        return savedUser;
    }

    @Override
    public void delete() {
        userRepository.delete(getCurrent());
    }

    private void checkUserEmailIsUnique(String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new UserWithEmailAlreadyExistsException("User with email " + email + " already exists");
        }
    }
}
