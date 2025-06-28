package de.telran.gardenStore.service;

import de.telran.gardenStore.dto.*;
import de.telran.gardenStore.entity.Category;
import de.telran.gardenStore.exception.CategoryNotFoundException;
import de.telran.gardenStore.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public abstract class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<CategoryResponseDto> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryResponseDto getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found"));
        return convertToDto(category);
    }

    @Override
    public CategoryResponseDto createCategory(CategoryCreateRequestDto dto) {
        Category category = modelMapper.map(dto, Category.class);
        Category savedCategory = categoryRepository.save(category);
        return convertToDto(savedCategory);
    }

    @Override
    public void deleteCategoryById(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new CategoryNotFoundException("Category not found");
        }
        categoryRepository.deleteById(id);
    }

    private CategoryResponseDto convertToDto(Category category) {
        return modelMapper.map(category, CategoryResponseDto.class);
    }
}
//vnizu bolee sovershennaya versiya i bolee zaschischennaya
//@Service
//@RequiredArgsConstructor
//public class CategoryServiceImpl implements CategoryService {
//    private final CategoryRepository categoryRepository;
//    private final ModelMapper modelMapper;
//
//    @Override
//    public List<CategoryResponseDto> getAllCategories() {
//        return categoryRepository.findAll().stream()
//                .map(category -> modelMapper.map(category, CategoryResponseDto.class))
//                .collect(Collectors.toList());
//    }
//
//    @Override
//    public CategoryResponseDto getCategoryById(Long id) {
//        Category category = categoryRepository.findById(id)
//                .orElseThrow(() -> new CategoryNotFoundException("Category not found with id: " + id));
//        return modelMapper.map(category, CategoryResponseDto.class);
//    }
//
//    @Override
//    public CategoryResponseDto createCategory(CategoryCreateRequestDto dto) {
//        Category category = modelMapper.map(dto, Category.class);
//        category.setActive(true);
//        Category saved = categoryRepository.save(category);
//        return modelMapper.map(saved, CategoryResponseDto.class);
//    }
//
//    @Override
//    public void deleteCategoryById(Long id) {
//        if (!categoryRepository.existsById(id)) {
//            throw new CategoryNotFoundException("Category not found with id: " + id);
//        }
//        categoryRepository.deleteById(id);
//    }
//}
