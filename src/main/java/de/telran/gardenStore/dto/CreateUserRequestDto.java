package de.telran.gardenStore.dto;

import de.telran.gardenStore.enums.Role;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode
@Builder
public class CreateUserRequestDto {
    private String name;

    private String email;

    private String phoneNumber;

    private String passwordHash;

    private Role role;
}
