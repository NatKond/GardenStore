package de.telran.gardenStore.controller;

import de.telran.gardenStore.dto.FavoriteResponseDto;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RequestMapping("/v1/favorites")
@PreAuthorize("hasRole('USER')")
public interface FavoriteController {

    @GetMapping
    List<FavoriteResponseDto> getAllForCurrentUser();

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{productId}")
    FavoriteResponseDto create(@PathVariable @Positive Long productId);

    @DeleteMapping("/{favoriteId}")
    void delete(@PathVariable @Positive Long favoriteId);
}
