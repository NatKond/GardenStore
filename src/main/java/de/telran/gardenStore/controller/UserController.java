package de.telran.gardenStore.controller;

import de.telran.gardenStore.dto.UserCreateRequestDto;
import de.telran.gardenStore.dto.UserResponseDto;

import java.util.List;

public interface UserController {

    List<UserResponseDto> getAllUsers();

    UserResponseDto getUserById(Long id);

    UserResponseDto createUser(UserCreateRequestDto userRequest);

    void deleteUserById(Long id);
}
