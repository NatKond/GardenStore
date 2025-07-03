package de.telran.gardenStore.service;

import de.telran.gardenStore.entity.AppUser;

import java.util.List;

public interface UserService {

    List<AppUser> getAllUsers();

    AppUser getUserById(Long userId);

    AppUser getUserByEmail(String email);

    AppUser createUser(AppUser appUser);

    AppUser updateUser(Long id, AppUser appUser);

    void deleteUserById(Long userId);
}
