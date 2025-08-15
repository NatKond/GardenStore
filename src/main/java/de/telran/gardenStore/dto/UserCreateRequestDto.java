package de.telran.gardenStore.dto;

import de.telran.gardenStore.serializer.SensitiveData;
import jakarta.validation.constraints.*;
import lombok.*;

@Data
@Builder(toBuilder = true)
public class UserCreateRequestDto {

    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 30, message = "User name should be from 2 to 30 characters")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Should be a well-formed email address")
    private String email;

    @SensitiveData
    @Pattern(regexp = "^\\+?\\d{10,15}$", message = "Phone number should have at least 10 characters")
    private String phoneNumber;

    @SensitiveData
    @NotBlank(message = "Password is required")
    @Pattern(regexp = "^\\w{5,}$", message = "Password should have at least 5 characters")
    private String password;
}
