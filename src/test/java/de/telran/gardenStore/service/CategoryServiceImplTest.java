package de.telran.gardenStore.service;

import de.telran.gardenStore.entity.Category;
import de.telran.gardenStore.exception.CategoryNotFoundException;
import de.telran.gardenStore.repository.CategoryRepository;
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
    private static Category categoryCreate;
    private static Category categoryCreated;
    private static Category categoryUpdated;

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
                .categoryId(4L)
                .name("Tools and equipment")
                .build();

        categoryCreate = Category.builder()
                .name("Pots and planters")
                .build();

        categoryCreated = Category.builder()
                .categoryId(5L)
                .name("Pots and planters")
                .build();

        categoryUpdated = Category.builder()
                .categoryId(1L)
                .name("Soil and substrates")
                .build();
    }

    @DisplayName("Test method getAllCategories")
    @Test
    void getAllCategories() {
        List<Category> expected = List.of(category1, category2, category3, category4);

        when(categoryRepositoryMock.findAll()).thenReturn(List.of(category1, category2, category3, category4));

        List<Category> actual = categoryService.getAllCategories();

        assertNotNull(actual);
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
        verify(categoryRepositoryMock).findById(1L);
    }

    @DisplayName("Test method getCategoryById negative case")
    @Test
    void getCategoryByIdNegativeCase() {
        Long categoryId = 6L;

        when(categoryRepositoryMock.findById(categoryId)).thenThrow(new CategoryNotFoundException("Category with id " + categoryId + " not found"));

        assertThrows(CategoryNotFoundException.class, () -> categoryService.getCategoryById(categoryId));
    }

    @DisplayName("Test method createCategory")
    @Test
    void createCategory() {
        Category expected = categoryCreated;

        when(categoryRepositoryMock.save(categoryCreate)).thenReturn(expected);

        Category actual = categoryService.createCategory(categoryCreate);

        assertNotNull(actual);
        assertEquals(expected, actual);
        verify(categoryRepositoryMock).save(categoryCreate);
    }

    @DisplayName("Test method updateCategory")
    @Test
    void updateCategory() {
        Category expected = categoryUpdated;

        when(categoryRepositoryMock.findById(1L)).thenReturn(Optional.of(expected));
        when(categoryRepositoryMock.save(expected)).thenReturn(expected);

        Category actual = categoryService.updateCategory(expected.getCategoryId(), expected);

        assertNotNull(actual);
        assertEquals(expected, actual);
        verify(categoryRepositoryMock).findById(1L);
        verify(categoryRepositoryMock).save(expected);
    }

    @DisplayName("Test method deleteCategoryById")
    @Test
    void deleteCategoryById() {
        categoryRepositoryMock.deleteById(1L);
        verify(categoryRepositoryMock).deleteById(1L);
    }
}