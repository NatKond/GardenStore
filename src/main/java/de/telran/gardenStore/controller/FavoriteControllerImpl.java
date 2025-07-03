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
@RequiredArgsConstructor
public class FavoriteControllerImpl implements FavoriteController {

    private final FavoriteService favoriteService;

    private final ModelMapper modelMapper;

    @Override
    public List<FavoriteResponseDto> getAllFavorites() {
        List<Favorite> favorites = favoriteService.getAllFavorites();
        return favorites.stream()
                .map(fav -> modelMapper.map(fav, FavoriteResponseDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public FavoriteResponseDto getFavoriteById(Long favoriteId) {
        Favorite favorite = favoriteService.getFavoriteById(favoriteId);
        return modelMapper.map(favorite, FavoriteResponseDto.class);
    }

    @Override
    public FavoriteResponseDto createFavorite(FavoriteCreateRequestDto favoriteCreateRequestDto) {
        Favorite favorite = favoriteService.createFavorite(modelMapper.map(favoriteCreateRequestDto, Favorite.class));
        return modelMapper.map(favorite, FavoriteResponseDto.class);
    }


    @Override
    public void deleteFavorite(Long favoriteId) {
        favoriteService.deleteFavoriteById(favoriteId);
    }
}
