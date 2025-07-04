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

import static org.assertj.core.api.Assertions.assertThatThrownBy;
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

        when(categoryRepositoryMock.findById(categoryId)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> categoryService.getCategoryById(categoryId))
                .isInstanceOf(CategoryNotFoundException.class)
                .hasMessageContaining("Category with id 6 not found");

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

    @DisplayName("Test method updateCategory")
    @Test
    void updateCategory() {

        Category categoryUpdated = category1.toBuilder()
                .name("Soil and substrates")
                .build();

        when(categoryRepositoryMock.findById(1L)).thenReturn(Optional.of(categoryUpdated));
        when(categoryRepositoryMock.save(categoryUpdated)).thenReturn(categoryUpdated);

        Category actual = categoryService.updateCategory(categoryUpdated.getCategoryId(), categoryUpdated);

        assertNotNull(actual);
        assertEquals(categoryUpdated, actual);
        verify(categoryRepositoryMock).findById(1L);
        verify(categoryRepositoryMock).save(categoryUpdated);
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