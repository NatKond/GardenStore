package de.telran.gardenStore.service;

import de.telran.gardenStore.entity.AppUser;

import java.util.List;

public interface UserService {

    List<AppUser> getAllUsers();

    AppUser getUserById(Long id);

    AppUser createUser(AppUser appUser);

    void deleteUserById(Long id);
}
