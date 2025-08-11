package de.telran.gardenStore.service;

import de.telran.gardenStore.AbstractTest;
import de.telran.gardenStore.entity.Category;
import de.telran.gardenStore.exception.CannotDeleteCategoryException;
import de.telran.gardenStore.exception.CategoryNotFoundException;
import de.telran.gardenStore.exception.CategoryWithNameAlreadyExistsException;
import de.telran.gardenStore.repository.CategoryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest extends AbstractTest {

    @Mock
    private CategoryRepository categoryRepositoryMock;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @DisplayName("Get all categories")
    @Test
    void getAll() {
        List<Category> expected = List.of(category1, category2, category3);

        when(categoryRepositoryMock.findAll()).thenReturn(List.of(category1, category2, category3));

        List<Category> actual = categoryService.getAll();

        assertNotNull(actual);
        assertEquals(expected.size(), actual.size());
        assertEquals(expected, actual);
        verify(categoryRepositoryMock).findAll();
    }

    @DisplayName("Get category by ID : positive case")
    @Test
    void getByIdPositiveCase() {
        Long categoryId = category1.getCategoryId();

        Category expected = category1;

        when(categoryRepositoryMock.findById(categoryId)).thenReturn(Optional.of(expected));

        Category actual = categoryService.getById(categoryId);

        assertNotNull(actual);
        assertEquals(expected, actual);
        assertEquals(expected.getName(), actual.getName());
        verify(categoryRepositoryMock).findById(categoryId);
    }

    @DisplayName("Get category by ID : negative case")
    @Test
    void getByIdNegativeCase() {
        Long categoryId = 99L;

        when(categoryRepositoryMock.findById(categoryId)).thenReturn(Optional.empty());

        RuntimeException runtimeException = assertThrows(CategoryNotFoundException.class, () -> categoryService.getById(categoryId));
        assertEquals("Category with id " + categoryId + " not found", runtimeException.getMessage());
    }

    @DisplayName("Create new category : positive case")
    @Test
    void createPositiveCase() {
        Category expected = categoryCreated;

        when(categoryRepositoryMock.save(categoryToCreate)).thenReturn(expected);
        when(categoryRepositoryMock.findCategoryByName(categoryCreated.getName())).thenReturn(Optional.empty());

        Category actual = categoryService.create(categoryToCreate);

        assertNotNull(actual);
        assertEquals(expected, actual);
        assertEquals(expected.getName(), actual.getName());
        verify(categoryRepositoryMock).save(categoryToCreate);
    }

    @DisplayName("Create new category : negative case")
    @Test
    void createNegativeCase() {
        Category categoryToCreate = Category.builder()
                .name("Protective products and septic tanks")
                .build();

        when(categoryRepositoryMock.findCategoryByName(categoryToCreate.getName())).thenReturn(Optional.of(category2));

        RuntimeException exception = assertThrows(CategoryWithNameAlreadyExistsException.class, () -> categoryService.create(categoryToCreate));
        assertEquals("Category with name " + categoryToCreate.getName() + " already exists.", exception.getMessage());
    }

    @DisplayName("Update category by ID : positive case")
    @Test
    void updatePositiveCase() {

        String nameToUpdate = "Soil and substrates";

        Long categoryId = 1L;

        Category categoryToUpdate = Category.builder()
                .name(nameToUpdate)
                .build();

        Category categoryUpdated = Category.builder()
                .categoryId(categoryId)
                .name(nameToUpdate)
                .build();

        when(categoryRepositoryMock.findById(categoryId)).thenReturn(Optional.of(category1));
        when(categoryRepositoryMock.save(categoryUpdated)).thenReturn(categoryUpdated);
        when(categoryRepositoryMock.findCategoryByName(nameToUpdate)).thenReturn(Optional.empty());

        Category actual = categoryService.update(categoryId, categoryToUpdate);

        assertNotNull(actual);
        assertEquals(categoryUpdated, actual);
        assertEquals(categoryUpdated.getName(), actual.getName());

        verify(categoryRepositoryMock).findById(categoryUpdated.getCategoryId());
        verify(categoryRepositoryMock).save(categoryUpdated);
    }

    @DisplayName("Update category by ID : negative case")
    @Test
    void updateNegativeCase() {

        String nameToUpdate = "Protective products and septic tanks";

        Long categoryId = 1L;

        Category categoryUpdate = Category.builder()
                .name(nameToUpdate)
                .build();

        when(categoryRepositoryMock.findById(categoryId)).thenReturn(Optional.of(category1));
        when(categoryRepositoryMock.findCategoryByName(nameToUpdate)).thenReturn(Optional.of(category2));

        RuntimeException exception = assertThrows(CategoryWithNameAlreadyExistsException.class, () -> categoryService.update(categoryId, categoryUpdate));
        assertEquals("Category with name " + nameToUpdate + " already exists.", exception.getMessage());
    }

    @DisplayName("Delete category by ID : positive case")
    @Test
    void deleteByIdPositiveCase() {
        Category categoryToDelete = categoryCreated;
        Long categoryId = categoryToDelete.getCategoryId();

        when(categoryRepositoryMock.findById(categoryToDelete.getCategoryId())).thenReturn(Optional.of(categoryToDelete));

        categoryService.deleteById(categoryId);

        verify(categoryRepositoryMock).delete(categoryToDelete);
        verify(categoryRepositoryMock).findById(categoryToDelete.getCategoryId());
    }

    @DisplayName("Delete category by ID : negative case(category not found)")
    @Test
    void deleteCategoryNotFound() {
        Long categoryId = 99L;

        when(categoryRepositoryMock.findById(categoryId)).thenReturn(Optional.empty());

        CategoryNotFoundException categoryNotFoundException = assertThrows(CategoryNotFoundException.class, () -> categoryService.deleteById(categoryId));
        assertEquals("Category with id " + categoryId + " not found", categoryNotFoundException.getMessage());
    }

    @DisplayName("Delete category by ID : negative case(category contains products)")
    @Test
    void deleteCategoryContainsProducts() {
        Category categoryToDelete = category1;
        Long categoryId = category1.getCategoryId();

        when(categoryRepositoryMock.findById(categoryId)).thenReturn(Optional.of(categoryToDelete));

        CannotDeleteCategoryException cannotDeleteCategoryException = assertThrows(CannotDeleteCategoryException.class, () -> categoryService.deleteById(categoryId));
        assertEquals("Cannot delete category " + categoryToDelete.getName() + " because it contains products.", cannotDeleteCategoryException.getMessage());
    }
}