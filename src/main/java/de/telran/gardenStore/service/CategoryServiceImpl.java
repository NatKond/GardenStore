package de.telran.gardenStore.service;

import de.telran.gardenStore.entity.Category;
import de.telran.gardenStore.exception.CategoryNotFoundException;
import de.telran.gardenStore.exception.CategoryWithNameAlreadyExistsException;
import de.telran.gardenStore.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Category getCategoryById(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException("Category with id " + categoryId + " not found"));
    }

    @Override
    public Category getCategoryByName(String name) {
        return categoryRepository.findCategoryByName(name).orElseThrow(() -> new CategoryNotFoundException("Category with name " + name + " not found"));
    }

    @Override
    public Category createCategory(Category category) {
        nameCheck(category.getName());
        return categoryRepository.save(category);
    }

    @Override
    public Category updateCategory(Long categoryId, Category updatedCategory) {

        Category existing = getCategoryById(categoryId);
        if (!existing.getName().equals(updatedCategory.getName())) {
            nameCheck(updatedCategory.getName());
        }

        existing.setName(updatedCategory.getName());
        return categoryRepository.save(existing);
    }

    @Override
    public void deleteCategoryById(Long categoryId) {
        categoryRepository.delete(getCategoryById(categoryId));
    }

    private void nameCheck(String name) {
        if (categoryRepository.findCategoryByName(name).isPresent()) {
            throw new CategoryWithNameAlreadyExistsException("Category with name " + name + " already exists.");
        }
    }
}
