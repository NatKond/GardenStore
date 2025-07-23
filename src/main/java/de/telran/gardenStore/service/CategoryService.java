package de.telran.gardenStore.service;

import de.telran.gardenStore.entity.Category;

import java.util.List;

public interface CategoryService {

    List<Category> getAll();

    Category getById(Long categoryId);

    Category create(Category category);

    Category update(Long userId, Category category);

    void deleteById(Long categoryId);
}
