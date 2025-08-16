package de.telran.gardenStore.service;

import de.telran.gardenStore.entity.AppUser;
import de.telran.gardenStore.exception.UserDeletionNotAllowedException;
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
        log.debug("Attempt to create {}", user);

        return userRepository.save(user);
    }

    @Override
    public AppUser update(AppUser user) {
        AppUser userToUpdate = getCurrent();

        if (!userToUpdate.getEmail().equals(user.getEmail())) {
            checkUserEmailIsUnique(user.getEmail());
        }

        userToUpdate.setName(user.getName());
        userToUpdate.setEmail(user.getEmail());
        userToUpdate.setPhoneNumber(user.getPhoneNumber());
        userToUpdate.setPasswordHash(user.getPasswordHash());
        log.debug("Attempt to update {} to {}", user, userToUpdate);

        return userRepository.save(userToUpdate);
    }

    @Override
    public void delete() {
        AppUser user = getCurrent();
        if (userRepository.hasOrders(user.getUserId())){
            throw new UserDeletionNotAllowedException("User cannot be deleted because they have placed orders");
        }
        userRepository.delete(user);
    }

    private void checkUserEmailIsUnique(String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new UserWithEmailAlreadyExistsException("User with email " + email + " already exists");
        }
    }
}
