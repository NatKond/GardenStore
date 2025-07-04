package de.telran.gardenStore.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.telran.gardenStore.dto.CategoryCreateRequestDto;
import de.telran.gardenStore.dto.CategoryResponseDto;
import de.telran.gardenStore.entity.Category;
import de.telran.gardenStore.exception.CategoryNotFoundException;
import de.telran.gardenStore.service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CategoryControllerImpl.class)
@AutoConfigureMockMvc(addFilters = false)
class CategoryControllerImplTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CategoryService categoryService;

    @MockitoBean
    private ModelMapper modelMapper;

    private Category category1;
    private Category category2;
    private Category category3;
    private Category category4;

    private CategoryResponseDto categoryResponseDto1;
    private CategoryResponseDto categoryResponseDto2;
    private CategoryResponseDto categoryResponseDto3;

    @BeforeEach
    void setUp() {
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

        categoryResponseDto1 = CategoryResponseDto.builder()
                .categoryId(category1.getCategoryId())
                .name(category1.getName())
                .build();

        categoryResponseDto2 = CategoryResponseDto.builder()
                .categoryId(category2.getCategoryId())
                .name(category2.getName())
                .build();

        categoryResponseDto3 = CategoryResponseDto.builder()
                .categoryId(category3.getCategoryId())
                .name(category3.getName())
                .build();
    }

    @DisplayName("Test method getAllCategories")
    @Test
    void getAllCategories() throws Exception {
        List<Category> categories = List.of(category1, category2, category3);

        List<CategoryResponseDto> categoriesDto = List.of(categoryResponseDto1, categoryResponseDto2, categoryResponseDto3);

        when(categoryService.getAllCategories()).thenReturn(categories);

        when(modelMapper.map(category1, CategoryResponseDto.class)).thenReturn(categoryResponseDto1);
        when(modelMapper.map(category2, CategoryResponseDto.class)).thenReturn(categoryResponseDto2);
        when(modelMapper.map(category3, CategoryResponseDto.class)).thenReturn(categoryResponseDto3);

        mockMvc
                .perform(get("/v1/categories"))
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json(objectMapper.writeValueAsString(categoriesDto)));
    }

    @DisplayName("Test method getCategoryById positive case")
    @Test
    void getCategoryByIdPositiveCase() throws Exception {
        CategoryResponseDto categoryResponseDto1 = CategoryResponseDto.builder()
                .categoryId(category1.getCategoryId())
                .name(category1.getName())
                .build();

        when(categoryService.getCategoryById(category1.getCategoryId())).thenReturn(category1);

        when(modelMapper.map(category1, CategoryResponseDto.class)).thenReturn(categoryResponseDto1);

        mockMvc
                .perform(get("/v1/categories/{categoryId}", category1.getCategoryId()))
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json(objectMapper.writeValueAsString(categoryResponseDto1)));

    }

    @DisplayName("Test method getCategoryById negative case")
    @Test
    void getCategoryByIdNegativeCase() throws Exception {
        Long categoryId = 6L;

        when(categoryService.getCategoryById(categoryId)).thenThrow(new CategoryNotFoundException("Category with id " + categoryId + " not found"));

        mockMvc
                .perform(get("/v1/categories/{categoryId}", categoryId))
                .andDo(print())
                .andExpectAll(
                        status().isNotFound(),
                        jsonPath("$.exception").value("CategoryNotFoundException"),
                        jsonPath("$.message").value("Category with id " + categoryId + " not found"),
                        jsonPath("$.status").value(404));

    }

    @DisplayName("Test method createCategory")
    @Test
    void createCategory() throws Exception {

        Category categoryCreate = category4;

        Category categoryCreated = category4.toBuilder()
                .categoryId(4L)
                .build();

        CategoryCreateRequestDto categoryCreateRequestDto = CategoryCreateRequestDto.builder()
                .name(categoryCreate.getName())
                .build();

        CategoryResponseDto categoryResponseCreatedDto = CategoryResponseDto.builder()
                .categoryId(categoryCreated.getCategoryId())
                .name(categoryCreated.getName())
                .build();

        when(categoryService.createCategory(categoryCreate)).thenReturn(categoryCreated);

        when(modelMapper.map(categoryCreateRequestDto, Category.class)).thenReturn(categoryCreate);
        when(modelMapper.map(categoryCreated, CategoryResponseDto.class)).thenReturn(categoryResponseCreatedDto);

        mockMvc
                .perform(post("/v1/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoryCreateRequestDto)))
                .andDo(print())
                .andExpectAll(
                        status().isCreated(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json(objectMapper.writeValueAsString(categoryResponseCreatedDto)));
    }

    @DisplayName("Test method updateCategory")
    @Test
    void updateCategory() throws Exception {

        Category categoryUpdate = category1.toBuilder()
                .name("Soil and substrates")
                .build();

        Category categoryUpdated = categoryUpdate.toBuilder()
                .categoryId(1L)
                .build();

        CategoryCreateRequestDto categoryUpdateRequestDto = CategoryCreateRequestDto.builder()
                .name(categoryUpdate.getName())
                .build();

        CategoryResponseDto categoryResponseUpdatedDto = CategoryResponseDto.builder()
                .categoryId(categoryUpdated.getCategoryId())
                .name(categoryUpdated.getName())
                .build();

        when(categoryService.updateCategory(categoryUpdated.getCategoryId(), categoryUpdate)).thenReturn(categoryUpdated);
        when(categoryService.getCategoryById(categoryUpdated.getCategoryId())).thenReturn(categoryUpdated);

        when(modelMapper.map(categoryUpdateRequestDto, Category.class)).thenReturn(categoryUpdate);
        when(modelMapper.map(categoryUpdated, CategoryResponseDto.class)).thenReturn(categoryResponseUpdatedDto);

        mockMvc
                .perform(put("/v1/categories/{category_id}", categoryUpdated.getCategoryId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoryUpdateRequestDto)))
                .andDo(print())
                .andExpectAll(
                        status().isAccepted(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json(objectMapper.writeValueAsString(categoryResponseUpdatedDto)));
    }

    @DisplayName("Test method deleteCategory")
    @Test
    void deleteCategory() throws Exception {
        Long categoryId = category1.getCategoryId();

        mockMvc
                .perform(delete("/v1/categories/{category_id}", categoryId))
                .andDo(print())
                .andExpect(status().isOk());
    }
}