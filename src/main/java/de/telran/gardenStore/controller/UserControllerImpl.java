package de.telran.gardenStore.controller;

import de.telran.gardenStore.dto.UserCreateRequestDto;
import de.telran.gardenStore.dto.UserResponseDto;
import de.telran.gardenStore.entity.AppUser;
import de.telran.gardenStore.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
@Validated
public class UserControllerImpl implements UserController{

    private final UserService userService;

    private final ModelMapper modelMapper;

    @Override
    @GetMapping
    public List<UserResponseDto> getAllUsers() {
        return userService.getAllUsers().stream().map(appUser -> modelMapper.map(appUser, UserResponseDto.class)).collect(Collectors.toList());
    }

    @Override
    @GetMapping("/{userId}")
    public UserResponseDto getUserById(@PathVariable @Positive Long userId) {
        return modelMapper.map(userService.getUserById(userId), UserResponseDto.class);
    }

    @Override
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/register")
    public UserResponseDto createUser(@RequestBody @Valid UserCreateRequestDto userRequest) {
        return modelMapper.map(
                userService.createUser(
                        modelMapper.map(userRequest, AppUser.class)),
                UserResponseDto.class);
    }

    @Override
    @DeleteMapping("/{userId}")
    public void deleteUserById(@PathVariable @Positive Long userId) {
        userService.deleteUserById(userId);
    }
}
