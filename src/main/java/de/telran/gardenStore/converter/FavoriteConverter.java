package de.telran.gardenStore.converter;

import de.telran.gardenStore.dto.FavoriteResponseDto;
import de.telran.gardenStore.entity.Favorite;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class FavoriteConverter implements ConverterEntityToDto<Favorite, FavoriteResponseDto, FavoriteResponseDto> {

    private final ModelMapper modelMapper;

    @Override
    public FavoriteResponseDto convertEntityToDto(Favorite favorite) {
        return modelMapper.map(favorite, FavoriteResponseDto.class);
    }

    @Override
    public List<FavoriteResponseDto> convertEntityListToDtoList(List<Favorite> favorites) {
        return ConverterEntityToDto.convertList(favorites, this::convertEntityToDto);
    }
}
