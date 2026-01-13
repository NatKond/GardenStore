package de.telran.gardenStore.converter;

import de.telran.gardenStore.dto.UserCreateRequestDto;
import de.telran.gardenStore.dto.UserResponseDto;
import de.telran.gardenStore.dto.UserShortResponseDto;
import de.telran.gardenStore.entity.AppUser;
import de.telran.gardenStore.entity.Favorite;
import de.telran.gardenStore.enums.Role;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserConverter implements Converter<AppUser, UserCreateRequestDto, UserResponseDto, UserShortResponseDto> {

    private final ModelMapper modelMapper;

    private final PasswordEncoder passwordEncoder;

    private final FavoriteConverter favoriteConverter;

    @Override
    public AppUser toEntity(UserCreateRequestDto userCreateRequestDto) {
        modelMapper.typeMap(UserCreateRequestDto.class, AppUser.class).addMappings(
                mapper -> mapper
                        .using(context -> passwordEncoder.encode((String) context.getSource()))
                        .map(UserCreateRequestDto::getPassword, AppUser::setPasswordHash));

        return modelMapper.map(userCreateRequestDto, AppUser.class);
    }

    @Override
    public UserResponseDto toDto(AppUser user) {
        modelMapper.typeMap(AppUser.class, UserResponseDto.class).addMappings(
                mapper -> {
                    mapper
                            .using(context -> favoriteConverter.toDtoList((List<Favorite>) context.getSource()))
                            .map(AppUser::getFavorites, UserResponseDto::setFavorites);
                    mapper
                            .using(context -> convertRoles((Set<Role>) context.getSource()))
                            .map(AppUser::getRoles, UserResponseDto::setRoles);
                }
        );
        return modelMapper.map(user, UserResponseDto.class);
    }

    @Override
    public List<UserShortResponseDto> toDtoList(List<AppUser> users) {
        modelMapper.typeMap(AppUser.class, UserShortResponseDto.class).addMappings(
                mapper ->
                        mapper
                                .using(context -> convertRoles((Set<Role>) context.getSource()))
                                .map(AppUser::getRoles, UserShortResponseDto::setRoles)
        );
        return ConverterEntityToDto.toList(users, user -> modelMapper.map(user, UserShortResponseDto.class));
    }

    private List<String> convertRoles(Set<Role> roles) {
        return roles.stream().map(Role::name).collect(Collectors.toList());
    }

}

