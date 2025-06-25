package de.telran.gardenStore.controller;

import de.telran.gardenStore.dto.UserResponseDto;
import de.telran.gardenStore.service.UserServiceInterface;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserServiceInterface userService;

    private final ModelMapper modelMapper;

    @GetMapping
    public List<UserResponseDto> getAllUsers() {
        return userService.getAllUsers().stream().map(user -> modelMapper.map(user, UserResponseDto.class)).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public UserResponseDto getUserById(@PathVariable Long id) {
        return modelMapper.map(userService.getUserById(id), UserResponseDto.class);
    }

    @DeleteMapping("/{id}")
    public void deleteUserById(@PathVariable Long id) {
        userService.deleteUserById(id);
    }
}
