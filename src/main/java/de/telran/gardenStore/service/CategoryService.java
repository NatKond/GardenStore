package de.telran.gardenStore.service;

import de.telran.gardenStore.dto.CategoryCreateRequestDto;
import de.telran.gardenStore.dto.CategoryResponseDto;
import java.util.List;

public interface CategoryService {
    List<CategoryResponseDto> getAllCategories();
    CategoryResponseDto getCategoryById(Long id);
    CategoryResponseDto createCategory(CategoryCreateRequestDto dto);
    CategoryResponseDto updateCategory(Long id, CategoryCreateRequestDto dto);
    void deleteCategoryById(Long id);
}