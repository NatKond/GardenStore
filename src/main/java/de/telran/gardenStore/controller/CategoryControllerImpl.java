package de.telran.gardenStore.controller;

import de.telran.gardenStore.dto.CategoryCreateRequestDto;
import de.telran.gardenStore.dto.CategoryResponseDto;
import de.telran.gardenStore.entity.Category;
import de.telran.gardenStore.service.CategoryService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Validated
public class CategoryControllerImpl implements CategoryController {

    private final CategoryService categoryService;

    private final ModelMapper modelMapper;

    @Override

    public List<CategoryResponseDto> getAllCategories() {
        return categoryService.getAllCategories().stream()
                .map(category -> modelMapper.map(category, CategoryResponseDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public CategoryResponseDto getCategoryById(@Positive Long categoryId) {
        return modelMapper.map(categoryService.getCategoryById(categoryId), CategoryResponseDto.class);
    }

    @Override
    public CategoryResponseDto createCategory(@Valid CategoryCreateRequestDto dto) {
        Category category = modelMapper.map(dto, Category.class);
        return modelMapper.map(categoryService.createCategory(category), CategoryResponseDto.class);
    }

    @Override
    public CategoryResponseDto updateCategory(@Positive Long categoryId, @Valid CategoryCreateRequestDto dto) {
        Category category = modelMapper.map(dto, Category.class);
        return modelMapper.map(categoryService.updateCategory(categoryId, category), CategoryResponseDto.class);
    }

    @Override
    public void deleteCategory(@Positive Long categoryId) {
        categoryService.deleteCategoryById(categoryId);
    }
}