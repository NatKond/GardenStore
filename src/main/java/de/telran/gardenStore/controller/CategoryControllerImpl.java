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

    public List<CategoryShortResponseDto> getAllCategories() {
        return categoryConverter.convertEntityListToDtoList(categoryService.getAllCategories());
    }

    @Override
    public CategoryResponseDto getCategoryById(@Positive Long categoryId) {
        return categoryConverter.convertEntityToDto(categoryService.getCategoryById(categoryId));
    }

    @Override
    public CategoryResponseDto createCategory(@Valid CategoryCreateRequestDto categoryCreateRequestDto) {
        return categoryConverter.convertEntityToDto(categoryService.createCategory(
                categoryConverter.convertDtoToEntity(categoryCreateRequestDto)));
    }

    @Override
    public CategoryResponseDto updateCategory(@Positive Long categoryId, @Valid CategoryCreateRequestDto categoryCreateRequestDto) {
        return categoryConverter.convertEntityToDto(categoryService.updateCategory(categoryId,  categoryConverter.convertDtoToEntity(categoryCreateRequestDto)));
    }

    @Override
    public void deleteCategory(@Positive Long categoryId) {
        categoryService.deleteCategoryById(categoryId);
    }
}