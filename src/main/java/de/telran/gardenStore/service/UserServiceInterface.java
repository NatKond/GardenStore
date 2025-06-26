package de.telran.gardenStore.service;

import de.telran.gardenStore.entity.User;

import java.util.List;

public interface UserServiceInterface {

    List<User> getAllUsers();

    User getUserById(Long id);

    User createUser(User user);

    void deleteUserById(Long id);
}
