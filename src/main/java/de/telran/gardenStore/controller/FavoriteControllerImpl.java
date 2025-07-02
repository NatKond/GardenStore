package de.telran.gardenStore.controller;

import de.telran.gardenStore.dto.FavoriteCreateRequestDto;
import de.telran.gardenStore.dto.FavoriteResponseDto;
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

    @GetMapping("/{userId}")
    @Override
    public List<FavoriteResponseDto> getAllFavoritesByUser(Long userId) {
        List<Favorite> favorites = favoriteService.getAllFavoritesByUser(userId);
        return favorites.stream()
                .map(fav -> modelMapper.map(fav, FavoriteResponseDto.class))
                .collect(Collectors.toList());
    }
    @PostMapping("/{userId}")
    @Override
    public FavoriteResponseDto createFavorite(@PathVariable Long userId, FavoriteCreateRequestDto favoriteCreateRequestDto) {
        favoriteCreateRequestDto.setUserId(userId);
        Favorite favorite = favoriteService.createFavorite(modelMapper.map(favoriteCreateRequestDto, Favorite.class));
        return modelMapper.map(favorite, FavoriteResponseDto.class);
    }

    @DeleteMapping("/{favoriteId}")
    @Override
    public void deleteFavorite(@PathVariable Long favoriteId) {
        favoriteService.deleteFavoriteById(favoriteId);
    }
}
