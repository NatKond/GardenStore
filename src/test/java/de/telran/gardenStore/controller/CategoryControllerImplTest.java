package de.telran.gardenStore.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.telran.gardenStore.AbstractTest;
import de.telran.gardenStore.converter.Converter;
import de.telran.gardenStore.dto.CategoryCreateRequestDto;
import de.telran.gardenStore.dto.CategoryResponseDto;
import de.telran.gardenStore.dto.CategoryShortResponseDto;
import de.telran.gardenStore.entity.Category;
import de.telran.gardenStore.exception.CategoryNotFoundException;
import de.telran.gardenStore.exception.CategoryWithNameAlreadyExistsException;
import de.telran.gardenStore.service.CategoryService;
import de.telran.gardenStore.service.security.JwtService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CategoryControllerImpl.class)
@AutoConfigureMockMvc(addFilters = false)
public class CategoryControllerImplTest extends AbstractTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private CategoryService categoryService;

    @MockitoBean
    private Converter<Category, CategoryCreateRequestDto, CategoryResponseDto, CategoryShortResponseDto> categoryConverter;

    @DisplayName("GET /v1/categories - Get all categories")
    @Test
    void getAll() throws Exception {
        List<Category> categories = List.of(category1, category2, category3);

        List<CategoryShortResponseDto> categoriesDto = List.of(categoryShortResponseDto1, categoryShortResponseDto2, categoryShortResponseDto3);

        when(categoryService.getAll()).thenReturn(categories);

        when(categoryConverter.convertEntityListToDtoList(categories)).thenReturn(categoriesDto);

        mockMvc
                .perform(get("/v1/categories"))
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json(objectMapper.writeValueAsString(categoriesDto)));
    }

    @DisplayName("GET /v1/categories/{categoryId} - Get category by ID : positive case")
    @Test
    void getByIdPositiveCase() throws Exception {

        when(categoryService.getById(category1.getCategoryId())).thenReturn(category1);

        when(categoryConverter.convertEntityToDto(category1)).thenReturn(categoryResponseDto1);

        mockMvc
                .perform(get("/v1/categories/{categoryId}", category1.getCategoryId()))
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json(objectMapper.writeValueAsString(categoryResponseDto1)));

    }

    @DisplayName("GET /v1/categories/{categoryId} - Get category by ID : negative case")
    @Test
    void getByIdNegativeCase() throws Exception {
        Long categoryId = 6L;

        when(categoryService.getById(categoryId)).thenThrow(new CategoryNotFoundException("Category with id " + categoryId + " not found"));

        mockMvc
                .perform(get("/v1/categories/{categoryId}", categoryId))
                .andDo(print())
                .andExpectAll(
                        status().isNotFound(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.exception").value("CategoryNotFoundException"),
                        jsonPath("$.message").value("Category with id " + categoryId + " not found"),
                        jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()));

    }

    @DisplayName("POST /v1/categories - Create new category : positive case")
    @Test
    void createPositiveCase() throws Exception {

        Category categoryCreated = this.categoryToCreate.toBuilder()
                .categoryId(4L)
                .build();

        CategoryResponseDto categoryResponseCreatedDto = CategoryResponseDto.builder()
                .categoryId(categoryCreated.getCategoryId())
                .name(categoryCreated.getName())
                .build();

        when(categoryService.create(categoryToCreate)).thenReturn(categoryCreated);
        when(categoryConverter.convertDtoToEntity(categoryCreateRequestDto)).thenReturn(categoryToCreate);
        when(categoryConverter.convertEntityToDto(categoryCreated)).thenReturn(categoryResponseCreatedDto);

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

    @DisplayName("POST /v1/categories - Create new category : negative case")
    @Test
    void createNegativeCase() throws Exception {

        when(categoryConverter.convertDtoToEntity(categoryCreateRequestDto)).thenReturn(categoryToCreate);
        when(categoryService.create(categoryToCreate)).thenThrow(new CategoryWithNameAlreadyExistsException("Category with name " + categoryToCreate.getName() + " already exists."));

        mockMvc
                .perform(post("/v1/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoryCreateRequestDto)))
                .andDo(print())
                .andExpectAll(
                        status().isConflict(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.exception").value("CategoryWithNameAlreadyExistsException"),
                        jsonPath("$.message").value("Category with name " + categoryToCreate.getName() + " already exists."),
                        jsonPath("$.status").value(HttpStatus.CONFLICT.value()));
    }

    @DisplayName("PUT /v1/categories/{category_id} - Update category")
    @Test
    void update() throws Exception {

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

        when(categoryConverter.convertDtoToEntity(categoryUpdateRequestDto)).thenReturn(categoryUpdate);
        when(categoryService.update(categoryUpdated.getCategoryId(), categoryUpdate)).thenReturn(categoryUpdated);
        when(categoryService.getById(categoryUpdated.getCategoryId())).thenReturn(categoryUpdated);
        when(categoryConverter.convertEntityToDto(categoryUpdated)).thenReturn(categoryResponseUpdatedDto);

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

    @DisplayName("DELETE /v1/categories/{category_id} - Delete category by ID")
    @Test
    void delete() throws Exception {
        Long categoryId = category1.getCategoryId();

        doNothing().when(categoryService).deleteById(categoryId);

        mockMvc
                .perform(MockMvcRequestBuilders.delete("/v1/categories/{category_id}", categoryId))
                .andDo(print())
                .andExpect(status().isOk());

        verify(categoryService).deleteById(categoryId);
    }
}