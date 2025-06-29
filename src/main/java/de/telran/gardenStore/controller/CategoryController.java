package de.telran.gardenStore.controller;

import de.telran.gardenStore.dto.CategoryCreateRequestDto;
import de.telran.gardenStore.dto.CategoryResponseDto;

import java.util.List;

public interface CategoryController {

    List<CategoryResponseDto> getAllCategories();

    CategoryResponseDto getCategoryById(Long id);

    CategoryResponseDto createCategory(CategoryCreateRequestDto dto);

    CategoryResponseDto updateCategory(Long id, CategoryCreateRequestDto dto);

    void deleteCategory(Long id);
}
