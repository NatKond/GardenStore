package de.telran.gardenStore.converter;

import de.telran.gardenStore.dto.UserCreateRequestDto;
import de.telran.gardenStore.dto.UserResponseDto;
import de.telran.gardenStore.dto.UserShortResponseDto;
import de.telran.gardenStore.entity.AppUser;
import de.telran.gardenStore.entity.Favorite;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UserConverter implements Converter<AppUser, UserCreateRequestDto, UserResponseDto, UserShortResponseDto> {

    private final ModelMapper modelMapper;

    private final PasswordEncoder passwordEncoder;

    private final FavoriteConverter favoriteConverter;

    @Override
    public AppUser convertDtoToEntity(UserCreateRequestDto userCreateRequestDto) {
        modelMapper.typeMap(UserCreateRequestDto.class, AppUser.class).addMappings(
                mapper -> mapper
                        .using(context -> passwordEncoder.encode((String)context.getSource()))
                        .map(UserCreateRequestDto::getPassword, AppUser::setPasswordHash));

        return modelMapper.map(userCreateRequestDto, AppUser.class);
    }

    @Override
    public UserResponseDto convertEntityToDto(AppUser user) {
        modelMapper.typeMap(AppUser.class, UserResponseDto.class).addMappings(
                mapper -> mapper
                        .using(context -> favoriteConverter.convertEntityListToDtoList((List<Favorite>)context.getSource()))
                        .map(AppUser::getFavorites, UserResponseDto::setFavorites));

        return modelMapper.map(user, UserResponseDto.class);
    }

    @Override
    public List<UserShortResponseDto> convertEntityListToDtoList(List<AppUser> users) {
        return Converter.convertList(users, user -> modelMapper.map(user, UserShortResponseDto.class));
    }

}

