package de.telran.gardenStore.controller;

import de.telran.gardenStore.converter.Converter;
import de.telran.gardenStore.dto.FavoriteCreateRequestDto;
import de.telran.gardenStore.dto.FavoriteResponseDto;
import de.telran.gardenStore.entity.Favorite;
import de.telran.gardenStore.service.FavoriteService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/favorites")
@RequiredArgsConstructor
@Validated
public class FavoriteControllerImpl implements FavoriteController {

    private final FavoriteService favoriteService;

    private final Converter<Favorite, FavoriteCreateRequestDto, FavoriteResponseDto,FavoriteResponseDto> favoriteConverter;

    @Override
    @GetMapping("/{userId}")
    public List<FavoriteResponseDto> getAllFavoritesByUser(@PathVariable @Positive Long userId) {
        return favoriteConverter.convertEntityListToDtoList(favoriteService.getAllFavoritesByUser(userId));
    }

    @Override
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{userId}")
    public FavoriteResponseDto createFavorite(@PathVariable @Positive Long userId,
                                              @RequestBody @Valid FavoriteCreateRequestDto favoriteCreateRequestDto) {
        favoriteCreateRequestDto.setUserId(userId);
        return favoriteConverter.convertEntityToDto(favoriteService.createFavorite(
                favoriteConverter.convertDtoToEntity(favoriteCreateRequestDto)));
    }

    @Override
    @DeleteMapping("/{favoriteId}")
    public void deleteFavorite(@PathVariable @Positive Long favoriteId) {
        favoriteService.deleteFavoriteById(favoriteId);
    }
}
