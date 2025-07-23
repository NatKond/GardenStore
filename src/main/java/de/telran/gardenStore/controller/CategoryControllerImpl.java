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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
public class CategoryControllerImpl implements CategoryController {

    private final CategoryService categoryService;

    private final Converter<Category, CategoryCreateRequestDto, CategoryResponseDto, CategoryShortResponseDto> categoryConverter;

    @Override
    public List<CategoryShortResponseDto> getAll() {
        return categoryConverter.convertEntityListToDtoList(
                categoryService.getAll());
    }

    @Override
    public CategoryResponseDto getById(@Positive Long categoryId) {
        return categoryConverter.convertEntityToDto(
                categoryService.getById(categoryId));
    }

    @Override
    public CategoryResponseDto create(@Valid CategoryCreateRequestDto categoryCreateRequestDto) {
        return categoryConverter.convertEntityToDto(
                categoryService.create(
                        categoryConverter.convertDtoToEntity(categoryCreateRequestDto)));
    }

    @Override
    public CategoryResponseDto update(@Positive Long categoryId, @Valid CategoryCreateRequestDto categoryCreateRequestDto) {
        return categoryConverter.convertEntityToDto(
                categoryService.update(categoryId,
                        categoryConverter.convertDtoToEntity(categoryCreateRequestDto)));
    }

    @Override
    public void delete(@Positive Long categoryId) {
        categoryService.deleteById(categoryId);
    }
}