package de.telran.gardenStore.controller;

import de.telran.gardenStore.converter.Converter;
import de.telran.gardenStore.dto.*;
import de.telran.gardenStore.entity.AppUser;
import de.telran.gardenStore.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@Validated
public class UserControllerImpl implements UserController {

    private final UserService userService;

    private final Converter<AppUser, UserCreateRequestDto, UserResponseDto, UserShortResponseDto> userConverter;
    //private final UserConverter userConverter;


    @Override
    public List<UserShortResponseDto> getAllUsers() {
        return userConverter.convertEntityListToDtoList(userService.getAllUsers());
    }

    @Override
    public UserResponseDto getUserById(@Positive Long userId) {
        return userConverter.convertEntityToDto(userService.getUserById(userId));
    }

    @Override
    public UserResponseDto createUser(@RequestBody @Valid UserCreateRequestDto userCreateRequestDto) {
        return userConverter.convertEntityToDto(
                userService.createUser(userConverter.convertDtoToEntity(userCreateRequestDto)));
    }

    @Override
    public UserResponseDto updateUser(@Positive Long userId, @Valid UserCreateRequestDto userRequest) {
        return userConverter.convertEntityToDto(
                userService.updateUser(userId, userConverter.convertDtoToEntity(userRequest)));
    }

    @Override
    public void deleteUserById(@Positive Long userId) {
        userService.deleteUserById(userId);
    }
}
