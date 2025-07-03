package de.telran.gardenStore.controller;

import de.telran.gardenStore.dto.UserCreateRequestDto;
import de.telran.gardenStore.dto.UserResponseDto;
import de.telran.gardenStore.entity.AppUser;
import de.telran.gardenStore.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@Validated
public class UserControllerImpl implements UserController {

    private final UserService userService;

    private final ModelMapper modelMapper;

    @Override
    public List<UserResponseDto> getAllUsers() {
        return userService.getAllUsers().stream().map(appUser -> modelMapper.map(appUser, UserResponseDto.class)).collect(Collectors.toList());
    }

    @Override
    public UserResponseDto getUserById(@Positive Long userId) {
        return modelMapper.map(userService.getUserById(userId), UserResponseDto.class);
    }

    @Override
    public UserResponseDto createUser(@RequestBody @Valid UserCreateRequestDto userRequest) {
        return modelMapper.map(
                userService.createUser(
                        modelMapper.map(userRequest, AppUser.class)),
                UserResponseDto.class);
    }

    @Override
    public UserResponseDto updateUser(@Positive Long userId, @Valid UserCreateRequestDto userRequest) {
        return modelMapper.map(
                userService.updateUser(userId,
                        modelMapper.map(userRequest, AppUser.class)),
                UserResponseDto.class);
    }

    @Override
    public void deleteUserById(@Positive Long userId) {
        userService.deleteUserById(userId);
    }
}
