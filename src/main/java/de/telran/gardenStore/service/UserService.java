package de.telran.gardenStore.service;

import de.telran.gardenStore.entity.AppUser;

import java.util.List;

public interface UserService {

    List<AppUser> getAll();

    AppUser getById(Long userId);

    AppUser getCurrent();

    AppUser getByEmail(String email);

    AppUser create(AppUser appUser);

    AppUser update(AppUser appUser);

    void delete();
}
