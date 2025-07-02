package de.telran.gardenStore.controller;

import de.telran.gardenStore.dto.CategoryCreateRequestDto;
import de.telran.gardenStore.dto.CategoryResponseDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.validation.annotation.Validated;

import java.util.List;
@Validated
public interface CategoryController {

    List<CategoryResponseDto> getAllCategories();

    CategoryResponseDto getCategoryById(@Positive Long categoryId);

    CategoryResponseDto createCategory(@Valid CategoryCreateRequestDto dto);

    CategoryResponseDto updateCategory(@Positive Long categoryId, @Valid CategoryCreateRequestDto dto);

    void deleteCategory(@Positive Long categoryId);
}
