package de.telran.gardenStore.controller;

import de.telran.gardenStore.dto.UserCreateRequestDto;
import de.telran.gardenStore.dto.UserResponseDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Validated
public interface UserController {

    List<UserResponseDto> getAllUsers();

    UserResponseDto getUserById(@Positive Long id);
    
    UserResponseDto createUser(@Valid UserCreateRequestDto userRequest);

    void deleteUserById(@Positive Long id);
}
