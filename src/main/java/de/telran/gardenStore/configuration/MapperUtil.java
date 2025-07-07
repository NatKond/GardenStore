package de.telran.gardenStore.configuration;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import static org.modelmapper.convention.MatchingStrategies.STRICT;


@Configuration
@RequiredArgsConstructor
public class MapperUtil {

    @Bean
    public ModelMapper modelMapper() {

        ModelMapper modelMapper = new ModelMapper();

//        Converter<Long, Category> categoryIdToCategoryConverter =
//                context -> categoryService.getCategoryById(context.getSource());
//
//        Converter<Long, AppUser> userIdToUserConverter =
//                context -> userService.getUserById(context.getSource());
//
//        Converter<Long, Product> productIdToProductConverter =
//                context -> productService.getProductById(context.getSource());
//
//        Converter<String,String> passwordHashConverter =
//                context -> passwordEncoder.encode(context.getSource());
        modelMapper.getConfiguration()
                .setMatchingStrategy(STRICT)
                .setFieldMatchingEnabled(true)
                .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE);

//        modelMapper.typeMap(ProductCreateRequestDto.class, Product.class).addMappings(
//                mapper -> {
//                    mapper.skip(Product::setProductId);
//                    mapper
//                            .using(context -> categoryService.getCategoryById((Long) context.getSource()))
//                            .map(ProductCreateRequestDto::getCategoryId, Product::setCategory);
//                }
//        );
//
//        modelMapper.typeMap(FavoriteCreateRequestDto.class, Favorite.class).addMappings(
//                mapper -> {
//                    mapper.skip(Favorite::setFavoriteId);
//                    mapper
//                            .using(context -> userService.getUserById((Long)context.getSource()))
//                            .map(FavoriteCreateRequestDto::getUserId, Favorite::setUser);
//
//                    mapper
//                            .using(context -> productService.getProductById((Long)context.getSource()))
//                            .map(FavoriteCreateRequestDto::getProductId, Favorite::setProduct);
//                }
//        );
//
//        modelMapper.typeMap(Product.class, ProductResponseDto.class).addMappings(
//                (mapper ->
//                        mapper.map(product -> product.getCategory().getCategoryId(), ProductResponseDto::setCategoryId)));

//        modelMapper.typeMap(Favorite.class, FavoriteResponseDto.class).addMappings(
//                (mapper -> {
//                    mapper.map(favorite -> favorite.getProduct().getProductId(), FavoriteResponseDto::setProductId);
//                    mapper.map(favorite -> favorite.getUser().getUserId(), FavoriteResponseDto::setUserId);
//                }));

        return modelMapper;
    }
}
