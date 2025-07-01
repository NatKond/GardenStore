package de.telran.gardenStore.controller;

import de.telran.gardenStore.dto.FavoriteResponseDto;
import de.telran.gardenStore.entity.AppUser;
import de.telran.gardenStore.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/favorites")
@RequiredArgsConstructor
public class FavoriteControllerImpl implements FavoriteController {
    private FavoriteService favoriteService;

    @GetMapping
    @Override
    public List<FavoriteResponseDto> getAll() {
        return favoriteService.getAllFavorites();
    }

    @GetMapping("/{id}")
    @Override
    public FavoriteResponseDto getById(@PathVariable Long id) {
        return favoriteService.getFavoriteById(id);
    }

    @PostMapping
    @Override
    public FavoriteResponseDto createByUser(AppUser user) {
        return favoriteService.createFavorite(user);
    }

    @DeleteMapping("/{id}")
    @Override
    public void deleteById(@PathVariable Long id) {
        favoriteService.deleteFavoriteById(id);
    }
}
