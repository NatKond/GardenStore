package de.telran.gardenStore.controller;

import de.telran.gardenStore.converter.Converter;
import de.telran.gardenStore.dto.FavoriteCreateRequestDto;
import de.telran.gardenStore.dto.FavoriteResponseDto;
import de.telran.gardenStore.entity.Favorite;
import de.telran.gardenStore.service.FavoriteService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
public class FavoriteControllerImpl implements FavoriteController {

    private final FavoriteService favoriteService;

    private final Converter<Favorite, FavoriteCreateRequestDto, FavoriteResponseDto,FavoriteResponseDto> favoriteConverter;

    @Override
    public List<FavoriteResponseDto> getAllFavoritesByUser(@Positive Long userId) {
        return favoriteConverter.convertEntityListToDtoList(favoriteService.getAllFavoritesByUser(userId));
    }

    @Override
    public FavoriteResponseDto createFavorite(@Positive Long userId, @Valid FavoriteCreateRequestDto favoriteCreateRequestDto) {
        favoriteCreateRequestDto.setUserId(userId);
        return favoriteConverter.convertEntityToDto(favoriteService.createFavorite(
                favoriteConverter.convertDtoToEntity(favoriteCreateRequestDto)));
    }

    @Override
    public void deleteFavorite(@Positive Long favoriteId) {
        favoriteService.deleteFavoriteById(favoriteId);
    }
}
