package de.telran.gardenStore.converter;

import de.telran.gardenStore.dto.FavoriteCreateRequestDto;
import de.telran.gardenStore.dto.FavoriteResponseDto;
import de.telran.gardenStore.entity.Favorite;
import de.telran.gardenStore.service.ProductService;
import de.telran.gardenStore.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class FavoriteConverter implements Converter<Favorite, FavoriteCreateRequestDto, FavoriteResponseDto, FavoriteResponseDto> {

    private final ModelMapper modelMapper;

    private final UserService userService;

    private final ProductService productService;

    @Override
    public Favorite convertDtoToEntity(FavoriteCreateRequestDto favoriteCreateRequestDto) {
        modelMapper.typeMap(FavoriteCreateRequestDto.class, Favorite.class).addMappings(
                mapper -> {
                    mapper.skip(Favorite::setFavoriteId);
                    mapper
                            .using(context -> userService.getUserById((Long) context.getSource()))
                            .map(FavoriteCreateRequestDto::getUserId, Favorite::setUser);

                    mapper
                            .using(context -> productService.getProductById((Long) context.getSource()))
                            .map(FavoriteCreateRequestDto::getProductId, Favorite::setProduct);
                }
        );

        return modelMapper.map(favoriteCreateRequestDto, Favorite.class);
    }

    @Override
    public FavoriteResponseDto convertEntityToDto(Favorite favorite) {
        modelMapper.typeMap(Favorite.class, FavoriteResponseDto.class).addMappings(
                (mapper -> {
//                    mapper.map(favoriteEntity -> favoriteEntity.getProduct().getProductId(), FavoriteResponseDto::setProductId);
                    mapper.map(favoriteEntity -> favoriteEntity.getUser().getUserId(), FavoriteResponseDto::setUserId);
                }));

        return modelMapper.map(favorite, FavoriteResponseDto.class);
    }

    @Override
    public List<FavoriteResponseDto> convertEntityListToDtoList(List<Favorite> favorites) {
        return Converter.convertList(favorites, this::convertEntityToDto);
    }
}
