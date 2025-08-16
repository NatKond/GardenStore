package de.telran.gardenStore.controller;
import de.telran.gardenStore.annotation.Loggable;
import de.telran.gardenStore.converter.ConverterEntityToDto;
import de.telran.gardenStore.dto.FavoriteResponseDto;
import de.telran.gardenStore.entity.Favorite;
import de.telran.gardenStore.service.FavoriteService;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/favorites")
@PreAuthorize("hasRole('USER')")
public class FavoriteControllerImpl implements FavoriteController {

    private final FavoriteService favoriteService;

    private final ConverterEntityToDto<Favorite, FavoriteResponseDto, FavoriteResponseDto> favoriteConverter;

    @Override
    @GetMapping()
    public List<FavoriteResponseDto> getAllForCurrentUser() {
        return favoriteConverter.convertEntityListToDtoList(
                favoriteService.getAllForCurrentUser());
    }

    @Override
    @Loggable
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{productId}")
    public FavoriteResponseDto create(@Positive @PathVariable Long productId) {
        return favoriteConverter.convertEntityToDto(
                favoriteService.create(productId));
    }

    @Override
    @DeleteMapping("/{favoriteId}")
    public void delete(@PathVariable @Positive Long favoriteId) {
        favoriteService.deleteById(favoriteId);
    }
}
