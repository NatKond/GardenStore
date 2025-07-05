package de.telran.gardenStore.controller;

import de.telran.gardenStore.dto.CategoryCreateRequestDto;
import de.telran.gardenStore.dto.CategoryResponseDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RequestMapping("/v1/categories")
public interface CategoryController {

    @GetMapping
    List<CategoryResponseDto> getAllCategories();

    @GetMapping("/{categoryId}")
    CategoryResponseDto getCategoryById(@PathVariable @Positive Long categoryId);

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    CategoryResponseDto createCategory(@RequestBody @Valid CategoryCreateRequestDto dto);

    @ResponseStatus(HttpStatus.ACCEPTED)
    @PutMapping("/{categoryId}")
    CategoryResponseDto updateCategory(@PathVariable @Positive Long categoryId,
                                       @RequestBody @Valid CategoryCreateRequestDto dto);

    @DeleteMapping("/{categoryId}")
    void deleteCategory(@PathVariable @Positive Long categoryId);
}
