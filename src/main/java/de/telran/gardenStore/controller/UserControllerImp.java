package de.telran.gardenStore.controller;

import de.telran.gardenStore.dto.UserCreateRequestDto;
import de.telran.gardenStore.dto.UserResponseDto;
import de.telran.gardenStore.entity.User;
import de.telran.gardenStore.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserControllerImp implements UserController{

    private final UserService userService;

    private final ModelMapper modelMapper;

    @Override
    @GetMapping
    public List<UserResponseDto> getAllUsers() {
        return userService.getAllUsers().stream().map(user -> modelMapper.map(user, UserResponseDto.class)).collect(Collectors.toList());
    }

    @Override
    @GetMapping("/{id}")
    public UserResponseDto getUserById(@PathVariable Long id) {
        return modelMapper.map(userService.getUserById(id), UserResponseDto.class);
    }

    @Override
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/register")
    public UserResponseDto createUser(@RequestBody UserCreateRequestDto userRequest) {
        return modelMapper.map(
                userService.createUser(
                        modelMapper.map(userRequest, User.class)),
                UserResponseDto.class);
    }

    @Override
    @DeleteMapping("/{id}")
    public void deleteUserById(@PathVariable Long id) {
        userService.deleteUserById(id);
    }
}
