package de.telran.gardenStore.controller;

import de.telran.gardenStore.dto.CategoryCreateRequestDto;
import de.telran.gardenStore.dto.CategoryResponseDto;
import de.telran.gardenStore.dto.CategoryShortResponseDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RequestMapping("/v1/categories")
public interface CategoryController {

    @GetMapping
    List<CategoryShortResponseDto> getAll();

    @GetMapping("/{categoryId}")
    CategoryResponseDto getById(@PathVariable @Positive Long categoryId);

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    CategoryResponseDto create(@RequestBody @Valid CategoryCreateRequestDto dto);

    @ResponseStatus(HttpStatus.ACCEPTED)
    @PutMapping("/{categoryId}")
    @PreAuthorize("hasRole('ADMIN')")
    CategoryResponseDto update(@PathVariable @Positive Long categoryId,
                               @RequestBody @Valid CategoryCreateRequestDto dto);


    @DeleteMapping("/{categoryId}")
    @PreAuthorize("hasRole('ADMIN')")
    void delete(@PathVariable @Positive Long categoryId);
}
