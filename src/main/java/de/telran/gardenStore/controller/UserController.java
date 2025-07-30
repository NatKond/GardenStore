package de.telran.gardenStore.controller;

import de.telran.gardenStore.dto.UserCreateRequestDto;
import de.telran.gardenStore.dto.UserResponseDto;
import de.telran.gardenStore.dto.UserShortResponseDto;
import de.telran.gardenStore.dto.security.LoginRequest;
import de.telran.gardenStore.dto.security.LoginResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/v1/users")
public interface UserController {

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    List<UserShortResponseDto> getAll();

    @GetMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    UserResponseDto getById(@PathVariable @Positive Long userId);

    @GetMapping("/me")
    @PreAuthorize("hasRole('USER')")
    UserResponseDto getCurrent();

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/register")
    UserResponseDto create(@RequestBody @Valid UserCreateRequestDto userCreateRequestDto);

    @PostMapping("/login")
    LoginResponse login(@RequestBody @Valid LoginRequest loginRequest);

    @PreAuthorize("hasRole('USER')")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @PutMapping
    UserResponseDto update(@RequestBody @Valid UserCreateRequestDto userRequest);

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping
    void delete();
}
