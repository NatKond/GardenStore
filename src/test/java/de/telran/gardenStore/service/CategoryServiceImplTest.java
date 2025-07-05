package de.telran.gardenStore.service;

import de.telran.gardenStore.entity.Category;
import de.telran.gardenStore.exception.CategoryNotFoundException;
import de.telran.gardenStore.exception.CategoryWithNameAlreadyExistsException;
import de.telran.gardenStore.repository.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
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
class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepositoryMock;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    private static Category category1;
    private static Category category2;
    private static Category category3;
    private static Category category4;

    @BeforeAll
    static void setUp() {
        category1 = Category.builder()
                .categoryId(1L)
                .name("Fertilizer")
                .build();

        category2 = Category.builder()
                .categoryId(2L)
                .name("Protective products and septic tanks")
                .build();

        category3 = Category.builder()
                .categoryId(3L)
                .name("Planting material")
                .build();

        category4 = Category.builder()
                .name("Pots and planters")
                .build();
    }

    @DisplayName("Test method getAllCategories")
    @Test
    void getAllCategories() {
        List<Category> expected = List.of(category1, category2, category3);

        when(categoryRepositoryMock.findAll()).thenReturn(List.of(category1, category2, category3));

        List<Category> actual = categoryService.getAllCategories();

        assertNotNull(actual);
        assertEquals(3, actual.size());
        assertEquals(expected, actual);
        verify(categoryRepositoryMock).findAll();
    }

    @DisplayName("Test method getCategoryById positive case")
    @Test
    void getCategoryByIdPositiveCase() {
        Category expected = category1;

        when(categoryRepositoryMock.findById(1L)).thenReturn(Optional.of(expected));

        Category actual = categoryService.getCategoryById(1L);

        assertNotNull(actual);
        assertEquals(expected, actual);
        assertEquals(expected.getName(), actual.getName());
        verify(categoryRepositoryMock).findById(1L);
    }

    @DisplayName("Test method getCategoryById negative case")
    @Test
    void getCategoryByIdNegativeCase() {
        Long categoryId = 6L;

        when(categoryRepositoryMock.findById(categoryId)).thenReturn(Optional.empty());
        RuntimeException runtimeException = assertThrows(CategoryNotFoundException.class, () -> categoryService.getCategoryById(categoryId));
        Assertions.assertEquals("Category with id " + categoryId + " not found", runtimeException.getMessage());

    }

    @DisplayName("Test method createCategory")
    @Test
    void createCategory() {
        Category categoryCreate = category4;

        Category categoryCreated = category4.toBuilder()
                .categoryId(4L)
                .build();

        when(categoryRepositoryMock.save(categoryCreate)).thenReturn(categoryCreated);

        Category actual = categoryService.createCategory(categoryCreate);

        assertNotNull(actual);
        assertEquals(categoryCreated, actual);
        verify(categoryRepositoryMock).save(categoryCreate);
    }

    @DisplayName("Test method updateCategory positive case")
    @Test
    void updateCategoryPositiveCase() {

        String updatedName = "Soil and substrates";

        Long categoryId = 1L;

        Category categoryUpdate = Category.builder()
                .name(updatedName)
                .build();

        Category categoryUpdated = Category.builder()
                .categoryId(categoryId)
                .name(updatedName)
                .build();

        when(categoryRepositoryMock.findById(categoryId)).thenReturn(Optional.of(category1));
        when(categoryRepositoryMock.save(categoryUpdated)).thenReturn(categoryUpdated);
        when(categoryRepositoryMock.findCategoryByName(updatedName)).thenReturn(Optional.empty());

        Category actual = categoryService.updateCategory(categoryId, categoryUpdate);

        assertNotNull(actual);
        assertEquals(categoryUpdated, actual);
        assertEquals(categoryUpdated.getName(), actual.getName());

        verify(categoryRepositoryMock).findById(categoryUpdated.getCategoryId());
        verify(categoryRepositoryMock).save(categoryUpdated);
    }

    @DisplayName("Test method updateCategory negative case")
    @Test
    void updateCategoryNegativeCase() {

        String updatedName = "Planting material";

        Long categoryId = 1L;

        Category categoryUpdate = Category.builder()
                .name(updatedName)
                .build();

        when(categoryRepositoryMock.findById(categoryId)).thenReturn(Optional.of(category1));
        when(categoryRepositoryMock.findCategoryByName(updatedName)).thenReturn(Optional.of(category3));

        RuntimeException exception = assertThrows(CategoryWithNameAlreadyExistsException.class, () -> categoryService.updateCategory(categoryId, categoryUpdate));
        Assertions.assertEquals("Category with name " + updatedName + " already exists.", exception.getMessage());
    }

    @DisplayName("Test method deleteCategoryById")
    @Test
    void deleteCategoryById() {
        Category deletedCategory = category1;

        when(categoryRepositoryMock.findById(deletedCategory.getCategoryId())).thenReturn(Optional.of(deletedCategory));

        categoryService.deleteCategoryById(1L);

        verify(categoryRepositoryMock).delete(deletedCategory);
        verify(categoryRepositoryMock).findById(deletedCategory.getCategoryId());
    }
}