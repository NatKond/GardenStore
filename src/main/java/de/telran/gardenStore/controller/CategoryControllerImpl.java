package de.telran.gardenStore.controller;

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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/categories")
@Validated
public class CategoryControllerImpl implements CategoryController {

    private final CategoryService categoryService;

    private final Converter<Category, CategoryCreateRequestDto, CategoryResponseDto, CategoryShortResponseDto> categoryConverter;

    @Override
    @GetMapping
    public List<CategoryShortResponseDto> getAll() {
        return categoryConverter.convertEntityListToDtoList(categoryService.getAllCategories());
    }

    @Override
    @GetMapping("/{categoryId}")
    public CategoryResponseDto getById(@PathVariable @Positive Long categoryId) {
        return categoryConverter.convertEntityToDto(categoryService.getCategoryById(categoryId));
    }

    @Override
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryResponseDto create(@RequestBody @Valid CategoryCreateRequestDto categoryCreateRequestDto) {
        return categoryConverter.convertEntityToDto(categoryService.createCategory(
                categoryConverter.convertDtoToEntity(categoryCreateRequestDto)));
    }

    @Override
    @ResponseStatus(HttpStatus.ACCEPTED)
    @PutMapping("/{categoryId}")
    public CategoryResponseDto update(@PathVariable @Positive Long categoryId,
                                              @RequestBody @Valid CategoryCreateRequestDto categoryCreateRequestDto) {
        return categoryConverter.convertEntityToDto(categoryService.updateCategory(categoryId,  categoryConverter.convertDtoToEntity(categoryCreateRequestDto)));
    }

    @Override
    @DeleteMapping("/{categoryId}")
    public void delete(@PathVariable @Positive Long categoryId) {
        categoryService.deleteCategoryById(categoryId);
    }
}