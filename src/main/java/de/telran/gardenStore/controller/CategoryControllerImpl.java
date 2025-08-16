package de.telran.gardenStore.controller;

import de.telran.gardenStore.annotation.Loggable;
import de.telran.gardenStore.converter.Converter;
import de.telran.gardenStore.dto.CategoryCreateRequestDto;
import de.telran.gardenStore.dto.CategoryResponseDto;
import de.telran.gardenStore.dto.CategoryShortResponseDto;
import de.telran.gardenStore.entity.Category;
import de.telran.gardenStore.service.CategoryService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/categories")
public class CategoryControllerImpl implements CategoryController {

    private final CategoryService categoryService;

    private final Converter<Category, CategoryCreateRequestDto, CategoryResponseDto, CategoryShortResponseDto> categoryConverter;

    @Override
    @GetMapping
    public List<CategoryShortResponseDto> getAll() {
        return categoryConverter.toDtoList(categoryService.getAll());
    }

    @Override
    @GetMapping("/{categoryId}")
    public CategoryResponseDto getById(@PathVariable @Positive Long categoryId) {
        return categoryConverter.toDto(
                categoryService.getById(categoryId));
    }

    @Override
    @Loggable
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public CategoryResponseDto create(@RequestBody @Valid CategoryCreateRequestDto categoryCreateRequestDto) {
        return categoryConverter.toDto(categoryService.create(
                categoryConverter.toEntity(categoryCreateRequestDto)));
    }

    @Override
    @Loggable
    @ResponseStatus(HttpStatus.ACCEPTED)
    @PutMapping("/{categoryId}")
    @PreAuthorize("hasRole('ADMIN')")
    public CategoryResponseDto update(@PathVariable @Positive Long categoryId,
                                      @RequestBody @Valid CategoryCreateRequestDto categoryCreateRequestDto) {
        return categoryConverter.toDto(
                categoryService.update(categoryId,  categoryConverter.toEntity(categoryCreateRequestDto)));
    }

    @Override
    @DeleteMapping("/{categoryId}")
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(@PathVariable @Positive Long categoryId) {
        categoryService.deleteById(categoryId);
    }
}