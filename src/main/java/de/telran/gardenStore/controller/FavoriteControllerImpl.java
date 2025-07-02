package de.telran.gardenStore.controller;

import de.telran.gardenStore.dto.FavoriteCreateRequestDto;
import de.telran.gardenStore.dto.FavoriteResponseDto;
import de.telran.gardenStore.entity.Favorite;
import de.telran.gardenStore.service.FavoriteService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/favorites")
@RequiredArgsConstructor
@Validated
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

    @GetMapping("/{favoriteId}")
    @Override
    public FavoriteResponseDto getFavoriteById(@PathVariable @Positive Long favoriteId) {
        Favorite favorite = favoriteService.getFavoriteById(favoriteId);
        return modelMapper.map(favorite, FavoriteResponseDto.class);
    }

    @PostMapping
    @Override
    public FavoriteResponseDto createFavorite(@RequestBody @Valid FavoriteCreateRequestDto favoriteCreateRequestDto) {
        Favorite favorite = favoriteService.createFavorite(modelMapper.map(favoriteCreateRequestDto, Favorite.class));
        return modelMapper.map(favorite, FavoriteResponseDto.class);
    }

    @DeleteMapping("/{favoriteId}")
    @Override
    public void deleteFavorite(@PathVariable @Positive Long favoriteId) {
        favoriteService.deleteFavoriteById(favoriteId);
    }
}
