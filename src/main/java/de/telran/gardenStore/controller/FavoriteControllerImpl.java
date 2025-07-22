package de.telran.gardenStore.controller;
import de.telran.gardenStore.converter.ConverterEntityToDto;
import de.telran.gardenStore.dto.FavoriteResponseDto;
import de.telran.gardenStore.entity.Favorite;
import de.telran.gardenStore.service.FavoriteService;
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

    private final ConverterEntityToDto<Favorite, FavoriteResponseDto, FavoriteResponseDto> favoriteConverter;

    @Override
    public List<FavoriteResponseDto> getAllForCurrentUser() {
        return favoriteConverter.convertEntityListToDtoList(
                favoriteService.getAllForCurrentUser());
    }

    @Override
    public FavoriteResponseDto create(@Positive Long productId) {
        return favoriteConverter.convertEntityToDto(
                favoriteService.create(productId));
    }

    @Override
    public void delete(@Positive Long favoriteId) {
        favoriteService.deleteById(favoriteId);
    }
}
