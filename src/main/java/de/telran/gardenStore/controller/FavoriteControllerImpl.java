package de.telran.gardenStore.controller;

import de.telran.gardenStore.dto.FavoriteResponseDto;
import de.telran.gardenStore.entity.AppUser;
import de.telran.gardenStore.entity.Favorite;
import de.telran.gardenStore.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/favorites")
@RequiredArgsConstructor
public class FavoriteControllerImpl implements FavoriteController {

    private final FavoriteService favoriteService;
    private final ModelMapper modelMapper;

    @GetMapping
    @Override
    public List<FavoriteResponseDto> getAllFavorites() {
        List<Favorite> favorites = favoriteService.getAllFavorites();
        return favorites.stream()
                .map(fav -> modelMapper.map(fav, FavoriteResponseDto.class))
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    @Override
    public FavoriteResponseDto getFavoriteById(@PathVariable Long id) {
        Favorite favorite = favoriteService.getFavoriteById(id);
        return modelMapper.map(favorite, FavoriteResponseDto.class);
    }

    @PostMapping
    @Override
    public FavoriteResponseDto createFavorite(@RequestBody AppUser user) {
        Favorite favorite = favoriteService.createFavorite(user);
        return modelMapper.map(favorite, FavoriteResponseDto.class);
    }

    @DeleteMapping("/{id}")
    @Override
    public void deleteFavorite(@PathVariable Long id) {
        favoriteService.deleteFavoriteById(id);
    }
}
