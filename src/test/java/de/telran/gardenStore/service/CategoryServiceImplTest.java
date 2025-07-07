package de.telran.gardenStore.service;

import de.telran.gardenStore.AbstractTest;
import de.telran.gardenStore.entity.Category;
import de.telran.gardenStore.exception.CategoryNotFoundException;
import de.telran.gardenStore.exception.CategoryWithNameAlreadyExistsException;
import de.telran.gardenStore.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
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

//    private Category category1;
//    private Category category2;
//    private Category category3;
//    private Category categoryToCreate;
//    private Category categoryCreated;

//    @BeforeEach
//    void setUp() {
//        category1 = Category.builder()
//                .categoryId(1L)
//                .name("Fertilizer")
//                .build();
//
//        category2 = Category.builder()
//                .categoryId(2L)
//                .name("Protective products and septic tanks")
//                .build();
//
//        category3 = Category.builder()
//                .categoryId(3L)
//                .name("Planting material")
//                .build();
//
//        categoryToCreate = Category.builder()
//                .name("Pots and planters")
//                .build();
//
//        categoryCreated = categoryToCreate.toBuilder()
//                .categoryId(4L)
//                .build();
//    }

    @DisplayName("Get all categories")
    @Test
    void getAllCategories() {
        List<Category> expected = List.of(category1, category2, category3);

        when(categoryRepositoryMock.findAll()).thenReturn(List.of(category1, category2, category3));

        List<Category> actual = categoryService.getAllCategories();

        assertNotNull(actual);
        assertEquals(expected.size(), actual.size());
        assertEquals(expected, actual);
        verify(categoryRepositoryMock).findAll();
    }

    @DisplayName("Get category by ID : positive case")
    @Test
    void getCategoryByIdPositiveCase() {
        Long categoryId = category1.getCategoryId();

        Category expected = category1;

        when(categoryRepositoryMock.findById(categoryId)).thenReturn(Optional.of(expected));

        Category actual = categoryService.getCategoryById(categoryId);

        assertNotNull(actual);
        assertEquals(expected, actual);
        assertEquals(expected.getName(), actual.getName());
        verify(categoryRepositoryMock).findById(categoryId);
    }

    @DisplayName("Get category by ID : negative case")
    @Test
    void getCategoryByIdNegativeCase() {
        Long categoryId = 99L;

        when(categoryRepositoryMock.findById(categoryId)).thenReturn(Optional.empty());

        RuntimeException runtimeException = assertThrows(CategoryNotFoundException.class, () -> categoryService.getCategoryById(categoryId));
        assertEquals("Category with id " + categoryId + " not found", runtimeException.getMessage());
    }

    @DisplayName("Create new category : positive case")
    @Test
    void createCategoryPositiveCase() {
        Category expected = categoryCreated;

        when(categoryRepositoryMock.save(categoryToCreate)).thenReturn(expected);
        when(categoryRepositoryMock.findCategoryByName(categoryCreated.getName())).thenReturn(Optional.empty());

        Category actual = categoryService.createCategory(categoryToCreate);

        assertNotNull(actual);
        assertEquals(expected, actual);
        assertEquals(expected.getName(), actual.getName());
        verify(categoryRepositoryMock).save(categoryToCreate);
    }

    @DisplayName("Create new category : negative case")
    @Test
    void createCategoryNegativeCase() {
        Category categoryToCreate = Category.builder()
                .name("Planting material")
                .build();

        when(categoryRepositoryMock.findCategoryByName(categoryToCreate.getName())).thenReturn(Optional.of(category3));

        RuntimeException exception = assertThrows(CategoryWithNameAlreadyExistsException.class, () -> categoryService.createCategory(categoryToCreate));
        assertEquals("Category with name " + categoryToCreate.getName() + " already exists.", exception.getMessage());
    }

    @DisplayName("Update category by ID : positive case")
    @Test
    void updateCategoryPositiveCase() {

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

        Category actual = categoryService.updateCategory(categoryId, categoryToUpdate);

        assertNotNull(actual);
        assertEquals(categoryUpdated, actual);
        assertEquals(categoryUpdated.getName(), actual.getName());

        verify(categoryRepositoryMock).findById(categoryUpdated.getCategoryId());
        verify(categoryRepositoryMock).save(categoryUpdated);
    }

    @DisplayName("Update category by ID : negative case")
    @Test
    void updateCategoryNegativeCase() {

        String nameToUpdate = "Planting material";

        Long categoryId = 1L;

        Category categoryUpdate = Category.builder()
                .name(nameToUpdate)
                .build();

        when(categoryRepositoryMock.findById(categoryId)).thenReturn(Optional.of(category1));
        when(categoryRepositoryMock.findCategoryByName(nameToUpdate)).thenReturn(Optional.of(category3));

        RuntimeException exception = assertThrows(CategoryWithNameAlreadyExistsException.class, () -> categoryService.updateCategory(categoryId, categoryUpdate));
        assertEquals("Category with name " + nameToUpdate + " already exists.", exception.getMessage());
    }

    @DisplayName("Delete category by ID : positive case")
    @Test
    void deleteCategoryByIdPositiveCase() {
        Category deletedCategory = category1;

        Long categoryId = 1L;

        when(categoryRepositoryMock.findById(deletedCategory.getCategoryId())).thenReturn(Optional.of(deletedCategory));

        categoryService.deleteCategoryById(categoryId);

        verify(categoryRepositoryMock).delete(deletedCategory);
        verify(categoryRepositoryMock).findById(deletedCategory.getCategoryId());
    }

    @DisplayName("Delete category by ID : negative case")
    @Test
    void deleteCategoryByIdNegativeCase() {
        Long categoryId = 99L;

        when(categoryRepositoryMock.findById(categoryId)).thenReturn(Optional.empty());

        RuntimeException runtimeException = assertThrows(CategoryNotFoundException.class, () -> categoryService.deleteCategoryById(categoryId));
        assertEquals("Category with id " + categoryId + " not found", runtimeException.getMessage());
    }
}