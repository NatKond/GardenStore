package de.telran.gardenStore.service;

import de.telran.gardenStore.entity.Category;
import de.telran.gardenStore.exception.CannotDeleteCategoryException;
import de.telran.gardenStore.exception.CategoryNotFoundException;
import de.telran.gardenStore.exception.CategoryWithNameAlreadyExistsException;
import de.telran.gardenStore.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public List<Category> getAll() {
        return categoryRepository.findAll();
    }

    @Override
    public Category getById(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException("Category with id " + categoryId + " not found"));
    }

    @Override
    public Category create(Category category) {
        checkCategoryNameIsUnique(category.getName());

        Category savedCategory = categoryRepository.save(category);
        log.debug("CategoryId = {}: Category saved", savedCategory.getCategoryId());
        return savedCategory;
    }

    @Override
    public Category update(Long categoryId, Category category) {
        Category existing = getById(categoryId);
        if (!existing.getName().equals(category.getName())) {
            checkCategoryNameIsUnique(category.getName());
        }
        existing.setName(category.getName());

        Category savedCategory = categoryRepository.save(existing);
        log.debug("CategoryId = {}: Category updated", categoryId);
        return savedCategory;
    }

    @Override
    public void deleteById(Long categoryId) {
        Category category = getById(categoryId);
        if (!category.getProducts().isEmpty()) {
            throw new CannotDeleteCategoryException("Cannot delete category " + category.getName() + " because it contains products.");
        }
        categoryRepository.delete(category);
    }

    private void checkCategoryNameIsUnique(String name) {
        if (categoryRepository.findCategoryByName(name).isPresent()) {
            throw new CategoryWithNameAlreadyExistsException("Category with name " + name + " already exists.");
        }
    }

}
