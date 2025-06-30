package de.telran.gardenStore.service;

import de.telran.gardenStore.entity.AppUser;

import java.util.List;

public interface UserService {

    List<AppUser> getAllUsers();

    AppUser getUserById(Long userId);

    AppUser createUser(AppUser appUser);

    void deleteUserById(Long userId);
}
