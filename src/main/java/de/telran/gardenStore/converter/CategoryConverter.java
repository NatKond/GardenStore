package de.telran.gardenStore.converter;

import de.telran.gardenStore.dto.CategoryCreateRequestDto;
import de.telran.gardenStore.dto.CategoryResponseDto;
import de.telran.gardenStore.dto.CategoryShortResponseDto;
import de.telran.gardenStore.entity.Category;
import de.telran.gardenStore.entity.Product;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CategoryConverter implements Converter<Category,CategoryCreateRequestDto, CategoryResponseDto, CategoryShortResponseDto> {

    private final ModelMapper modelMapper;

    private final ProductConverter productConverter;

    @Override
    public Category toEntity(CategoryCreateRequestDto categoryCreateRequestDto) {
        return modelMapper.map(categoryCreateRequestDto, Category.class);
    }

    @Override
    public CategoryResponseDto toDto(Category category) {
        modelMapper.typeMap(Category.class, CategoryResponseDto.class).addMappings(
                mapper -> mapper
                        .using(context -> productConverter.toDtoList((List<Product>) context.getSource()))
                        .map(Category::getProducts, CategoryResponseDto::setProducts)
        );

        return modelMapper.map(category, CategoryResponseDto.class);
    }

    @Override
    public List<CategoryShortResponseDto> toDtoList(List<Category> categories) {
        return ConverterEntityToDto.toList(categories, category -> modelMapper.map(category, CategoryShortResponseDto.class));
    }
}
