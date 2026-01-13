package de.telran.gardenStore.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@Builder(toBuilder = true)
public class UserCreateRequestDto {

    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 30, message = "User name should be from 2 to 30 characters")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be in a valid format, e.g., example@example.com")
    private String email;

    @Pattern(regexp = "^\\+?\\d{10,15}$", message = "Phone number should have at least 10 characters")
    private String phoneNumber;

    @ToString.Exclude
    @NotBlank(message = "Password is required")
    @Size(min = 5, message = "Password should have at least 5 characters")
    private String password;
}
