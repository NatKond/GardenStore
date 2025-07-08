package de.telran.gardenStore.controller;

import de.telran.gardenStore.converter.Converter;
import de.telran.gardenStore.dto.*;
import de.telran.gardenStore.entity.AppUser;
import de.telran.gardenStore.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@Validated
@RequestMapping("/v1/users")
public class UserControllerImpl implements UserController {

    private final UserService userService;

    private final Converter<AppUser, UserCreateRequestDto, UserResponseDto, UserShortResponseDto> userConverter;

    @Override
    @GetMapping
    public List<UserShortResponseDto> getAllUsers() {
        return userConverter.convertEntityListToDtoList(userService.getAllUsers());
    }

    @Override
    @GetMapping("/{userId}")
    public UserResponseDto getUserById(@PathVariable @Positive Long userId) {
        return userConverter.convertEntityToDto(userService.getUserById(userId));
    }

    @Override
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/register")
    public UserResponseDto createUser(@RequestBody @Valid UserCreateRequestDto userCreateRequestDto) {
        return userConverter.convertEntityToDto(
                userService.createUser(userConverter.convertDtoToEntity(userCreateRequestDto)));
    }

    @Override
    @ResponseStatus(HttpStatus.ACCEPTED)
    @PutMapping("/{userId}")
    public UserResponseDto updateUser(@PathVariable @Positive Long userId, @RequestBody @Valid UserCreateRequestDto userRequest) {
        return userConverter.convertEntityToDto(
                userService.updateUser(userId, userConverter.convertDtoToEntity(userRequest)));
    }

    @Override
    @DeleteMapping("/{userId}")
    public void deleteUserById(@PathVariable @Positive Long userId) {
        userService.deleteUserById(userId);
    }
}
