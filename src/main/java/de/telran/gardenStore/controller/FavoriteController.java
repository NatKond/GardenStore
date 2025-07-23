package de.telran.gardenStore.controller;

import de.telran.gardenStore.dto.FavoriteResponseDto;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RequestMapping("/v1/favorites")
public interface FavoriteController {

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping
    List<FavoriteResponseDto> getAllForCurrentUser();

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    FavoriteResponseDto create(@RequestParam @Positive Long productId);

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @DeleteMapping("/{favoriteId}")
    void delete(@PathVariable @Positive Long favoriteId);
}
