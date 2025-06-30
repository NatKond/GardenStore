package de.telran.gardenStore.controller;

import de.telran.gardenStore.dto.CategoryCreateRequestDto;
import de.telran.gardenStore.dto.CategoryResponseDto;
import de.telran.gardenStore.entity.Category;
import de.telran.gardenStore.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryControllerImpl implements CategoryController {

    private final CategoryService categoryService;

    private final ModelMapper modelMapper;

    @Override
    @GetMapping
    public List<CategoryResponseDto> getAllCategories() {
        return categoryService.getAllCategories().stream()
                .map(category -> modelMapper.map(category, CategoryResponseDto.class))
                .collect(Collectors.toList());
    }

    @Override
    @GetMapping("/{categoryId}")
    public CategoryResponseDto getCategoryById(@PathVariable Long categoryId) {
        return modelMapper.map(categoryService.getCategoryById(categoryId), CategoryResponseDto.class);
    }

    @Override
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryResponseDto createCategory(@Valid @RequestBody CategoryCreateRequestDto dto) {
        Category category = modelMapper.map(dto, Category.class);
        return modelMapper.map(categoryService.createCategory(category), CategoryResponseDto.class);
    }

    @ResponseStatus(HttpStatus.ACCEPTED)
    @Override
    @PutMapping("/{categoryId}")
    public CategoryResponseDto updateCategory(@PathVariable Long categoryId, @Valid @RequestBody CategoryCreateRequestDto dto) {
        Category category = modelMapper.map(dto, Category.class);
        return modelMapper.map(categoryService.updateCategory(categoryId, category), CategoryResponseDto.class);
    }

    @Override
    @DeleteMapping("/{categoryId}")
    public void deleteCategory(@PathVariable Long categoryId) {
        categoryService.deleteCategoryById(categoryId);
    }
}