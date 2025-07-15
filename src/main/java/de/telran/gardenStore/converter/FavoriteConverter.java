package de.telran.gardenStore.converter;

import de.telran.gardenStore.dto.FavoriteCreateRequestDto;
import de.telran.gardenStore.dto.FavoriteResponseDto;
import de.telran.gardenStore.entity.Favorite;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class FavoriteConverter implements Converter<Favorite, FavoriteCreateRequestDto, FavoriteResponseDto, FavoriteResponseDto> {

    private final ModelMapper modelMapper;

    @Override
    public Favorite convertDtoToEntity(FavoriteCreateRequestDto favoriteCreateRequestDto) {
        return modelMapper.map(favoriteCreateRequestDto, Favorite.class);
    }

    @Override
    public FavoriteResponseDto convertEntityToDto(Favorite favorite) {
        return modelMapper.map(favorite, FavoriteResponseDto.class);
    }

    @Override
    public List<FavoriteResponseDto> convertEntityListToDtoList(List<Favorite> favorites) {
        return Converter.convertList(favorites, this::convertEntityToDto);
    }
}
