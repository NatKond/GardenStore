package de.telran.gardenStore.dto;

import de.telran.gardenStore.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserRequestDto {
    @NotBlank
    private String name;

    @Email
    private String email;

    private String phoneNumber;

    @NotBlank
    private String passwordHash;

    private Role role;
}
