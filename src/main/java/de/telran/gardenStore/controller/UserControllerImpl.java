package de.telran.gardenStore.controller;

import de.telran.gardenStore.converter.Converter;
import de.telran.gardenStore.dto.*;
import de.telran.gardenStore.dto.security.LoginRequest;
import de.telran.gardenStore.dto.security.LoginResponse;
import de.telran.gardenStore.entity.AppUser;
import de.telran.gardenStore.service.UserService;
import de.telran.gardenStore.service.security.AuthenticationService;
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

    private final AuthenticationService authenticationService;

    @Override
    public List<UserShortResponseDto> getAll() {
        return userConverter.convertEntityListToDtoList(userService.getAll());
    }

    @Override
    public UserResponseDto getById(@Positive Long userId) {
        return userConverter.convertEntityToDto(userService.getById(userId));
    }

    @Override
    public UserResponseDto getCurrent() {
        return userConverter.convertEntityToDto(
                userService.getCurrent());
    }

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        return authenticationService.authenticate(loginRequest);
    }

    @Override
    public UserResponseDto create(@Valid UserCreateRequestDto userCreateRequestDto) {
        return userConverter.convertEntityToDto(
                userService.create(
                        userConverter.convertDtoToEntity(userCreateRequestDto)));
    }

    @Override
    public UserResponseDto update(@Valid UserCreateRequestDto userRequest) {
        return userConverter.convertEntityToDto(
                userService.update(
                        userConverter.convertDtoToEntity(userRequest)));
    }

    @Override
    public void delete() {
        userService.delete();
    }
}
