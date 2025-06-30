package de.telran.gardenStore.service;

import de.telran.gardenStore.entity.Category;

import java.util.List;

public interface CategoryService {

    List<Category> getAllCategories();

    Category getCategoryById(Long userId);

    Category createCategory(Category category);

    Category updateCategory(Long userId, Category category);

    void deleteCategoryById(Long userId);
}
