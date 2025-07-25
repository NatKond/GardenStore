package de.telran.gardenStore.converter;

import de.telran.gardenStore.dto.ProductCreateRequestDto;
import de.telran.gardenStore.dto.ProductResponseDto;
import de.telran.gardenStore.dto.ProductShortResponseDto;
import de.telran.gardenStore.entity.Category;
import de.telran.gardenStore.entity.Product;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ProductConverter implements Converter<Product, ProductCreateRequestDto, ProductResponseDto, ProductShortResponseDto> {

    private final ModelMapper modelMapper;

    @Override
    public Product convertDtoToEntity(ProductCreateRequestDto productCreateRequestDto) {
        modelMapper.typeMap(ProductCreateRequestDto.class, Product.class).addMappings(
                mapper -> {
                    mapper.skip(Product::setProductId);
                    mapper
                            .using(context -> Category.builder().categoryId((Long) context.getSource()).build())
                            .map(ProductCreateRequestDto::getCategoryId, Product::setCategory);
                }
        );

        return modelMapper.map(productCreateRequestDto, Product.class);
    }

    @Override
    public ProductResponseDto convertEntityToDto(Product product) {
        modelMapper.typeMap(Product.class, ProductResponseDto.class).addMappings(
                (mapper ->
                        mapper.map(productEntity -> productEntity.getCategory().getCategoryId(), ProductResponseDto::setCategoryId)));
        return modelMapper.map(product, ProductResponseDto.class);
    }

    @Override
    public List<ProductShortResponseDto> convertEntityListToDtoList(List<Product> products) {
        modelMapper.typeMap(Product.class, ProductShortResponseDto.class).addMappings(
                (mapper ->
                        mapper.map(productEntity -> productEntity.getCategory().getCategoryId(), ProductShortResponseDto::setCategoryId)));

        return ConverterEntityToDto.convertList(products, (product) ->  modelMapper.map(product, ProductShortResponseDto.class));
    }
}
