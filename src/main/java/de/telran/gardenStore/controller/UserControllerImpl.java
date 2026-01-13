package de.telran.gardenStore.controller;

import de.telran.gardenStore.annotation.Loggable;
import de.telran.gardenStore.converter.Converter;
import de.telran.gardenStore.dto.UserCreateRequestDto;
import de.telran.gardenStore.dto.UserResponseDto;
import de.telran.gardenStore.dto.UserShortResponseDto;
import de.telran.gardenStore.dto.security.LoginRequest;
import de.telran.gardenStore.dto.security.LoginResponse;
import de.telran.gardenStore.entity.AppUser;
import de.telran.gardenStore.service.UserService;
import de.telran.gardenStore.service.security.AuthenticationService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@Validated
@RequestMapping("/v1/users")
@RestController
public class UserControllerImpl implements UserController {

    private final UserService userService;

    private final Converter<AppUser, UserCreateRequestDto, UserResponseDto, UserShortResponseDto> userConverter;

    private final AuthenticationService authenticationService;

    @Override
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserShortResponseDto> getAll() {
        return userConverter.toDtoList(
                userService.getAll());
    }

    @Override
    @GetMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public UserResponseDto getById(@PathVariable @Positive Long userId) {
        return userConverter.toDto(
                userService.getById(userId));
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('USER')")
    @Override
    public UserResponseDto getCurrent() {
        return userConverter.toDto(
                userService.getCurrent());
    }

    @Override
    @PostMapping("/login")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public LoginResponse login(@RequestBody  @Valid LoginRequest loginRequest) {
        return authenticationService.authenticate(loginRequest);
    }

    @Override
    @Loggable
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/register")
    public UserResponseDto create(@RequestBody @Valid UserCreateRequestDto userCreateRequestDto) {
        return userConverter.toDto(
                userService.create(
                        userConverter.toEntity(userCreateRequestDto)));
    }

    @Override
    @Loggable
    @ResponseStatus(HttpStatus.ACCEPTED)
    @PutMapping
    public UserResponseDto update(@RequestBody @Valid UserCreateRequestDto userRequest) {
        return userConverter.toDto(
                userService.update(
                        userConverter.toEntity(userRequest)));
    }

    @Override
    @Loggable
    @DeleteMapping()
    public void delete() {
        userService.delete();
    }
}
