package de.telran.gardenStore.controller;

import de.telran.gardenStore.dto.UserCreateRequestDto;
import de.telran.gardenStore.dto.UserResponseDto;
import de.telran.gardenStore.dto.UserShortResponseDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/v1/users")
public interface UserController {

    @GetMapping
    List<UserShortResponseDto> getAllUsers();

    @GetMapping("/{userId}")
    UserResponseDto getUserById(@PathVariable @Positive Long userId);

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/register")
    UserResponseDto createUser(@RequestBody @Valid UserCreateRequestDto userRequest);

    @ResponseStatus(HttpStatus.ACCEPTED)
    @PutMapping("/{userId}")
    UserResponseDto updateUser(@PathVariable @Positive Long userId,
                                    @RequestBody @Valid UserCreateRequestDto userRequest);

    @DeleteMapping("/{userId}")
    void deleteUserById(@PathVariable @Positive Long userId);
}
