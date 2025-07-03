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
@RequiredArgsConstructor
@Validated
public class FavoriteControllerImpl implements FavoriteController {

    private final FavoriteService favoriteService;

    private final ModelMapper modelMapper;

    @Override
    public List<FavoriteResponseDto> getAllFavoritesByUser(@Positive Long userId) {
        List<Favorite> favorites = favoriteService.getAllFavoritesByUser(userId);
        return favorites.stream()
                .map(fav -> modelMapper.map(fav, FavoriteResponseDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public FavoriteResponseDto createFavorite(@Positive Long userId, @Valid FavoriteCreateRequestDto favoriteCreateRequestDto) {

        favoriteCreateRequestDto.setUserId(userId);
        Favorite favorite = favoriteService.createFavorite(modelMapper.map(favoriteCreateRequestDto, Favorite.class));
        return modelMapper.map(favorite, FavoriteResponseDto.class);
    }

    @Override
    public void deleteFavorite(@Positive Long favoriteId) {
        favoriteService.deleteFavoriteById(favoriteId);
    }
}
