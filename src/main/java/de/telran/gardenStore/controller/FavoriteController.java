package de.telran.gardenStore.controller;

import de.telran.gardenStore.dto.FavoriteResponseDto;
import de.telran.gardenStore.entity.AppUser;

import java.util.List;

public interface FavoriteController {
    List<FavoriteResponseDto> getAll();

    FavoriteResponseDto getById(Long id);

    FavoriteResponseDto createByUser(AppUser user);

    void deleteById(Long id);
}
